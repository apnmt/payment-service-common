package de.apnmt.payment.common.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.apnmt.payment.common.domain.enumeration.Currency;
import de.apnmt.payment.common.domain.enumeration.Interval;

/**
 * A Price.
 */
@Entity
@Table(name = "price")
public class Price implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Column(name = "nickname", nullable = false)
    private String nickname;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "interval_type", nullable = false)
    private Interval interval;

    @OneToMany(mappedBy = "price")
    @JsonIgnoreProperties(value = {"price", "subscription"}, allowSetters = true)
    private Set<SubscriptionItem> subscriptionItems = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = {"prices"}, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Price id(String id) {
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

    public Currency getCurrency() {
        return this.currency;
    }

    public Price currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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

    public Interval getInterval() {
        return this.interval;
    }

    public Price interval(Interval interval) {
        this.interval = interval;
        return this;
    }

    public void setInterval(Interval interval) {
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
        return this.id != null && this.id.equals(((Price) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return this.getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Price{" + "id=" + this.getId() + ", nickname='" + this.getNickname() + "'" + ", currency='" + this.getCurrency() + "'" + ", amount=" + this.getAmount() + ", " +
                "interval='" + this.getInterval() + "'" + "}";
    }
}
