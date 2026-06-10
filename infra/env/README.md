# Environment Configuration

Environment-specific configuration templates for Obsyl deployments.

| Directory | Purpose                              |
| --------- | ------------------------------------ |
| `local/`  | Developer machines                   |
| `staging/`| Pre-production testing               |
| `prod/`   | Production deployment                |

## Secrets

Do not commit secrets, credentials, or production values to source control. Use environment variables, secret managers, or local-only files listed in `.gitignore`.
