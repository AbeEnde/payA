package com.mycompany.myapp.web;

import java.util.Enumeration;
import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MessgageControler {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void readMessageFromQueue() {
        jmsTemplate.browse(
            "netsurfingzone-queue",
            new BrowserCallback<TextMessage>() {
                @Override
                @GetMapping("/paysender")
                public TextMessage doInJms(Session session, QueueBrowser browser) throws JMSException {
                    @SuppressWarnings("unchecked")
                    Enumeration<TextMessage> messages = browser.getEnumeration();
                    while (messages.hasMoreElements()) {}
                    return (TextMessage) messages;
                }
            }
        );
    }
}
