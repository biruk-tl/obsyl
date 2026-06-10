package com.obsyl.ingestion.infrastructure.event;

import com.obsyl.ingestion.application.event.EventPublisher;
import com.obsyl.ingestion.domain.event.DomainEvent;
import com.obsyl.ingestion.infrastructure.buffer.EventBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Buffers domain events and dispatches them in batches to a downstream publisher.
 * Simulates high-throughput ingestion pipelines until Kafka is introduced.
 */
public class BatchedEventPublisher implements EventPublisher {

    private static final Logger log = LoggerFactory.getLogger(BatchedEventPublisher.class);
    private static final int BATCH_SIZE = 50;

    private final EventBuffer eventBuffer;
    private final EventPublisher delegatePublisher;

    public BatchedEventPublisher(EventBuffer eventBuffer, EventPublisher delegatePublisher) {
        this.eventBuffer = eventBuffer;
        this.delegatePublisher = delegatePublisher;
    }

    @Override
    public void publish(DomainEvent event) {
        eventBuffer.add(event);
        flushBatch();
    }

    @Scheduled(fixedDelay = 2000)
    public void flushRemainingEvents() {
        flushBatch();
    }

    void flushBatch() {
        List<DomainEvent> batch = eventBuffer.drain(BATCH_SIZE);
        if (batch.isEmpty()) {
            return;
        }

        log.info("Publishing buffered batch of {} events", batch.size());
        for (DomainEvent event : batch) {
            delegatePublisher.publish(event);
        }
    }
}
