package de.apnmt.payment.common.service.errors;

import de.apnmt.common.errors.HttpError;
import org.zalando.problem.Status;

public class SubscriptionNotFoundException extends HttpError {
    private static final long serialVersionUID = -3156013019385522649L;

    public SubscriptionNotFoundException(String id) {
        super(Status.NOT_FOUND, "subscription.not.found", "Subscription with id = " + id + " not found.");
    }
}
