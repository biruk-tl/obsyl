package com.obsyl.ingestion.domain.event;

import com.obsyl.ingestion.domain.EventType;
import com.obsyl.ingestion.domain.TelemetryEventEnvelope;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event raised when telemetry has been successfully ingested into Obsyl.
 */
public final class TelemetryIngestedEvent implements DomainEvent {

    private final String eventId;
    private final Instant timestamp;
    private final EventType eventType;
    private final String schemaVersion;
    private final TelemetryEventEnvelope payload;

    public TelemetryIngestedEvent(
            String eventId,
            Instant timestamp,
            EventType eventType,
            String schemaVersion,
            TelemetryEventEnvelope payload
    ) {
        this.eventId = Objects.requireNonNull(eventId, "eventId must not be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null");
        this.eventType = Objects.requireNonNull(eventType, "eventType must not be null");
        this.schemaVersion = Objects.requireNonNull(schemaVersion, "schemaVersion must not be null");
        this.payload = Objects.requireNonNull(payload, "payload must not be null");
    }

    public static TelemetryIngestedEvent from(TelemetryEventEnvelope envelope) {
        return new TelemetryIngestedEvent(
                envelope.getEventId(),
                envelope.getTimestamp(),
                envelope.getEventType(),
                envelope.getSchemaVersion(),
                envelope
        );
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public TelemetryEventEnvelope getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return "TelemetryIngestedEvent{"
                + "eventId='" + eventId + '\''
                + ", timestamp=" + timestamp
                + ", eventType=" + eventType
                + ", schemaVersion='" + schemaVersion + '\''
                + '}';
    }
}
