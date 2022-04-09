package com.mycompany.myapp.config;

import javax.jms.Queue;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class JMSConfig {

    @Value("active-mq-url")
    private String activeMqUrl;

    @Bean
    public Queue queue() {
        return new ActiveMQQueue("PaymentQueue");
    }

    @Bean
    public ActiveMQConnectionFactory connectionFatory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        factory.setTrustAllPackages(true);

        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(connectionFatory());
    }
}
