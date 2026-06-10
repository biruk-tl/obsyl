package com.obsyl.ingestion.application;

import com.obsyl.ingestion.api.dto.LogRequest;
import com.obsyl.ingestion.domain.EventType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogIngestionServiceTest {

    private final LogIngestionService service = new LogIngestionService();

    @Test
    void ingestBuildsTelemetryEventFromValidRequest() {
        var request = new LogRequest(
                "service started",
                "info",
                "obsyl-ingestion",
                null,
                null,
                "trace-abc"
        );

        var event = service.ingest(request);

        assertNotNull(event.getEventId());
        assertEquals("obsyl-ingestion", event.getService());
        assertEquals("unknown", event.getEnvironment());
        assertEquals("v1", event.getSchemaVersion());
        assertEquals(EventType.LOG, event.getEventType());
        assertEquals("INFO", event.getLogEvent().getLevel());
        assertEquals("service started", event.getLogEvent().getMessage());
        assertEquals("trace-abc", event.getMetadata().get("traceId"));
        assertNotNull(event.getTimestamp());
    }

    @Test
    void ingestAppliesProvidedTimestampAndEnvironment() {
        var request = new LogRequest(
                "deployment complete",
                "WARN",
                "obsyl-ingestion",
                "2026-06-10T14:32:01.123Z",
                "staging",
                null
        );

        var event = service.ingest(request);

        assertEquals("2026-06-10T14:32:01.123Z", event.getTimestamp().toString());
        assertEquals("staging", event.getEnvironment());
    }

    @Test
    void ingestRejectsMissingRequiredFields() {
        var request = new LogRequest("service started", null, "obsyl-ingestion", null, null, null);

        var exception = assertThrows(InvalidLogRequestException.class, () -> service.ingest(request));

        assertTrue(exception.getMessage().contains("level"));
    }
}
