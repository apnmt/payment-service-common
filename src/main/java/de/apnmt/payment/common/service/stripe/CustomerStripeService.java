package de.apnmt.payment.common.service.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomerStripeService extends StripeService {

    public Customer createCustomer(String email) throws StripeException {
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("email", email);
        return Customer.create(customerParams);
    }

    public Customer createPaymentMethod(String paymentMethodId, String customerId) throws StripeException {
        Map<String, Object> customerParams = new HashMap<>();
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customerId);
        PaymentMethod updatedPaymentMethod = paymentMethod.attach(params);

        setInvoiceSettings(customerParams, updatedPaymentMethod.getId());
        Customer customer = Customer.retrieve(customerId);
        customer.update(customerParams);
        return customer;
    }

    private void setInvoiceSettings(Map<String, Object> customerParams, String paymentMethod) {
        Map<String, String> invoiceSettings = new HashMap<>();
        invoiceSettings.put("default_payment_method", paymentMethod);
        customerParams.put("invoice_settings", invoiceSettings);
    }

}
