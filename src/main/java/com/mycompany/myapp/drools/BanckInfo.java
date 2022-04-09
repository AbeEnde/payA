package com.mycompany.myapp.drools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties
public class BanckInfo implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;
    private String cik;

    public String getCik() {
        return cik;
    }

    public void setCik(String cik) {
        this.cik = cik;
    }

    private String banckName;
    private String accNo;
    private Double deposit;

    public BanckInfo() {}

    public BanckInfo(String cik, String banckName, String accNo, Double deposit) {
        this.cik = cik;
        this.banckName = banckName;
        this.accNo = accNo;
        this.deposit = deposit;
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

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }
}
