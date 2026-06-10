package com.obsyl.ingestion.infrastructure.buffer;

import com.obsyl.ingestion.domain.event.DomainEvent;

import java.util.List;

/**
 * In-memory buffer for domain events awaiting batch dispatch.
 */
public interface EventBuffer {

    void add(DomainEvent event);

    List<DomainEvent> drain(int batchSize);

    int size();
}
