package de.apnmt.payment.common.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import com.stripe.exception.StripeException;
import de.apnmt.common.TopicConstants;
import de.apnmt.common.errors.BadRequestAlertException;
import de.apnmt.common.event.ApnmtEvent;
import de.apnmt.common.event.ApnmtEventType;
import de.apnmt.common.event.value.OrganizationActivationEventDTO;
import de.apnmt.common.sender.ApnmtEventSender;
import de.apnmt.payment.common.domain.Customer;
import de.apnmt.payment.common.domain.Price;
import de.apnmt.payment.common.domain.Subscription;
import de.apnmt.payment.common.domain.SubscriptionItem;
import de.apnmt.payment.common.repository.CustomerRepository;
import de.apnmt.payment.common.repository.PriceRepository;
import de.apnmt.payment.common.repository.SubscriptionRepository;
import de.apnmt.payment.common.service.dto.CustomerDTO;
import de.apnmt.payment.common.service.dto.SubscriptionDTO;
import de.apnmt.payment.common.service.errors.CustomerNotFoundException;
import de.apnmt.payment.common.service.errors.SubscriptionNotFoundException;
import de.apnmt.payment.common.service.mapper.CustomerMapper;
import de.apnmt.payment.common.service.mapper.SubscriptionMapper;
import de.apnmt.payment.common.service.stripe.SubscriptionStripeService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PriceRepository priceRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final CustomerService customerService;
    private final PriceService priceService;
    private final SubscriptionStripeService subscriptionStripeService;
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final ApnmtEventSender<OrganizationActivationEventDTO> sender;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, PriceRepository priceRepository, SubscriptionMapper subscriptionMapper,
                               CustomerService customerService, PriceService priceService, SubscriptionStripeService subscriptionStripeService,
                               CustomerRepository customerRepository, CustomerMapper customerMapper, ApnmtEventSender<OrganizationActivationEventDTO> sender) {
        this.subscriptionRepository = subscriptionRepository;
        this.priceRepository = priceRepository;
        this.subscriptionMapper = subscriptionMapper;
        this.customerService = customerService;
        this.priceService = priceService;
        this.subscriptionStripeService = subscriptionStripeService;
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.sender = sender;
    }

    public SubscriptionDTO checkout(SubscriptionDTO subscriptionDTO, String paymentMethod) {
        try {
            Subscription subscription = this.subscriptionMapper.toEntity(subscriptionDTO);
            this.validateSubscription(subscription);
            CustomerDTO customerDTO = this.customerService.createCustomer(subscriptionDTO.getCustomer());
            Customer customer = this.customerMapper.toEntity(customerDTO);
            this.customerService.createPaymentMethod(paymentMethod, customer.getId());
            com.stripe.model.Subscription stripeResult = this.subscriptionStripeService.createSubscription(subscription, customer.getId());
            subscriptionDTO = this.handleSubscription(stripeResult, customer);
        } catch (StripeException ex) {
            throw new BadRequestAlertException(ex.getMessage(), "Stripe", ex.getCode());
        }

        return subscriptionDTO;
    }

    private void validateSubscription(Subscription subscription) {
        for (SubscriptionItem item : subscription.getSubscriptionItems()) {
            Price price = this.priceService.validatePrice(item.getPrice());
            item.setPrice(price);
        }
    }

    private SubscriptionDTO handleSubscription(com.stripe.model.Subscription subscription, Customer customer) {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
        if (subscription.getStatus().equals("trialing") || subscription.getStatus().equals("active")) {
            subscriptionDTO = this.saveSubscription(subscription, customer);
        }
        return subscriptionDTO;
    }

    private SubscriptionDTO saveSubscription(com.stripe.model.Subscription subscription, Customer customer) {
        Subscription s = new Subscription();
        s.setId(subscription.getId());
        s.setCustomer(customer);
        List<com.stripe.model.SubscriptionItem> items = subscription.getItems().getData();
        for (com.stripe.model.SubscriptionItem item : items) {
            com.stripe.model.Price p = item.getPrice();
            Optional<Price> optPrice = this.priceRepository.findById(p.getId());
            if (optPrice.isPresent()) {
                SubscriptionItem subscriptionItem = new SubscriptionItem();
                subscriptionItem.setId(item.getId());
                Price price = optPrice.get();
                subscriptionItem.setPrice(price);
                subscriptionItem.setQuantity(item.getQuantity().intValue());
                s.addSubscriptionItem(subscriptionItem);
            }
        }
        if (subscription.getStatus().equals("active")) {
            // setting time to one hour in the future
            // webhook will set the expiration date
            LocalDateTime expirationDate = LocalDateTime.now().plusHours(1);
            s.setExpirationDate(expirationDate);
        } else if (subscription.getStatus().equals("trialing")) {
            LocalDateTime expirationDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(subscription.getCurrentPeriodEnd()), TimeZone.getDefault().toZoneId());
            s.setExpirationDate(expirationDate);
        }
        s = this.subscriptionRepository.save(s);
        this.sendActivationEvent(s);
        return this.subscriptionMapper.toDto(s);
    }

    private void sendActivationEvent(Subscription subscription) {
        OrganizationActivationEventDTO organizationActivationEventDTO = new OrganizationActivationEventDTO();
        organizationActivationEventDTO.setOrganizationId(subscription.getCustomer().getOrganizationId());
        organizationActivationEventDTO.setActive(true);
        ApnmtEvent<OrganizationActivationEventDTO> event = new ApnmtEvent<OrganizationActivationEventDTO>().timestamp(LocalDateTime.now())
                .type(ApnmtEventType.organizationActivationChanged)
                .value(organizationActivationEventDTO);
        this.sender.send(TopicConstants.ORGANIZATION_ACTIVATION_CHANGED_TOPIC, event);
    }

    public SubscriptionDTO findOne(String id) {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if (subscription.isEmpty()) {
            throw new SubscriptionNotFoundException(id);
        }
        return this.subscriptionMapper.toDto(subscription.get());
    }

    public List<SubscriptionDTO> findAll(String customerId) {
        Optional<Customer> customer = this.customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException(customerId);
        }
        List<Subscription> subscriptions = this.subscriptionRepository.findAllByCustomer(customer.get());
        return this.subscriptionMapper.toDto(subscriptions);
    }

}
