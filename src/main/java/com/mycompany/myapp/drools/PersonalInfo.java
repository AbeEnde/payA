package com.mycompany.myapp.drools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties
public class PersonalInfo implements Serializable {

    private static final long serialVersionUID = -295422703255886286L;
    private String country;
    private String nationality;
    private int age;

    public PersonalInfo() {}

    public PersonalInfo(String country, String nationality, int age) {
        this.country = country;
        this.nationality = nationality;
        this.age = age;
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
}
