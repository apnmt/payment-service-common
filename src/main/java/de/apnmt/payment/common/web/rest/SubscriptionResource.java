package de.apnmt.payment.common.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import de.apnmt.common.errors.BadRequestAlertException;
import de.apnmt.payment.common.service.SubscriptionService;
import de.apnmt.payment.common.service.dto.SubscriptionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link SubscriptionDTO}.
 */
@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionResource {

    private final Logger log = LoggerFactory.getLogger(SubscriptionResource.class);

    private static final String ENTITY_NAME = "payserviceSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubscriptionService subscriptionService;

    public SubscriptionResource(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<SubscriptionDTO> checkout(@RequestBody SubscriptionDTO subscriptionDTO, @RequestHeader("X-paymentMethod") String paymentMethod) throws URISyntaxException {
        this.log.debug("REST request to checkout Subscription");
        if (subscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new subscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubscriptionDTO result = this.subscriptionService.checkout(subscriptionDTO, paymentMethod);
        return ResponseEntity.created(new URI("/api/subscriptions/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(this.applicationName, false, ENTITY_NAME,
                result.getId())).body(result);
    }

    @GetMapping("/{id}")
    public SubscriptionDTO getSubscription(@PathVariable("id") String id) {
        this.log.debug("REST request to get subscription with id {}", id);
        return this.subscriptionService.findOne(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<SubscriptionDTO> getAllSubscriptions(@PathVariable("customerId") String customerId) {
        this.log.debug("REST request to get all subscriptions for customer {}", customerId);
        return this.subscriptionService.findAll(customerId);
    }

}
