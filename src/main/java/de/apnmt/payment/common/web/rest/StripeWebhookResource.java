package de.apnmt.payment.common.web.rest;

import java.util.Optional;

import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.Invoice;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiResource;
import de.apnmt.payment.common.service.StripeWebhookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for stripe webhooks.
 */
@RestController
@RequestMapping("/api")
public class StripeWebhookResource {

    private final Logger log = LoggerFactory.getLogger(StripeWebhookResource.class);

    private final StripeWebhookService stripeWebhookService;

    public StripeWebhookResource(StripeWebhookService stripeWebhookService) {
        this.stripeWebhookService = stripeWebhookService;
    }

    @PostMapping("/stripe/events")
    public ResponseEntity<Void> handleStripeEvent(HttpEntity<String> httpEntity) throws EventDataObjectDeserializationException {
        this.log.debug("REST request to handle Stripe Webhook");
        String json = httpEntity.getBody();
        Event event = ApiResource.GSON.fromJson(json, Event.class);
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        Optional<StripeObject> maybe = dataObjectDeserializer.getObject();
        if (maybe.isPresent()) {
            if (event.getType().equals("invoice.payment_succeeded")) {
                Invoice invoice = (Invoice) maybe.get();
                this.stripeWebhookService.handleInvoiceSucceeded(invoice);
            } else {
                this.log.info("Unknown Event Type={}. Event will be ignored.", event.getType());
            }
        } else {
            this.log.debug("Stripe object is null. Event will be ignored.");
        }
        return ResponseEntity.noContent().build();
    }

}
