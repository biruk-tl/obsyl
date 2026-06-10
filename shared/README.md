# Shared

This directory contains artifacts shared across multiple Obsyl services.

## Purpose

- **Event contracts** — canonical data formats used by ingestion, processing, and query services.
- **Schema definitions** (future) — versioned event schemas for streaming pipelines (e.g. Kafka).
- **OpenAPI specifications** (future) — API contracts for client generation and documentation.

## What does not belong here

Business logic, database access, repositories, and service-specific implementations must live in their respective microservices — not in `shared`.

Keep this directory intentionally small. Add artifacts only when two or more services genuinely need them.
