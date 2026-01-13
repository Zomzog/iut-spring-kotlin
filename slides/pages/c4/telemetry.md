---
layout: cover
hideInToc: false
---

# Télémetry — OpenTelemetry (OTel)

---
layout: full
class: text-left
---

## OpenTelemetry

<v-click>

propagation (contexte de trace)

**+**

instrumentation (auto / manuelle)

**+**

export vers un collector (OTLP).

</v-click>

---
layout: full
class: text-left
---

## Concepts clés

<v-clicks>

- Trace : ensemble d'appels distribués.
- Span : opération individuelle (start/end, attributs).
- Resource : `service.name`, `service.version`, hôte, runtime.
- Propagation : W3C TraceContext (`traceparent`, `tracestate`) + en-têtes personnalisés (`X-Request-Id`).

</v-clicks>

<v-click>

## Propagation (W3C + corr-id)

- Priorité : si `traceparent` est présent → l'utiliser. Sinon, fallback sur `X-Request-Id` ou génération d'un UUID.

- Bonne pratique : propager `traceparent` dans les appels HTTP.

</v-click>

---
layout: full
class: text-left
---

## Instrumentation : auto vs manuelle

<v-clicks>

- Auto-instrumentation : starter / agent — couvre clients HTTP, JDBC, Kafka, WebFlux.
- Manuelle : spans autour d'opérations métiers, ajouter des attributs (userId, orderId).

</v-clicks>

---
layout: full
class: text-left
---

## Exemple

```kotlin
@RestController
class TelemetryController(private val demo: SpanDemoService) {
  @GetMapping("/demo")
  fun demo() = demo.process()
}
```

<v-click>

````md magic-move
```kotlin
@Service
class SpanDemoService(private val tracer: io.opentelemetry.api.trace.Tracer) {
  fun process(): String {
    return try {
      "done"
    } finally {
    }
  }
}
```
```kotlin
@Service
class SpanDemoService(private val tracer: io.opentelemetry.api.trace.Tracer) {
  fun process(): String {
    val span = tracer.spanBuilder("process-business").startSpan()
    return try {
      span.setAttribute("demo.attr", "ok")
      "done"
    } finally {
      span.end()
    }
  }
}
```
````

</v-click>

---
layout: full
class: text-left
---

## Collector & exporters

<v-clicks>

- OTLP = protocole unifié → envoyer vers `otel-collector`.
- Collector : reçoit, transforme et relaie vers Jaeger/Tempo/Elastic/Prometheus.

</v-clicks>

---
layout: full
class: text-left
---

## Intégration Spring Boot

Par starter

```kotlin [gradle]
dependencies {
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")
}
```

<v-click>

```yaml
spring:
  application.name: iut-spring-kotlin
otel:
  sdk.disabled: false
  exporter:
    otlp:
      endpoint: http://collector:4317
```

</v-click>

---
layout: full
class: text-left
---

## Intégration Spring Boot

Par agent

java -javaagent:path/to/opentelemetry-javaagent.jar -Dotel.service.name=your-service-name -jar myapp.jar

`javaagent` (opentelemetry-javaagent) : simple à lancer sans modifier le code ; moins de contrôle fin.

Avec comme variable d'environnement:

`OTEL_EXPORTER_OTLP_ENDPOINT=http://collector:4317`

---
layout: image
image: /flam.avif
backgroundSize: contain
---
