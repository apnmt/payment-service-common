package de.apnmt.payment.common.service.dto;

import de.apnmt.payment.domain.Subscription;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link Subscription} entity.
 */
public class SubscriptionDTO implements Serializable {

    private static final long serialVersionUID = -1031736763575543492L;
    private Long id;

    @NotNull
    private LocalDateTime expirationDate;

    private CustomerDTO customer;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getExpirationDate() {
        return this.expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public CustomerDTO getCustomer() {
        return this.customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionDTO)) {
            return false;
        }

        SubscriptionDTO subscriptionDTO = (SubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionDTO{" +
                "id=" + getId() +
                ", expirationDate='" + getExpirationDate() + "'" +
                ", customer=" + getCustomer() +
                "}";
    }
}
