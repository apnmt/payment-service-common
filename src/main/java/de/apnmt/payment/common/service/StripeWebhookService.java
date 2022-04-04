package de.apnmt.payment.common.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import de.apnmt.common.TopicConstants;
import de.apnmt.common.event.ApnmtEvent;
import de.apnmt.common.event.ApnmtEventType;
import de.apnmt.common.event.value.OrganizationActivationEventDTO;
import de.apnmt.common.sender.ApnmtEventSender;
import de.apnmt.payment.common.domain.Subscription;
import de.apnmt.payment.common.repository.SubscriptionRepository;
import de.apnmt.payment.common.service.stripe.SubscriptionStripeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Service
public class StripeWebhookService {

    private final Logger log = LoggerFactory.getLogger(StripeWebhookService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionStripeService subscriptionStripeService;
    private final ApnmtEventSender<OrganizationActivationEventDTO> sender;

    public StripeWebhookService(SubscriptionRepository subscriptionRepository, SubscriptionStripeService subscriptionStripeService,
                                ApnmtEventSender<OrganizationActivationEventDTO> sender) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionStripeService = subscriptionStripeService;
        this.sender = sender;
    }

    public void handleInvoiceSucceeded(Invoice invoice) {
        Optional<Subscription> optSubscription = this.subscriptionRepository.findById(invoice.getSubscription());
        if (optSubscription.isPresent()) {
            Subscription subscription = optSubscription.get();
            try {
                com.stripe.model.Subscription stripeSub = this.subscriptionStripeService.getSubscription(subscription.getId());
                LocalDateTime expires = LocalDateTime.ofInstant(Instant.ofEpochSecond(stripeSub.getCurrentPeriodEnd()), ZoneId.systemDefault());
                LocalDateTime currentDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());
                if (expires.isBefore(currentDateTime)) {
                    this.log.debug("Expiration Date: {} is before current DateTime {}. Event will be ignored.", expires, currentDateTime);
                    return;
                }
                // set expires to 3 days after the next billing date, to give
                // failed charges a chance to automatically retry
                LocalDateTime expirationDate = expires.plusDays(3);
                subscription.setExpirationDate(expirationDate);
                this.subscriptionRepository.save(subscription);
                this.sendActivationEvent(subscription);
            } catch (StripeException e) {
                this.log.error("Error while retrieving Subscription from Stripe", e);
            }
        } else {
            this.log.info("Subscription not found with id: {}. Event will be ignored.", invoice.getSubscription());
        }
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

}
