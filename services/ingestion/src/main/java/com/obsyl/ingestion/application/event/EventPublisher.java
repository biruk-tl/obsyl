package com.obsyl.ingestion.application.event;

import com.obsyl.ingestion.domain.event.DomainEvent;

/**
 * Abstraction for publishing internal domain events.
 * Implementations may use in-memory delivery today and Kafka or other brokers later.
 */
public interface EventPublisher {

    void publish(DomainEvent event);
}
