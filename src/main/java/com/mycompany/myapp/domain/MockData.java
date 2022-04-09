package com.mycompany.myapp.domain;

public class MockData {

    String trackId;
    String name;
    String email;
    String address;
    int creditCard;

    public MockData() {}

    public MockData(String trackId, String name, String email, String address, int creditCard) {
        super();
        this.trackId = trackId;
        this.name = name;
        this.email = email;
        this.address = address;
        this.creditCard = creditCard;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(int creditCard) {
        this.creditCard = creditCard;
    }
}
