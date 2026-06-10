# Infrastructure

This directory contains operational and deployment infrastructure for Obsyl.

It answers: **how does the system run?** — not how business logic works.

## Contents

| Path       | Purpose                                              |
| ---------- | ---------------------------------------------------- |
| `docker/`  | Dockerfiles for services and UI                      |
| `compose/` | Docker Compose configurations for local development  |
| `env/`     | Environment-specific configuration templates         |

## Future additions

- **Kubernetes** manifests for container orchestration
- **Terraform** modules for cloud infrastructure

Application business logic must never live here.
