package com.mycompany.myapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mycompany.myapp.domain.MessageStore;

@Configuration
public class ACconf {

    @Bean
    public MessageStore messageStore() {
        return new MessageStore();
    }
}
