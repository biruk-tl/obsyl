package com.obsyl.ingestion.domain;

import java.util.Map;
import java.util.Objects;

/**
 * Metric-specific payload for a {@link TelemetryEvent}.
 * Framework-free value object aligned with telemetry-event-v1.
 */
public final class MetricEvent {

    private final String metricName;
    private final double value;
    private final String unit;
    private final Map<String, String> tags;

    public MetricEvent(String metricName, double value, String unit, Map<String, String> tags) {
        this.metricName = Objects.requireNonNull(metricName, "metricName must not be null");
        this.value = value;
        this.unit = Objects.requireNonNull(unit, "unit must not be null");
        this.tags = tags == null ? Map.of() : Map.copyOf(tags);
    }

    public String getMetricName() {
        return metricName;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
