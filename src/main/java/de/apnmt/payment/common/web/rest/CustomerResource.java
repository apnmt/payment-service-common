package de.apnmt.payment.common.web.rest;

import java.net.URI;
import java.net.URISyntaxException;

import de.apnmt.common.errors.BadRequestAlertException;
import de.apnmt.payment.common.domain.Customer;
import de.apnmt.payment.common.service.CustomerService;
import de.apnmt.payment.common.service.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing {@link Customer}.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "payserviceCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("")
    public ResponseEntity<CustomerDTO> create(@RequestBody CustomerDTO customer) throws URISyntaxException {
        this.log.debug("REST request to create Customer: {}", customer);
        if (customer.getId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerDTO result = this.customerService.createCustomer(customer);
        return ResponseEntity.created(new URI("/api/customers/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(this.applicationName, false, ENTITY_NAME,
                result.getId())).body(result);
    }

    @PostMapping("/{customerId}/paymentMethods")
    public ResponseEntity<Void> createPaymentMethods(@PathVariable("customerId") String customerId, @RequestHeader("X-paymentMethod") String paymentMethod) {
        this.customerService.createPaymentMethod(paymentMethod, customerId);
        return ResponseEntity.noContent().build();
    }
}
