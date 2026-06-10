package com.obsyl.ingestion.application;

import com.obsyl.ingestion.api.dto.LogRequest;
import com.obsyl.ingestion.api.error.InvalidLogRequestException;
import com.obsyl.ingestion.application.validation.LogRequestValidator;
import com.obsyl.ingestion.domain.EventType;
import com.obsyl.ingestion.domain.event.TelemetryIngestedEvent;
import com.obsyl.ingestion.domain.schema.TelemetrySchemaVersion;
import com.obsyl.ingestion.infrastructure.buffer.InMemoryEventBuffer;
import com.obsyl.ingestion.infrastructure.event.BatchedEventPublisher;
import com.obsyl.ingestion.infrastructure.event.InMemoryEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogIngestionServiceTest {

    private InMemoryEventPublisher delegatePublisher;
    private LogIngestionService service;

    @BeforeEach
    void setUp() {
        delegatePublisher = new InMemoryEventPublisher();
        var batchedPublisher = new BatchedEventPublisher(new InMemoryEventBuffer(), delegatePublisher);
        service = new LogIngestionService(new LogRequestValidator(), batchedPublisher);
    }

    @Test
    void ingestBuildsTelemetryEventEnvelopeFromValidRequest() {
        var request = new LogRequest("obsyl-ingestion", "info", "service started", null, null, null);

        var envelope = service.ingest(request);

        assertNotNull(envelope.getEventId());
        assertEquals(TelemetrySchemaVersion.V1, envelope.getSchemaVersion());
        assertEquals(EventType.LOG, envelope.getEventType());
        assertNotNull(envelope.getTimestamp());
        assertEquals(envelope.getEventId(), envelope.getPayload().getEventId());
        assertEquals("obsyl-ingestion", envelope.getPayload().getService());
        assertEquals("unknown", envelope.getPayload().getEnvironment());
        assertEquals("INFO", envelope.getPayload().getLogEvent().getLevel());
        assertEquals("service started", envelope.getPayload().getLogEvent().getMessage());
    }

    @Test
    void ingestPublishesTelemetryIngestedEventThroughBufferedPipeline() {
        var request = new LogRequest("obsyl-ingestion", "INFO", "service started", null, null, null);

        var envelope = service.ingest(request);

        assertEquals(1, delegatePublisher.getPublishedEvents().size());
        var publishedEvent = delegatePublisher.getPublishedEvents().get(0);
        assertInstanceOf(TelemetryIngestedEvent.class, publishedEvent);

        var ingestedEvent = (TelemetryIngestedEvent) publishedEvent;
        assertEquals(envelope.getEventId(), ingestedEvent.getEventId());
        assertEquals(EventType.LOG, ingestedEvent.getEventType());
        assertEquals(TelemetrySchemaVersion.V1, ingestedEvent.getSchemaVersion());
        assertEquals(envelope, ingestedEvent.getPayload());
    }

    @Test
    void ingestDefaultsToV1WhenSchemaVersionMissing() {
        var request = new LogRequest("obsyl-ingestion", "INFO", "service started", null, null, null);

        var envelope = service.ingest(request);

        assertEquals(TelemetrySchemaVersion.V1, envelope.getPayload().getSchemaVersion());
    }

    @Test
    void ingestNormalizesExplicitV1SchemaVersion() {
        var request = new LogRequest("obsyl-ingestion", "INFO", "service started", null, null, "v1");

        var envelope = service.ingest(request);

        assertEquals(TelemetrySchemaVersion.V1, envelope.getPayload().getSchemaVersion());
    }

    @Test
    void ingestAppliesProvidedTimestampAndEnvironment() {
        var request = new LogRequest(
                "obsyl-ingestion",
                "WARN",
                "deployment complete",
                Instant.parse("2026-06-10T14:32:01.123Z"),
                "staging",
                null
        );

        var envelope = service.ingest(request);

        assertEquals("2026-06-10T14:32:01.123Z", envelope.getTimestamp().toString());
        assertEquals("staging", envelope.getPayload().getEnvironment());
    }

    @Test
    void ingestRejectsMissingRequiredFields() {
        var request = new LogRequest("obsyl-ingestion", null, "service started", null, null, null);

        var exception = assertThrows(InvalidLogRequestException.class, () -> service.ingest(request));

        assertTrue(exception.getMessage().contains("level"));
        assertEquals(0, delegatePublisher.getPublishedEvents().size());
    }
}
