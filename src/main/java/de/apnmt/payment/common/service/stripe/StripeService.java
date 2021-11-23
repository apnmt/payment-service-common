package de.apnmt.payment.common.service.stripe;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;

public abstract class StripeService {

    @Value("${application.payservice.stripekey}")
    private String stripeKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = this.stripeKey;
    }

}
