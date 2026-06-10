package com.obsyl.ingestion.application;

import com.obsyl.ingestion.domain.TelemetryEvent;

/**
 * Defines how Obsyl handles telemetry schema evolution across service versions.
 *
 * <p>Schema evolution is required because observability platforms outlive individual
 * producers and consumers. Without explicit version transformation, breaking field
 * changes would corrupt in-flight events and fragment storage indexes.
 *
 * <p>Breaking changes are avoided by:
 * <ul>
 *   <li>Defaulting all ingestion to a stable internal format (V1)</li>
 *   <li>Introducing additive changes within a version where possible</li>
 *   <li>Routing version-specific transformations through dedicated methods</li>
 *   <li>Keeping downstream pipelines dual-read capable during migration windows</li>
 * </ul>
 */
public class TelemetrySchemaEvolutionStrategy {

    /**
     * Upgrades a V1 event to V2 format for downstream systems that require the newer schema.
     * Not yet implemented — placeholder for future evolution.
     */
    public TelemetryEvent transformV1ToV2(TelemetryEvent event) {
        throw new UnsupportedOperationException("V2 schema transformation is not yet implemented");
    }

    /**
     * Downgrades a V2 event to V1 format for internal normalization and legacy consumers.
     * Not yet implemented — placeholder for future evolution.
     */
    public TelemetryEvent transformV2ToV1(TelemetryEvent event) {
        throw new UnsupportedOperationException("V2 schema transformation is not yet implemented");
    }
}
