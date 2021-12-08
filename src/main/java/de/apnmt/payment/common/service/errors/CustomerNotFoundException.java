package de.apnmt.payment.common.service.errors;

import de.apnmt.common.errors.HttpError;
import org.zalando.problem.Status;

public class CustomerNotFoundException extends HttpError {
    private static final long serialVersionUID = -8553662189276539231L;

    public CustomerNotFoundException(String id) {
        super(Status.NOT_FOUND, "customer.not.found", "Customer with id = " + id + " not found.");
    }
}
