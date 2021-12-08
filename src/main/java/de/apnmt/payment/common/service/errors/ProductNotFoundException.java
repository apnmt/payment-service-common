package de.apnmt.payment.common.service.errors;

import de.apnmt.common.errors.HttpError;
import org.zalando.problem.Status;

public class ProductNotFoundException extends HttpError {
    private static final long serialVersionUID = -3156013019385522649L;

    public ProductNotFoundException(String id) {
        super(Status.NOT_FOUND, "product.not.found", "Product with id = " + id + " not found.");
    }
}
