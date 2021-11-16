package de.apnmt.payment.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Price.
 */
@Entity
@Table(name = "price")
public class Price implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @NotNull
    @Column(name = "currency", nullable = false)
    private String currency;

    @NotNull
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

    @NotNull
    @Column(name = "interval", nullable = false)
    private String interval;

    @OneToMany(mappedBy = "price")
    @JsonIgnoreProperties(value = { "price", "subscription" }, allowSetters = true)
    private Set<SubscriptionItem> subscriptionItems = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "prices" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Price id(Long id) {
        this.id = id;
        return this;
    }

    public String getNickname() {
        return this.nickname;
    }

    public Price nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Price currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Price postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Long getAmount() {
        return this.amount;
    }

    public Price amount(Long amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getInterval() {
        return this.interval;
    }

    public Price interval(String interval) {
        this.interval = interval;
        return this;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public Set<SubscriptionItem> geSubscriptionItems() {
        return this.subscriptionItems;
    }

    public Price subscriptionItems(Set<SubscriptionItem> subscriptionItems) {
        this.seSubscriptionItems(subscriptionItems);
        return this;
    }

    public Price addSubscriptionItem(SubscriptionItem subscriptionItem) {
        this.subscriptionItems.add(subscriptionItem);
        subscriptionItem.setPrice(this);
        return this;
    }

    public Price removeSubscriptionItem(SubscriptionItem subscriptionItem) {
        this.subscriptionItems.remove(subscriptionItem);
        subscriptionItem.setPrice(null);
        return this;
    }

    public void seSubscriptionItems(Set<SubscriptionItem> subscriptionItems) {
        if (this.subscriptionItems != null) {
            this.subscriptionItems.forEach(i -> i.setPrice(null));
        }
        if (subscriptionItems != null) {
            subscriptionItems.forEach(i -> i.setPrice(this));
        }
        this.subscriptionItems = subscriptionItems;
    }

    public Product getProduct() {
        return this.product;
    }

    public Price product(Product product) {
        this.setProduct(product);
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
            return false;
        }
        return id != null && id.equals(((Price) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Price{" +
            "id=" + getId() +
            ", nickname='" + getNickname() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", amount=" + getAmount() +
            ", interval='" + getInterval() + "'" +
            "}";
    }
}
