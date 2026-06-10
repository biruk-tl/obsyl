package com.obsyl.ingestion.domain;

import java.util.Objects;

/**
 * Trace-specific payload for a {@link TelemetryEvent}.
 * Framework-free value object aligned with telemetry-event-v1.
 */
public final class TraceEvent {

    private final String traceId;
    private final String spanId;
    private final String parentSpanId;
    private final String operationName;
    private final long durationMs;

    public TraceEvent(
            String traceId,
            String spanId,
            String parentSpanId,
            String operationName,
            long durationMs
    ) {
        this.traceId = Objects.requireNonNull(traceId, "traceId must not be null");
        this.spanId = Objects.requireNonNull(spanId, "spanId must not be null");
        this.parentSpanId = parentSpanId;
        this.operationName = Objects.requireNonNull(operationName, "operationName must not be null");
        this.durationMs = durationMs;
    }

    public String getTraceId() {
        return traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public String getParentSpanId() {
        return parentSpanId;
    }

    public String getOperationName() {
        return operationName;
    }

    public long getDurationMs() {
        return durationMs;
    }
}
