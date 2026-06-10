package com.obsyl.ingestion.infrastructure.event;

import com.obsyl.ingestion.application.event.EventPublisher;
import com.obsyl.ingestion.domain.event.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Downstream in-memory publisher that receives batches from {@link BatchedEventPublisher}.
 * Simulates stream delivery until Kafka is introduced.
 */
@Component
public class InMemoryEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(InMemoryEventPublisher.class);

    private final List<DomainEvent> publishedEvents = new CopyOnWriteArrayList<>();

    @Override
    public void publish(DomainEvent event) {
        publishedEvents.add(event);
        log.info("Published domain event: {}", event);
    }

    public List<DomainEvent> getPublishedEvents() {
        return List.copyOf(publishedEvents);
    }
}
