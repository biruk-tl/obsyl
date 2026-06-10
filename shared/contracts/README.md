# Contracts

This directory contains canonical event contracts used across Obsyl.

Contracts define **data formats**, not implementation logic. They answer: *what does a log, metric, or trace look like?* — not *how do we store or process it?*

## Examples (planned)

| Contract     | Description                          |
| ------------ | ------------------------------------ |
| `LogEvent`   | Structured log entry                 |
| `MetricEvent`| Time-series measurement              |
| `TraceEvent` | Distributed trace span               |

Add contracts here as they are needed by more than one service.
