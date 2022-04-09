package com.mycompany.myapp.domain;

import java.io.Serializable;

public class Message implements Serializable {

    private String msg;
    private static final long serialVersionUID = 1L;

    public Message(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
