package com.obsyl.ingestion.infrastructure.event;

import com.obsyl.ingestion.domain.EventType;
import com.obsyl.ingestion.domain.TelemetryEvent;
import com.obsyl.ingestion.domain.TelemetryEventEnvelope;
import com.obsyl.ingestion.domain.event.TelemetryIngestedEvent;
import com.obsyl.ingestion.domain.schema.TelemetrySchemaVersion;
import com.obsyl.ingestion.infrastructure.buffer.InMemoryEventBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class BatchedEventPublisherTest {

    private InMemoryEventPublisher delegatePublisher;
    private BatchedEventPublisher batchedEventPublisher;

    @BeforeEach
    void setUp() {
        delegatePublisher = new InMemoryEventPublisher();
        batchedEventPublisher = new BatchedEventPublisher(new InMemoryEventBuffer(), delegatePublisher);
    }

    @Test
    void publishBuffersAndDispatchesEventInBatch() {
        var envelope = sampleEnvelope();
        var ingestedEvent = TelemetryIngestedEvent.from(envelope);

        batchedEventPublisher.publish(ingestedEvent);

        assertEquals(1, delegatePublisher.getPublishedEvents().size());
        assertInstanceOf(TelemetryIngestedEvent.class, delegatePublisher.getPublishedEvents().get(0));
    }

    private TelemetryEventEnvelope sampleEnvelope() {
        var telemetryEvent = new TelemetryEvent(
                "event-123",
                Instant.parse("2026-06-10T14:32:01.123Z"),
                "obsyl-ingestion",
                "unknown",
                TelemetrySchemaVersion.V1,
                EventType.LOG,
                Map.of(),
                new com.obsyl.ingestion.domain.LogEvent("INFO", "service started"),
                null,
                null
        );
        return TelemetryEventEnvelope.wrap(telemetryEvent);
    }
}
