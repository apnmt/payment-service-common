package de.apnmt.payment.common.service;

import com.stripe.exception.StripeException;
import de.apnmt.payment.common.domain.Customer;
import de.apnmt.payment.common.repository.CustomerRepository;
import de.apnmt.payment.common.service.dto.CustomerDTO;
import de.apnmt.payment.common.service.stripe.CustomerStripeService;
import de.apnmt.payment.common.service.stripe.mapper.PaymentMethodMapper;
import de.apnmt.payment.common.web.rest.errors.BadRequestAlertException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;
    private CustomerStripeService customerStripeService;
    private PaymentMethodMapper paymentMethodMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerStripeService customerStripeService, PaymentMethodMapper paymentMethodMapper) {
        this.customerRepository = customerRepository;
        this.customerStripeService = customerStripeService;
        this.paymentMethodMapper = paymentMethodMapper;
    }

    public Customer createCustomer(CustomerDTO customerDTO) {
        Optional<Customer> maybe = this.customerRepository.findById(customerDTO.getId());
        try {
            if (maybe.isEmpty()) {
                com.stripe.model.Customer stripeResult = this.customerStripeService.createCustomer(customerDTO.getEmail());
                Customer customer = new Customer();
                customer.setId(stripeResult.getId());
                customer.setOrganizationId(customerDTO.getOrganizationId());
                this.customerRepository.save(customer);
                return customer;
            }
        } catch (StripeException ex) {
            throw new BadRequestAlertException(ex.getMessage(), "Stripe", ex.getCode());
        }
        return maybe.get();
    }

    public void createPaymentMethod(String paymentMethod, String customerId) {
        try {
            this.customerStripeService.createPaymentMethod(paymentMethod, customerId);
        } catch (StripeException e) {
            throw new BadRequestAlertException(e.getMessage(), "Stripe", e.getCode());
        }
    }

}
