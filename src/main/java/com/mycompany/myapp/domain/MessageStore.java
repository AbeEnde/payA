package com.mycompany.myapp.domain;

import java.util.ArrayList;
import java.util.List;

public class MessageStore {

    private List<Pay> customers = new ArrayList<>();

    public void add(Pay customer) {
        customers.add(customer);
    }

    public void clear(int id) {
        customers.remove(id);
    }

    public List<Pay> getAll() {
        return customers;
    }
}
