package de.apnmt.payment.common.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "product")
    @JsonIgnoreProperties(value = {"subscriptionItems", "product"}, allowSetters = true)
    private Set<Price> prices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product id(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Price> getPrices() {
        return this.prices;
    }

    public Product prices(Set<Price> prices) {
        this.setPrices(prices);
        return this;
    }

    public Product addPrice(Price price) {
        this.prices.add(price);
        price.setProduct(this);
        return this;
    }

    public Product removePrice(Price price) {
        this.prices.remove(price);
        price.setProduct(null);
        return this;
    }

    public void setPrices(Set<Price> prices) {
        if (this.prices != null) {
            this.prices.forEach(i -> i.setProduct(null));
        }
        if (prices != null) {
            prices.forEach(i -> i.setProduct(this));
        }
        this.prices = prices;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return this.id != null && this.id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                ", description='" + getDescription() + "'" +
                "}";
    }
}
