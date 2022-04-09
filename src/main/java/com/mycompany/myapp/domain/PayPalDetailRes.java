package com.mycompany.myapp.domain;

public class PayPalDetailRes {

    String email;
    String phone;
    String status;
    String invoiceID;

    public PayPalDetailRes() {}

    public PayPalDetailRes(String email, String phone, String status, String invoiceID) {
        super();
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.invoiceID = invoiceID;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(String invoiceID) {
        this.invoiceID = invoiceID;
    }
}
