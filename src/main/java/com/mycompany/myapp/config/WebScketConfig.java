package com.mycompany.myapp.config;

import java.util.Optional;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableWebSocketMessageBroker
public class WebScketConfig implements WebSocketMessageBrokerConfigurer {

    private final JHipsterProperties jHipsterProperties;

    public WebScketConfig(JHipsterProperties jHipsterProperties) {
        this.jHipsterProperties = jHipsterProperties;
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue");

        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] allowedOrigins = Optional
            .ofNullable(jHipsterProperties.getCors().getAllowedOrigins())
            .map(origins -> origins.toArray(new String[0]))
            .orElse(new String[0]);
        registry.addEndpoint("/ws").setAllowedOrigins(allowedOrigins).withSockJS();
    }
}
