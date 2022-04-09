package com.mycompany.myapp.domain;

import java.io.Serializable;

public class Pay implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;

    String cik;
    String ccc;
    Long paymentAmount;
    String name;
    String email;
    String phone;

    public Pay() {}

    public Pay(String cik, String ccc, Long paymentAmount, String name, String email, String phone) {
        super();
        this.cik = cik;
        this.ccc = ccc;
        this.paymentAmount = paymentAmount;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Pay(Pay message) {}

    public String getCik() {
        return cik;
    }

    public void setCik(String cik) {
        this.cik = cik;
    }

    public String getCcc() {
        return ccc;
    }

    public void setCcc(String ccc) {
        this.ccc = ccc;
    }

    public Long getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Long paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
