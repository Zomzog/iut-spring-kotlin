---
layout: cover
---

# Messaging (Kafka, RabbitMQ...)

---
layout: full
class: text-left
---

## Qu'est-ce que le "messaging" ?

<v-click>

- Le messaging est une alternative asynchrone aux APIs REST : on échange des messages via un broker (<span v-mark.box.red="2">Kafka</span>, RabbitMQ...).

- Avantages : découplage, résilience, montée en charge asynchrone.

</v-click>

<div v-click.at='3'>

```kotlin [gradle]
dependencies {
  implementation("org.springframework.boot:spring-boot-starter-kafka")
}
```

<!-- Speaker: Dépendance Spring Kafka -->

</div>

---
layout: full
class: text-left
---

## Configuration minimale (application.properties)

````md magic-move

```yaml
spring:
  kafka:
```

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092

```
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer

```
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    consumer:
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer

```
```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
    consumer:
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      group-id: demo-group
      auto-offset-reset: earliest

```
````

<!-- Speaker: Conf minimale pour connecter producer/consumer -->

---
layout: full
class: text-left
---

## Exemple : Producteur & Consumer

<v-click>

````md magic-move
```kotlin
@Component
class Producer(private val kafka: KafkaTemplate<String, String>) {
}
```
```kotlin
@Component
class Producer(private val kafka: KafkaTemplate<String, String>) {
  fun send(msg: String) = kafka.send("demo-topic", msg)
}
```
````

</v-click>

<div v-click.at="3">

````md magic-move {at:'3'}
```kotlin
```
```kotlin
@Component
class Consumer {
}
```
```kotlin
@Component
class Consumer {
  fun listen(record: String) {
    // Traitement idempotent ici
    println("Reçu: $record")
  }
}
```
```kotlin
@Component
class Consumer {
  @KafkaListener(topics = ["demo-topic"], groupId = "demo-group")
  fun listen(record: String) {
    // Traitement idempotent ici
    println("Reçu: $record")
  }
}
```
````

</div>

<!-- Speaker: Consumer basique utilisant `@KafkaListener` -->

---
layout: full
class: text-left
---

## Handler d'erreur (DLQ)

<v-click>

````md magic-move
```kotlin
@Configuration
class KafkaErrorConfig {
  @Bean
  fun errorHandler(): DefaultErrorHandler {
    val backoff = FixedBackOff(1000L, 2) // 2 retries 1s
    return DefaultErrorHandler(lbackoff)
  }
}
```
```kotlin
@Configuration
class KafkaErrorConfig {
  @Bean
  fun errorHandler(recoverer: DeadLetterPublishingRecoverer): DefaultErrorHandler {
    val backoff = FixedBackOff(1000L, 2) // 2 retries 1s
    return DefaultErrorHandler(recoverer, backoff)
  }

  @Bean
  fun recoverer(template: KafkaTemplate<String, String>) = DeadLetterPublishingRecoverer(template)

}
```
````

<!-- Speaker: En cas d'erreur répétée, on publie dans une DLQ via `DeadLetterPublishingRecoverer` -->

</v-click>

<v-click>

- Producer : envoie des messages sur un topic.
- Consumer : lit les messages et les traite (idempotence, retries, DLQ).

</v-click>

