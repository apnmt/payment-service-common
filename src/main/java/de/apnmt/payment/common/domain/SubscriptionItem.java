package de.apnmt.payment.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A SubscriptionItem.
 */
@Entity
@Table(name = "subscription_item")
public class SubscriptionItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JsonIgnoreProperties(value = {"subscriptionItems", "product"}, allowSetters = true)
    private Price price;

    @ManyToOne
    @JsonIgnoreProperties(value = {"subscriptionItems", "customer"}, allowSetters = true)
    private Subscription subscription;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SubscriptionItem id(String id) {
        this.id = id;
        return this;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public SubscriptionItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Price getPrice() {
        return this.price;
    }

    public SubscriptionItem price(Price price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Subscription geSubscription() {
        return this.subscription;
    }

    public SubscriptionItem subscription(Subscription subscription) {
        this.seSubscription(subscription);
        return this;
    }

    public void seSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubscriptionItem)) {
            return false;
        }
        return this.id != null && this.id.equals(((SubscriptionItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubscriptionItem{" +
                "id=" + getId() +
                ", quantity=" + getQuantity() +
                "}";
    }
}
