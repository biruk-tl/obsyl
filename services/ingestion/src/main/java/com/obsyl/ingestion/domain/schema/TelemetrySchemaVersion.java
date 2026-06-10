package com.obsyl.ingestion.domain.schema;

/**
 * Canonical schema version identifiers for Obsyl telemetry events.
 * Represents the platform's event evolution strategy.
 */
public final class TelemetrySchemaVersion {

    public static final String V1 = "v1";
    public static final String V2 = "v2";

    private TelemetrySchemaVersion() {
    }
}
