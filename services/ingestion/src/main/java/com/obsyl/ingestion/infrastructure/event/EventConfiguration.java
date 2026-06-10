package com.obsyl.ingestion.infrastructure.event;

import com.obsyl.ingestion.application.event.EventPublisher;
import com.obsyl.ingestion.infrastructure.buffer.EventBuffer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class EventConfiguration {

    @Bean
    public EventPublisher eventPublisher(EventBuffer eventBuffer, InMemoryEventPublisher delegatePublisher) {
        return new BatchedEventPublisher(eventBuffer, delegatePublisher);
    }
}
