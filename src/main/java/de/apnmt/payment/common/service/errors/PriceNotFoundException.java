package de.apnmt.payment.common.service.errors;

import de.apnmt.common.errors.HttpError;
import org.zalando.problem.Status;

public class PriceNotFoundException extends HttpError {
    
    public PriceNotFoundException(String id) {
        super(Status.NOT_FOUND, "price.not.found", "Price with id = " + id + " not found.");
    }
}
