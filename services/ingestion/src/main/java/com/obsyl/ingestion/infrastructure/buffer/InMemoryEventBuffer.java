package com.obsyl.ingestion.infrastructure.buffer;

import com.obsyl.ingestion.domain.event.DomainEvent;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Thread-safe in-memory event buffer with blocking backpressure.
 *
 * <p>Backpressure is critical in ingestion systems. Without it, bursty producers can
 * overwhelm downstream consumers, exhaust heap memory, and trigger cascading failures
 * across storage and stream-processing tiers. In Kafka-based pipelines, analogous
 * protection appears as producer {@code max.block.ms} limits, consumer lag alerts,
 * and ingestion gateways that shed or throttle load when brokers cannot keep pace.
 *
 * <p>This implementation blocks producers when the buffer is full rather than dropping
 * events. Blocking preserves data integrity at the cost of increased request latency
 * under sustained overload — a common tradeoff for observability platforms where
 * silent data loss is unacceptable.
 */
@Component
public class InMemoryEventBuffer implements EventBuffer {

    public static final int MAX_BUFFER_SIZE = 1000;

    private final ConcurrentLinkedQueue<DomainEvent> events = new ConcurrentLinkedQueue<>();
    private final Object backpressureLock = new Object();

    @Override
    public void add(DomainEvent event) {
        synchronized (backpressureLock) {
            while (size() >= MAX_BUFFER_SIZE) {
                try {
                    backpressureLock.wait(100);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Interrupted while waiting for buffer capacity", interrupted);
                }
            }
            events.offer(event);
            backpressureLock.notifyAll();
        }
    }

    @Override
    public List<DomainEvent> drain(int batchSize) {
        List<DomainEvent> batch = new ArrayList<>(batchSize);
        synchronized (backpressureLock) {
            for (int i = 0; i < batchSize; i++) {
                DomainEvent event = events.poll();
                if (event == null) {
                    break;
                }
                batch.add(event);
            }
            backpressureLock.notifyAll();
        }
        return batch;
    }

    @Override
    public int size() {
        return events.size();
    }
}
