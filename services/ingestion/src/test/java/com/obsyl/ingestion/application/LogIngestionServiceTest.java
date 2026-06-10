package com.obsyl.ingestion.application;

import com.obsyl.ingestion.domain.EventType;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogIngestionServiceTest {

    private final LogIngestionService service = new LogIngestionService();

    @Test
    void ingestBuildsTelemetryEventEnvelopeFromValidCommand() {
        var command = new IngestLogCommand(
                "obsyl-ingestion",
                "info",
                "service started",
                null,
                null
        );

        var envelope = service.ingest(command);

        assertNotNull(envelope.getEventId());
        assertEquals("v1", envelope.getSchemaVersion());
        assertEquals(EventType.LOG, envelope.getEventType());
        assertNotNull(envelope.getTimestamp());
        assertEquals(envelope.getEventId(), envelope.getPayload().getEventId());
        assertEquals("obsyl-ingestion", envelope.getPayload().getService());
        assertEquals("unknown", envelope.getPayload().getEnvironment());
        assertEquals("INFO", envelope.getPayload().getLogEvent().getLevel());
        assertEquals("service started", envelope.getPayload().getLogEvent().getMessage());
    }

    @Test
    void ingestAppliesProvidedTimestampAndEnvironment() {
        var command = new IngestLogCommand(
                "obsyl-ingestion",
                "WARN",
                "deployment complete",
                Instant.parse("2026-06-10T14:32:01.123Z"),
                "staging"
        );

        var envelope = service.ingest(command);

        assertEquals("2026-06-10T14:32:01.123Z", envelope.getTimestamp().toString());
        assertEquals("staging", envelope.getPayload().getEnvironment());
    }

    @Test
    void ingestRejectsMissingRequiredFields() {
        var command = new IngestLogCommand("obsyl-ingestion", null, "service started", null, null);

        var exception = assertThrows(InvalidLogRequestException.class, () -> service.ingest(command));

        assertTrue(exception.getMessage().contains("level"));
    }
}
