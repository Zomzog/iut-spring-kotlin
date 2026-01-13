Demos and tests for c4 slides

Prerequisites:
- Java 21
- Docker (for Testcontainers and services)

Run a specific demo (example: observability):

```bash
./gradlew :bootRun --args='--spring.config.location=classpath:/application-observability.yml'
```

Run tests (requires Docker for integration tests):

```bash
./gradlew clean test
```
