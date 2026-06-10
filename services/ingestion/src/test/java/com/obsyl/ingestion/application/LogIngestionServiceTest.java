package com.obsyl.ingestion.application;

import com.obsyl.ingestion.api.dto.LogRequest;
import com.obsyl.ingestion.api.error.InvalidLogRequestException;
import com.obsyl.ingestion.application.validation.LogRequestValidator;
import com.obsyl.ingestion.domain.EventType;
import com.obsyl.ingestion.domain.schema.TelemetrySchemaVersion;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LogIngestionServiceTest {

    private final LogIngestionService service = new LogIngestionService(new LogRequestValidator());

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
    }
}
