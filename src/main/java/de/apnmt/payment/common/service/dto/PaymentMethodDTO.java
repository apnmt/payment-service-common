package de.apnmt.payment.common.service.dto;

public class PaymentMethodDTO {

    private String id;

    private String brand;

    private String last4;

    private boolean standard;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand() {
        return this.brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getLast4() {
        return this.last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }

    public boolean isStandard() {
        return this.standard;
    }

    public void setStandard(boolean standard) {
        this.standard = standard;
    }
}
