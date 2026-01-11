---
layout: full
class: text-left
---

# Observabilité

- Logs

- Metrics

- Telemetry

---
src: logs.md
---

---
layout: full
class: text-left
---

# Observabilité

- Logs ✅

- Metrics

- Telemetry

---
src: actuators.md
---

---
layout: full
class: text-left
---

# Observabilité

- Logs ✅

- Metrics ✅

- Telemetry

---
layout: full
class: text-left
---

## Ajouter les métriques

<v-click>

```kotlin [gradle]
dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("io.micrometer:micrometer-registry-prometheus")
}
```

</v-click>

<v-click>

- En ajoutant simplement ces dépendances, Spring Boot active automatiquement l'endpoint `/actuator/prometheus` (Cf. `@ConditionalOnClass` et les auto-configurations)

</v-click>

---
layout: full
class: text-left
---

## Ajouter une métrique personnalise

````md magic-move

```kotlin
@RestController
class MetricController {
  @GetMapping("/hello")
  fun hello() = "ok"
}
```
```kotlin
@RestController
class MetricController(private val registry: MeterRegistry) {
  @GetMapping("/hello")
  fun hello() = "ok"
}
```
```kotlin
@RestController
class MetricController(private val registry: MeterRegistry) {
  private val counter = registry.counter("demo.requests")
  @GetMapping("/hello")
  fun hello() = "ok"
}
```
```kotlin
@RestController
class MetricController(private val registry: MeterRegistry) {
  private val counter = registry.counter("demo.requests")
  @GetMapping("/hello")
  fun hello(): String {
    counter.increment()
    return "ok"
  }
}
```
````

<!-- Speaker: Étape 1 — contrôleur minimal -->

<!-- Speaker: Étape 2 — injecter MeterRegistry et déclarer un compteur -->

<!-- Speaker: Étape 3 — incrémenter le compteur à chaque appel -->
