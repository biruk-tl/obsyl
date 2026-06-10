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
    void ingestBuildsTelemetryEventFromValidCommand() {
        var command = new IngestLogCommand(
                "obsyl-ingestion",
                "info",
                "service started",
                null,
                null
        );

        var event = service.ingest(command);

        assertNotNull(event.getEventId());
        assertEquals("obsyl-ingestion", event.getService());
        assertEquals("unknown", event.getEnvironment());
        assertEquals("v1", event.getSchemaVersion());
        assertEquals(EventType.LOG, event.getEventType());
        assertEquals("INFO", event.getLogEvent().getLevel());
        assertEquals("service started", event.getLogEvent().getMessage());
        assertNotNull(event.getTimestamp());
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

        var event = service.ingest(command);

        assertEquals("2026-06-10T14:32:01.123Z", event.getTimestamp().toString());
        assertEquals("staging", event.getEnvironment());
    }

    @Test
    void ingestRejectsMissingRequiredFields() {
        var command = new IngestLogCommand("obsyl-ingestion", null, "service started", null, null);

        var exception = assertThrows(InvalidLogRequestException.class, () -> service.ingest(command));

        assertTrue(exception.getMessage().contains("level"));
    }
}
