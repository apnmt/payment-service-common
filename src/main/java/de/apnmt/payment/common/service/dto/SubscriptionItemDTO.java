package de.apnmt.payment.common.service.dto;

import de.apnmt.payment.common.domain.SubscriptionItem;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link SubscriptionItem} entity.
 */
public class SubscriptionItemDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer quantity;

    private PriceDTO price;

    private SubscriptionDTO subscription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public PriceDTO getPrice() {
        return price;
    }

    public void setPrice(PriceDTO price) {
        this.price = price;
    }

    public SubscriptionDTO geSubscription() {
        return subscription;
    }

    public void seSubscription(SubscriptionDTO subscription) {
        this.subscription = subscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionItemDTO)) {
            return false;
        }

        SubscriptionItemDTO subscriptionItemDTO = (SubscriptionItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subscriptionItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            ", subscription=" + geSubscription() +
            "}";
    }
}
