package com.mycompany.myapp.drools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class MergedApiResult {

    private String country;
    private String nationality;
    private int age;
    private String banckName;
    private String accNo;
    private Double balance;

    public MergedApiResult(String country, String nationality, int age, String banckName, String accNo, Double balance) {
        this.country = country;
        this.nationality = nationality;
        this.age = age;
        this.banckName = banckName;
        this.accNo = accNo;
        this.balance = balance;
    }

    public MergedApiResult() {}

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
