package com.obsyl.ingestion.domain;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

/**
 * Root domain aggregate representing any observability event in Obsyl.
 * Framework-free by design — the canonical internal truth for telemetry data.
 */
public final class TelemetryEvent {

    private final String eventId;
    private final Instant timestamp;
    private final String service;
    private final String environment;
    private final String schemaVersion;
    private final EventType eventType;
    private final Map<String, Object> metadata;
    private final LogEvent logEvent;
    private final MetricEvent metricEvent;
    private final TraceEvent traceEvent;

    public TelemetryEvent(
            String eventId,
            Instant timestamp,
            String service,
            String environment,
            String schemaVersion,
            EventType eventType,
            Map<String, Object> metadata,
            LogEvent logEvent,
            MetricEvent metricEvent,
            TraceEvent traceEvent
    ) {
        this.eventId = Objects.requireNonNull(eventId, "eventId must not be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null");
        this.service = Objects.requireNonNull(service, "service must not be null");
        this.environment = Objects.requireNonNull(environment, "environment must not be null");
        this.schemaVersion = Objects.requireNonNull(schemaVersion, "schemaVersion must not be null");
        this.eventType = Objects.requireNonNull(eventType, "eventType must not be null");
        this.metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        this.logEvent = logEvent;
        this.metricEvent = metricEvent;
        this.traceEvent = traceEvent;
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getService() {
        return service;
    }

    public String getEnvironment() {
        return environment;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public LogEvent getLogEvent() {
        return logEvent;
    }

    public MetricEvent getMetricEvent() {
        return metricEvent;
    }

    public TraceEvent getTraceEvent() {
        return traceEvent;
    }
}
