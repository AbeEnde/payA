package com.mycompany.myapp.domain;

import java.util.HashMap;
import java.util.Map;

public class PayPalProperty {

    public static final Map<String, String> getAcctAndConfig() {
        Map<String, String> configMap = new HashMap<String, String>();
        configMap.putAll(getConfig());

        configMap.put("acct1.UserName", "sb-v3z43o8598034_api1.business.example.com");
        configMap.put("acct1.Password", "T6VH545889D3JKG7");
        configMap.put("acct1.Signature", "ApJTjs9JGbPMPRqOqsMiJNhM6bB8AOAQN5WWBLdWWes8dpcLNKKhT55q");
        configMap.put("mode", "sandbox");

        return configMap;
    }

    public static final Map<String, String> getConfig() {
        Map<String, String> configMap = new HashMap<String, String>();

        configMap.put("mode", "sandbox");

        return configMap;
    }
}
