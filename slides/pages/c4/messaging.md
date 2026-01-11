---
layout: full
class: text-left
---

# Messaging (Kafka, RabbitMQ...)

---
layout: full
class: text-left
---

## Qu'est-ce que le "messaging" ?

<v-click>

- Le messaging est une alternative asynchrone aux APIs REST : on échange des messages via un broker (Kafka, RabbitMQ...).

- Avantages : découplage, résilience, montée en charge asynchrone.

</v-click>

---
layout: full
class: text-left
---

## Configuration minimale (application.properties)

<v-click>

```yaml
# broker (Testcontainers ou votre cluster)
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

<!-- Speaker: Conf minimale pour connecter producer/consumer -->

</v-click>

---
layout: full
class: text-left
---

## Exemple : Producteur & Consumer

<v-click>

```kotlin [gradle]
// build.gradle.kts
dependencies {
  implementation("org.springframework.kafka:spring-kafka")
}
```

<!-- Speaker: Dépendance Spring Kafka -->

</v-click>

<v-click>

```kotlin
class Producer(private val kafka: KafkaTemplate<String, String>) {
  fun send(msg: String) = kafka.send("demo-topic", msg)
}
```

<!-- Speaker: Producteur simple utilisant `KafkaTemplate` -->

</v-click>

<v-click>

```kotlin
// Consumer.kt
@Component
class Consumer {
  @KafkaListener(topics = ["demo-topic"], groupId = "demo-group")
  fun listen(record: String) {
    // Traitement idempotent ici
    println("Reçu: $record")
  }
}
```

<!-- Speaker: Consumer basique utilisant `@KafkaListener` -->

</v-click>

---
layout: full
class: text-left
---

## Handler d'erreur (DLQ)

<v-click>

```kotlin
// KafkaErrorConfig.kt
@Configuration
class KafkaErrorConfig {
  @Bean
  fun recoverer(template: KafkaTemplate<String, String>) = DeadLetterPublishingRecoverer(template)

  @Bean
  fun errorHandler(recoverer: DeadLetterPublishingRecoverer): DefaultErrorHandler {
    val backoff = FixedBackOff(1000L, 2) // 2 retries
    return DefaultErrorHandler(recoverer, backoff)
  }
}
```

<!-- Speaker: En cas d'erreur répétée, on publie dans une DLQ via `DeadLetterPublishingRecoverer` -->

</v-click>

<v-click>

- Producer : envoie des messages sur un topic.
- Consumer : lit les messages et les traite (idempotence, retries, DLQ).

</v-click>

