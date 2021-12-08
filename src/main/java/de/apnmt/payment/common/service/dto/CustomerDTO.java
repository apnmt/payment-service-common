package de.apnmt.payment.common.service.dto;

import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import de.apnmt.payment.common.domain.Customer;

/**
 * A DTO for the {@link Customer} entity.
 */
public class CustomerDTO implements Serializable {

    private static final long serialVersionUID = 4717333632140529077L;
    private String id;

    private String email;

    @NotNull
    private Long organizationId;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        CustomerDTO that = (CustomerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getEmail(), this.getOrganizationId());
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "CustomerDTO{" + "id=" + this.id + ", email='" + this.email + '\'' + ", organizationId=" + this.organizationId + '}';
    }
}
