package com.mycompany.myapp.service;

import com.mycompany.myapp.drools.WholePay;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrlService {

    @Autowired
    private KieContainer kieContainer;

    public WholePay getFinalObj(WholePay wpreq) {
        KieSession kieSession = kieContainer.newKieSession();

        kieSession.insert(wpreq);
        kieSession.fireAllRules();
        kieSession.dispose();

        return wpreq;
    }
}
