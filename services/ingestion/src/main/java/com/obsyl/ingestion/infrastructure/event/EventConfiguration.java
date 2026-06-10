package com.obsyl.ingestion.infrastructure.event;

import com.obsyl.ingestion.application.event.EventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfiguration {

    @Bean
    public EventPublisher eventPublisher(InMemoryEventPublisher inMemoryEventPublisher) {
        return inMemoryEventPublisher;
    }
}
