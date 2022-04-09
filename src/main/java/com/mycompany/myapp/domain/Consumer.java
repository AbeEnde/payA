package com.mycompany.myapp.domain;

import com.mycompany.myapp.drools.WholePay;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsSecurityException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.mycompany.myapp.service.DrlService;

@Component
public class Consumer {

    @Autowired
    DrlService drs;

    WholePay p;
    WholePay fmsg = new WholePay();

    @JmsListener(destination = "PaymentQueue")
    public WholePay listener(WholePay msg) {
        try {
            WholePay finlOb = drs.getFinalObj(msg);
            if (!finlOb.getCik().equalsIgnoreCase(fmsg.getCik())) {
                customers.add(finlOb);
                fmsg = finlOb;
            }

            return msg;
        } catch (NumberFormatException | JmsSecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<WholePay> customers = new ArrayList<>();

    public WholePay chenge(WholePay pay) {
        this.p = pay;

        return pay;
    }

    public List<WholePay> dequee() {
        return customers;
    }
}
