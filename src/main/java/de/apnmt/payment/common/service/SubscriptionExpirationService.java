package de.apnmt.payment.common.service;


import de.apnmt.payment.common.domain.Subscription;
import de.apnmt.payment.common.repository.SubscriptionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class SubscriptionExpirationService {

    private SubscriptionRepository subscriptionRepository;

    public SubscriptionExpirationService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Async
    @Scheduled(cron = "0 0 * * * *")
    public void checkExpirationOfSubscriptions() {
        List<Subscription> subscriptions = this.subscriptionRepository.findAll();
        for (Subscription subscription : subscriptions) {
            if (subscription.getExpirationDate().isBefore(LocalDateTime.now(ZoneId.systemDefault()))) {
                // TODO send organization deactivation event
            }
        }
    }

}
