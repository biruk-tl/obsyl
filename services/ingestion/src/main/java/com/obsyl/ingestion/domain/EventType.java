package com.obsyl.ingestion.domain;

/**
 * Discriminator for telemetry event payloads defined by the Obsyl contract.
 */
public enum EventType {
    LOG,
    METRIC,
    TRACE
}
