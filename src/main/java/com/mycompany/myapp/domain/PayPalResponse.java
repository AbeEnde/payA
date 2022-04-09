package com.mycompany.myapp.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PayPalResponse {

    String token;

    public PayPalResponse() {}

    public PayPalResponse(String token) {
        super();
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
