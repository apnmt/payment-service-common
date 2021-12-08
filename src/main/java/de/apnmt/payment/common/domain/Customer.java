package de.apnmt.payment.common.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Column(name = "organization_id", nullable = false)
    private Long organizationId;

    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties(value = {"subscriptionItems", "customer"}, allowSetters = true)
    private Set<Subscription> subscriptions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Customer id(String id) {
        this.id = id;
        return this;
    }

    public Long getOrganizationId() {
        return this.organizationId;
    }

    public Customer organizationId(Long organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Set<Subscription> getSubscriptions() {
        return this.subscriptions;
    }

    public Customer Subscriptions(Set<Subscription> subscriptions) {
        this.setSubscriptions(subscriptions);
        return this;
    }

    public Customer addSubscription(Subscription subscription) {
        this.subscriptions.add(subscription);
        subscription.setCustomer(this);
        return this;
    }

    public Customer removeSubscription(Subscription subscription) {
        this.subscriptions.remove(subscription);
        subscription.setCustomer(null);
        return this;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        if (this.subscriptions != null) {
            this.subscriptions.forEach(i -> i.setCustomer(null));
        }
        if (subscriptions != null) {
            subscriptions.forEach(i -> i.setCustomer(this));
        }
        this.subscriptions = subscriptions;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return this.id != null && this.id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return this.getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" + "id=" + this.getId() + ", organizationId=" + this.getOrganizationId() + "}";
    }
}
