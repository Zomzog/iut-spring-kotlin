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

Pour être "production ready" une application doit être en mesure de fournir :

- un health-check,
- des metrics,
- des logs.

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

Ajouter la dépendance Spring fournit directement plusieurs endpoints sous `/actuator`.

|endpoints | description|
|--|--|
|/actuator/health |santé de l'application|
|/actuator/info |information général sur l'application|
|/actuator/metrics |métriques de l'application|
|/actuator/beans |liste des beans et de leur dépendance|
|/actuator/... |[rtfm](https://docs.spring.io/spring-boot/reference/actuator/endpoints.html)|

</div>

---
layout: full
class: text-left
---

## Configuration des actuators

```yaml
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

```yaml [all endpoints do not use in production]
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

---
layout: full
class: text-left
---

## Metrics

:: code-group

```kotlin [gradle]

  runtimeOnly("io.micrometer:micrometer-registry-prometheus")
```

```xml [maven]
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <scope>runtime</scope>
</dependency>
```

::

---
layout: full
class: text-left
---

## /actuator/prometheus

```txt
http_server_requests_seconds_count

{method="GET",outcome="SUCCESS",status="200",uri="/actuator/health"}

117992
```

---
layout: full
class: text-left
---

## Custom metrics

```kotlin
@Component
class MetricsConfig(meterRegistry: MeterRegistry) {
    private val myCount = meterRegistry.counter("name.my.count",
                                                "aDimension", "theValue")

    override fun theIncrement() {
        myCount.increment()
    }
```

---
layout: full
class: text-left
---

## Traces

:: code-group

```kotlin [gradle]

  runtimeOnly("io.micrometer:micrometer-tracing-bridge-brave")
```

```xml [maven]
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>
```

::

---
layout: full
class: text-left
---

## TraceIds

```bash
INFO [678c233e1008f59fd685db096863b99c-17138239dbfafafa] bzh.zomzog.sandbox.Controller : GET Request

INFO [678c2513bc84da3276ad1dcc2d898ba3-9b5479c78326e029] bzh.zomzog.sandbox.Cron       : Run cron

INFO [678c233e1008f59fd685db096863b99c-17138239dbfafafa] bzh.zomzog.sandbox.Service    : Handle Request
```

<br/>

<div v-click>

## Traces & Span

<img src="/traces-spans.png" class="h-70 object-scale-down" ></img>

</div>
