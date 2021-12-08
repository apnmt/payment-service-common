package de.apnmt.payment.common.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import de.apnmt.payment.common.domain.Price;
import de.apnmt.payment.common.domain.enumeration.Currency;
import de.apnmt.payment.common.domain.enumeration.Interval;

/**
 * A DTO for the {@link Price} entity.
 */
public class PriceDTO implements Serializable {

    private static final long serialVersionUID = -1691310934193080984L;
    private String id;

    @NotNull
    private String nickname;

    @NotNull
    private Currency currency;

    @NotNull
    private Long amount;

    @NotNull
    private Interval interval;

    private ProductDTO product;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Interval getInterval() {
        return this.interval;
    }

    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    public ProductDTO getProduct() {
        return this.product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceDTO)) {
            return false;
        }

        PriceDTO priceDTO = (PriceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, priceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceDTO{" + "id=" + this.getId() + ", nickname='" + this.getNickname() + "'" + ", currency='" + this.getCurrency() + "'" + ", amount" + "=" + this.getAmount() + ", interval='" + this.getInterval() + "'" + ", product=" + this.getProduct() + "}";
    }
}
