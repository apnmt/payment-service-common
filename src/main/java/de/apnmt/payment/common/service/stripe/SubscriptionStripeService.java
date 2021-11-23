package de.apnmt.payment.common.service.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Subscription;
import de.apnmt.payment.common.domain.SubscriptionItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubscriptionStripeService extends StripeService {

    @Value("${application.payservice.trial-period-days}")
    private int trialPeriodDays;

    public Subscription createSubscription(de.apnmt.payment.common.domain.Subscription subscription, String customerId) throws StripeException {
        List<Object> items = new ArrayList<>();
        for (SubscriptionItem item : subscription.getSubscriptionItems()) {
            Map<String, Object> i = new HashMap<>();
            i.put("price", item.getPrice().getId());
            i.put("quantity", item.getQuantity());
            items.add(i);
        }
        Map<String, Object> expand = new HashMap<>();
        expand.put("0", "latest_invoice.payment_intent");
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        params.put("items", items);
        params.put("expand", expand);
        params.put("trial_period_days", this.trialPeriodDays);
        return Subscription.create(params);
    }

    public Subscription getSubscription(String id) throws StripeException {
        return Subscription.retrieve(id);
    }

}
