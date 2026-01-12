---
layout: cover
---

# Tests d'intégration

---
layout: full
class: text-left
---

## Tests d'intégration

<v-click>

- Éviter les mocks pour valider le comportement réel des dépendances (SQL, Kafka, Redis...)

</v-click>
<v-click>

- Testcontainers fournit des dépendances réelles, isolées et reproductibles (images Docker)

</v-click>
<v-click>

- Utiliser Testcontainers pour JPA/Kafka/Redis en CI ; garder des doubles rapides pour les unitaires

</v-click>

---
layout: full
class: text-left
---

## Exemple : @DataJpaTest + PostgreSQLContainer + @ServiceConnection

<v-click>

````md magic-move
```kotlin
@DataJpaTest
class UserRepositoryServiceConnectionTes {

  @Autowired
  lateinit var userRepository: UserRepository

  @Test
  fun `service connection should provide database and repository works`() {
    userRepository.save(UserEntity(id = 2L, name = "Bob"))

    val found = userRepository.findById(user.id.toString())
    assert(found.isPresent)
  }
}
```
```kotlin
@DataJpaTest
@Testcontainers
class UserRepositoryServiceConnectionTes {

  @Autowired
  lateinit var userRepository: UserRepository

  @Test
  fun `service connection should provide database and repository works`() {
    userRepository.save(UserEntity(id = 2L, name = "Bob"))

    val found = userRepository.findById(user.id.toString())
    assert(found.isPresent)
  }
}
```
```kotlin
@DataJpaTest
@Testcontainers
class UserRepositoryServiceConnectionTes {
  companion object {
    val postgres = PostgreSQLContainer("postgres:18-alpine").apply {
        withDatabaseName("testdb")
        withUsername("test")
        withPassword("test")
    }
  }
  @Autowired
  lateinit var userRepository: UserRepository

  @Test
  fun `service connection should provide database and repository works`() {
    userRepository.save(UserEntity(id = 2L, name = "Bob"))

    val found = userRepository.findById(user.id.toString())
    assert(found.isPresent)
  }
}
```
```kotlin
@DataJpaTest
@Testcontainers
class UserRepositoryServiceConnectionTes {
  companion object {
    @Container
    val postgres = PostgreSQLContainer("postgres:18-alpine").apply {
        withDatabaseName("testdb")
        withUsername("test")
        withPassword("test")
    }
  }
  @Autowired
  lateinit var userRepository: UserRepository

  @Test
  fun `service connection should provide database and repository works`() {
    userRepository.save(UserEntity(id = 2L, name = "Bob"))

    val found = userRepository.findById(user.id.toString())
    assert(found.isPresent)
  }
}
```
```kotlin
@DataJpaTest
@Testcontainers
class UserRepositoryServiceConnectionTes {
  companion object {
    @Container
    @ServiceConnection
    val postgres = PostgreSQLContainer("postgres:18-alpine").apply {
        withDatabaseName("testdb")
        withUsername("test")
        withPassword("test")
    }
  }
  @Autowired
  lateinit var userRepository: UserRepository

  @Test
  fun `service connection should provide database and repository works`() {
    userRepository.save(UserEntity(id = 2L, name = "Bob"))

    val found = userRepository.findById(user.id.toString())
    assert(found.isPresent)
  }
}
```
````

<!-- Speaker: `@ServiceConnection` crée automatiquement des ConnectionDetails que l'auto-configuration Spring Boot utilisera. -->

</v-click>

---
layout: full
class: text-left
---

## Alternative : utiliser le JDBC URL Testcontainers (JDBC module)

<v-click>

```yaml {3|4-5|all}
spring:
  datasource:
    url: jdbc:tc:postgresql:18-alpine:///testdb
    username: test
    password: test
```

</v-click>

<!-- Speaker: `jdbc:tc:` permet de démarrer et d'utiliser la base via le driver Testcontainers JDBC sans gérer explicitement le container. -->

---
layout: full
class: text-left
---

## Exemple minimal : Kafka + Testcontainers

```kotlin
@Testcontainers
@SpringBootTest
class KafkaIT {
  companion object {
    @Container
    @ServiceConnection
    val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))
  }

  @Test
  fun `produce and consume`() {
    // The test
  }
}
```

<!-- Speaker: KafkaContainer expose `bootstrapServers` — injectez-le dans votre client de test. -->
