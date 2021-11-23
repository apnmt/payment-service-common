package de.apnmt.payment.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Subscription.
 */
@Entity
@Table(name = "subscription")
public class Subscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @OneToMany(mappedBy = "subscription")
    @JsonIgnoreProperties(value = {"price", "subscription"}, allowSetters = true)
    private Set<SubscriptionItem> subscriptionItems = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = {"Subscriptions"}, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Subscription id(String id) {
        this.id = id;
        return this;
    }

    public LocalDateTime getExpirationDate() {
        return this.expirationDate;
    }

    public Subscription expirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
        return this;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Set<SubscriptionItem> getSubscriptionItems() {
        return this.subscriptionItems;
    }

    public Subscription subscriptionItems(Set<SubscriptionItem> subscriptionItems) {
        this.setSubscriptionItems(subscriptionItems);
        return this;
    }

    public Subscription addSubscriptionItem(SubscriptionItem subscriptionItem) {
        this.subscriptionItems.add(subscriptionItem);
        subscriptionItem.seSubscription(this);
        return this;
    }

    public Subscription removeSubscriptionItem(SubscriptionItem subscriptionItem) {
        this.subscriptionItems.remove(subscriptionItem);
        subscriptionItem.seSubscription(null);
        return this;
    }

    public void setSubscriptionItems(Set<SubscriptionItem> subscriptionItems) {
        if (this.subscriptionItems != null) {
            this.subscriptionItems.forEach(i -> i.seSubscription(null));
        }
        if (subscriptionItems != null) {
            subscriptionItems.forEach(i -> i.seSubscription(this));
        }
        this.subscriptionItems = subscriptionItems;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public Subscription customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subscription)) {
            return false;
        }
        return this.id != null && this.id.equals(((Subscription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + getId() +
                ", expirationDate='" + getExpirationDate() + "'" +
                "}";
    }
}
