package de.apnmt.payment.common.web.rest;

import de.apnmt.payment.common.domain.Customer;
import de.apnmt.payment.common.service.CustomerService;
import de.apnmt.payment.common.service.dto.CustomerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link Customer}.
 */
@RestController
@RequestMapping("/api/customers")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("")
    public ResponseEntity<Void> create(@RequestBody CustomerDTO customer) {
        this.log.debug("REST request to create Customer: {}", customer);
        this.customerService.createCustomer(customer);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{customerId}/paymentMethods")
    public ResponseEntity<Void> createPaymentMethods(@PathVariable("customerId") String customerId, @RequestHeader("X-paymentMethod") String paymentMethod) {
        this.customerService.createPaymentMethod(paymentMethod, customerId);
        return ResponseEntity.noContent().build();
    }
}
