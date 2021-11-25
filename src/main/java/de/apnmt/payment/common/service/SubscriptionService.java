package de.apnmt.payment.common.service;

import com.stripe.exception.StripeException;
import de.apnmt.payment.common.domain.Customer;
import de.apnmt.payment.common.domain.Price;
import de.apnmt.payment.common.domain.Subscription;
import de.apnmt.payment.common.domain.SubscriptionItem;
import de.apnmt.payment.common.repository.CustomerRepository;
import de.apnmt.payment.common.repository.PriceRepository;
import de.apnmt.payment.common.repository.SubscriptionRepository;
import de.apnmt.payment.common.service.dto.SubscriptionDTO;
import de.apnmt.payment.common.service.errors.CustomerNotFoundException;
import de.apnmt.payment.common.service.errors.SubscriptionNotFoundException;
import de.apnmt.payment.common.service.mapper.SubscriptionMapper;
import de.apnmt.payment.common.service.stripe.SubscriptionStripeService;
import de.apnmt.payment.common.web.rest.errors.BadRequestAlertException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class SubscriptionService {

    private SubscriptionRepository subscriptionRepository;
    private PriceRepository priceRepository;
    private SubscriptionMapper subscriptionMapper;
    private CustomerService customerService;
    private PriceService priceService;
    private SubscriptionStripeService subscriptionStripeService;
    private CustomerRepository customerRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository, PriceRepository priceRepository, SubscriptionMapper subscriptionMapper, CustomerService customerService, PriceService priceService, SubscriptionStripeService subscriptionStripeService, CustomerRepository customerRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.priceRepository = priceRepository;
        this.subscriptionMapper = subscriptionMapper;
        this.customerService = customerService;
        this.priceService = priceService;
        this.subscriptionStripeService = subscriptionStripeService;
        this.customerRepository = customerRepository;
    }

    public SubscriptionDTO checkout(SubscriptionDTO subscriptionDTO, String paymentMethod, String code) {
        try {
            Subscription subscription = this.subscriptionMapper.toEntity(subscriptionDTO);
            validateSubscription(subscription);
            Customer customer = this.customerService.createCustomer(subscriptionDTO.getCustomer());
            this.customerService.createPaymentMethod(paymentMethod, customer.getId());
            com.stripe.model.Subscription stripeResult = this.subscriptionStripeService.createSubscription(subscription, customer.getId());
            subscriptionDTO = handleSubscription(stripeResult, customer);
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
            subscriptionDTO = saveSubscription(subscription, customer);
            // TODO send organization activation event
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
            LocalDateTime expirationDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(subscription.getCurrentPeriodEnd()),
                    TimeZone.getDefault().toZoneId());
            s.setExpirationDate(expirationDate);
        }
        s = this.subscriptionRepository.save(s);
        return this.subscriptionMapper.toDto(s);
    }

    public SubscriptionDTO findOne(String id) {
        Optional<Subscription> subscription = this.subscriptionRepository.findById(id);
        if (!subscription.isPresent()) {
            throw new SubscriptionNotFoundException();
        }
        SubscriptionDTO sub = this.subscriptionMapper.toDto(subscription.get());
        return sub;
    }

    public List<SubscriptionDTO> findAll(String customerId) {
        Optional<Customer> customer = this.customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new CustomerNotFoundException();
        }
        List<Subscription> subscriptions = this.subscriptionRepository.findAllByCustomer(customer.get());
        return this.subscriptionMapper.toDto(subscriptions);
    }

}
