package com.obsyl.ingestion.domain;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Universal container for telemetry data flowing through Obsyl.
 * Designed for reuse across Kafka publishing, persistence, and stream processing.
 */
public final class TelemetryEventEnvelope {

    private final String eventId;
    private final String schemaVersion;
    private final Instant timestamp;
    private final EventType eventType;
    private final TelemetryEvent payload;
    private final Map<String, Object> metadata;

    public TelemetryEventEnvelope(
            String eventId,
            String schemaVersion,
            Instant timestamp,
            EventType eventType,
            TelemetryEvent payload,
            Map<String, Object> metadata
    ) {
        this.eventId = Objects.requireNonNull(eventId, "eventId must not be null");
        this.schemaVersion = Objects.requireNonNull(schemaVersion, "schemaVersion must not be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null");
        this.eventType = Objects.requireNonNull(eventType, "eventType must not be null");
        this.payload = Objects.requireNonNull(payload, "payload must not be null");
        this.metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }

    public static TelemetryEventEnvelope wrap(TelemetryEvent event) {
        return new TelemetryEventEnvelope(
                event.getEventId(),
                event.getSchemaVersion(),
                event.getTimestamp(),
                event.getEventType(),
                event,
                event.getMetadata()
        );
    }

    public String getEventId() {
        return eventId;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public EventType getEventType() {
        return eventType;
    }

    public TelemetryEvent getPayload() {
        return payload;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
