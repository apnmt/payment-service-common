package de.apnmt.payment.common.service;

import com.stripe.exception.StripeException;
import de.apnmt.common.errors.BadRequestAlertException;
import de.apnmt.payment.common.domain.Customer;
import de.apnmt.payment.common.repository.CustomerRepository;
import de.apnmt.payment.common.service.dto.CustomerDTO;
import de.apnmt.payment.common.service.mapper.CustomerMapper;
import de.apnmt.payment.common.service.stripe.CustomerStripeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerStripeService customerStripeService;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerStripeService customerStripeService, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerStripeService = customerStripeService;
        this.customerMapper = customerMapper;
    }

    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Optional<Customer> maybe = customerDTO.getId() == null ? Optional.empty() : this.customerRepository.findById(customerDTO.getId());
        try {
            if (maybe.isEmpty()) {
                com.stripe.model.Customer stripeResult = this.customerStripeService.createCustomer(customerDTO.getEmail());
                Customer customer = new Customer();
                customer.setId(stripeResult.getId());
                customer.setOrganizationId(customerDTO.getOrganizationId());
                this.customerRepository.save(customer);
                return this.customerMapper.toDto(customer);
            }
        } catch (StripeException ex) {
            throw new BadRequestAlertException(ex.getMessage(), "Stripe", ex.getCode());
        }
        return this.customerMapper.toDto(maybe.get());
    }

    public List<CustomerDTO> findAll() {
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toDto(customers);
    }

    public void createPaymentMethod(String paymentMethod, String customerId) {
        try {
            this.customerStripeService.createPaymentMethod(paymentMethod, customerId);
        } catch (StripeException e) {
            throw new BadRequestAlertException(e.getMessage(), "Stripe", e.getCode());
        }
    }

    /**
     * Delete all customers.
     */
    public void deleteAll() {
        customerRepository.deleteAll();
    }

}
