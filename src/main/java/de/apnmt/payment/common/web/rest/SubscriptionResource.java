package de.apnmt.payment.common.web.rest;

import de.apnmt.payment.common.service.SubscriptionService;
import de.apnmt.payment.common.service.dto.SubscriptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing {@link SubscriptionDTO}.
 */
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionResource.class);

    private static final String ENTITY_NAME = "payserviceProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private SubscriptionService subscriptionService;

    public SubscriptionResource(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/checkout")
    public SubscriptionDTO checkout(@RequestBody SubscriptionDTO subscriptionDTO, @RequestHeader("X-paymentMethod") String paymentMethod, @RequestHeader(value = "X-code", required = false) String code) {
        this.log.debug("REST request to checkout Payment");
        SubscriptionDTO result = this.subscriptionService.checkout(subscriptionDTO, paymentMethod, code);
        return result;
    }

    @GetMapping("/{id}")
    public SubscriptionDTO getSubscription(@PathVariable("id") String id) {
        this.log.debug("REST request to get subscription with id {}", id);
        return this.subscriptionService.findOne(id);
    }

    @GetMapping("/organization/{organizationId}")
    public List<SubscriptionDTO> getAllSubscriptions(@PathVariable("organizationId") Long organizationId) {
        this.log.debug("REST request to get all subscriptions for organization {}", organizationId);
        return this.subscriptionService.findAll(organizationId);
    }

}
