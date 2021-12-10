package de.apnmt.payment.common.service;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import de.apnmt.common.TopicConstants;
import de.apnmt.common.event.ApnmtEvent;
import de.apnmt.common.event.ApnmtEventType;
import de.apnmt.common.event.value.OrganizationActivationEventDTO;
import de.apnmt.common.sender.ApnmtEventSender;
import de.apnmt.payment.common.domain.Subscription;
import de.apnmt.payment.common.repository.SubscriptionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionExpirationService {

    private final SubscriptionRepository subscriptionRepository;
    private final ApnmtEventSender<OrganizationActivationEventDTO> sender;

    public SubscriptionExpirationService(SubscriptionRepository subscriptionRepository, ApnmtEventSender<OrganizationActivationEventDTO> sender) {
        this.subscriptionRepository = subscriptionRepository;
        this.sender = sender;
    }

    @Async
    @Scheduled(cron = "0 0 * * * *")
    public void checkExpirationOfSubscriptions() {
        List<Subscription> subscriptions = this.subscriptionRepository.findAll();
        for (Subscription subscription : subscriptions) {
            if (subscription.getExpirationDate().isBefore(LocalDateTime.now(ZoneId.systemDefault()))) {
                OrganizationActivationEventDTO organizationActivationEventDTO = new OrganizationActivationEventDTO();
                organizationActivationEventDTO.setOrganizationId(subscription.getCustomer().getOrganizationId());
                organizationActivationEventDTO.setActive(false);
                ApnmtEvent<OrganizationActivationEventDTO> event = new ApnmtEvent<OrganizationActivationEventDTO>().timestamp(LocalDateTime.now())
                        .type(ApnmtEventType.organizationActivationChanged)
                        .value(organizationActivationEventDTO);
                this.sender.send(TopicConstants.ORGANIZATION_ACTIVATION_CHANGED_TOPIC, event);
            }
        }
    }

}
