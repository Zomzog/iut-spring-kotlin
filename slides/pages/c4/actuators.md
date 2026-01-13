---
layout: cover
hideInToc: false
---

## Monitoring

---
layout: full
class: text-left
---

## Monitoring

Pour être "production ready", une application doit être en mesure de fournir :

- des logs
- un health‑check
- des métriques

---
layout: full
class: text-left
---

## Actuators Spring

:: code-group

```kotlin [gradle]
 implementation("org.springframework.boot:spring-boot-starter-actuator")
```

```xml [maven]
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

::

<div v-click>

L'ajout de la dépendance Spring fournit directement plusieurs endpoints sous `/actuator`.

|endpoints | description|
|--|--|
|/actuator/health | santé de l'application |
|/actuator/info | information générale sur l'application |
|/actuator/metrics | métriques de l'application |
|/actuator/beans | liste des beans et de leur dépendance |
|/actuator/... | [rtfm](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html) |

</div>

---
layout: full
class: text-left
---

## Configuration des actuators

```yaml [select endpoints]
management:
  endpoints:
    web:
      exposure:
        include: info, health, prometheus
```

<div v-click>

```yaml [all endpoints do not use in production]
management:
  endpoints:
    web:
      exposure:
        include: '*'
```

</div>

<div v-click>

```yaml [show all infos (database, disk...) not just UP]
management:
  endpoint:
    health:
      show-details: always
```

</div>

---
layout: full
class: text-left
---

## Configuration de health

```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "H2",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 252841029632,
        "free": 17691353088,
        "threshold": 10485760,
        "path": "C:\\git\\zomzog\\iut\\.",
        "exists": true
      }
    },
    "ping": {
      "status": "UP"
    }
  }
}
```

---
layout: full
class: text-left
---

## Customisation des actuators

````md magic-move
```kotlin
@Component
@EndpointWebExtension(endpoint = InfoEndpoint::class)
class CustomInfo(val delegate: InfoEndpoint) {
}
```
```kotlin
@Component
@EndpointWebExtension(endpoint = InfoEndpoint::class)
class CustomInfo(val delegate: InfoEndpoint) {

    @ReadOperation
    fun info(): WebEndpointResponse<Map<*, *>> {
        val info = this.delegate.info()
        return WebEndpointResponse(info, 200)
    }
}
```
```kotlin
@Component
@EndpointWebExtension(endpoint = InfoEndpoint::class)
class CustomInfo(val delegate: InfoEndpoint) {

    @ReadOperation
    fun info(): WebEndpointResponse<Map<*, *>> {
        val info = this.delegate.info()
        info["custom.value"] = "pony"
        return WebEndpointResponse(info, 200)
    }
}
```
````

---
layout: full
class: text-left
---

## Metrics

<v-click>

- Supervision avec **Micrometer** + Prometheus

</v-click>
<v-click>

- Exposer les métriques via `/actuator/prometheus`

</v-click>

<v-click>

:: code-group

```kotlin [gradle]
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  runtimeOnly("io.micrometer:micrometer-registry-prometheus")
```

```xml [maven]
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <scope>runtime</scope>
</dependency>
```

::

</v-click>

<v-click>

En ajoutant simplement ces dépendances, Spring Boot active automatiquement l'endpoint `/actuator/prometheus` (cf. `@ConditionalOnClass` et les auto-configurations).

</v-click>
<v-click>

GET /actuator/prometheus

```txt
http_server_requests_seconds_count

{method="GET",outcome="SUCCESS",status="200",uri="/actuator/health"}

117992
```

</v-click>

---
layout: full
class: text-left
---

## Custom metrics

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
```kotlin
@RestController
class MetricController(private val registry: MeterRegistry) {
  private val counter = registry.counter("demo.requests", "aDimension", "theValue")
  @GetMapping("/hello")
  fun hello(): String {
    counter.increment()
    return "ok"
  }
}
```
````
