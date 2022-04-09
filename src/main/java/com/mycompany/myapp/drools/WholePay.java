package com.mycompany.myapp.drools;

import java.io.Serializable;

public class WholePay implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;
    private String cik;
    private String ccc;
    private Long paymentAmount;
    private String name;
    private String email;
    private String phone;
    private String country;
    private String nationality;
    private int age;
    private String banckName;
    private String accNo;
    private Double balance;
    private String status;

    public WholePay(
        String cik,
        String ccc,
        Long paymentAmount,
        String name,
        String email,
        String phone,
        String country,
        String nationality,
        int age,
        String banckName,
        String accNo,
        Double balance,
        String status
    ) {
        this.cik = cik;
        this.ccc = ccc;
        this.paymentAmount = paymentAmount;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.nationality = nationality;
        this.age = age;
        this.banckName = banckName;
        this.accNo = accNo;
        this.balance = balance;
        this.status = status;
    }

    public WholePay() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBanckName() {
        return banckName;
    }

    public void setBanckName(String banckName) {
        this.banckName = banckName;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
