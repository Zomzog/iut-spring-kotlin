---
layout: cover
---

# SpringBoot

---
layout: full
class: text-left
---

# SpringBoot

```kotlin
@Configuration
class MyConfig {
    @Bean
    fun myDb() = PostgresDb()
}
```

````md magic-move
```
```kotlin
fun main() {
  val context: ApplicationContext #
     AnnotationConfigApplicationContext(MyConfig::class.java)
  val service = context.getBean(AService::class.java)
}
```

```kotlin
@SpringBootApplication
class PocApplication

fun main(args: Array<String>) {
    runApplication<PocApplication>(*args)
```
````

---
layout: full
class: text-left
---

# runApplication

<div v-click>
Choix du type d'application (servlet, cli...)

Démarre le serveur web si besoin
</div>

<div v-click>
Chargemement des configurations (variables d'environement...)
</div>

<div v-click>
Création du ApplicationContext / Scan des beans
</div>

---
layout: full
class: text-left
---

# Scan des sous packages

```kotlin
package bzh.zomzog.iut.amphi

@SpringBootApplication
class PocApplication

fun main(args: Array<String>) {
    runApplication<PocApplication>(*args)
```

Cherche les @Component (@Service, @Configuration...) dans les sous packages (ex: bzh.zomzog.iut.amphi.service)

---
layout: full
class: text-left
---

# Scan des sous packages

```kotlin
package bzh.zomzog.iut.amphi

@SpringBootApplication
class PocApplication

fun main(args: Array<String>) {
    runApplication<PocApplication>(*args)
```

<div v-click>

```kotlin
package bzh.zomzog.iut.amphi.services

@Service
class AServiceOk(db: Database) {
}
```

</div>

<div v-click>

```kotlin
package bzh.zomzog.iut

@Service
class AServiceNotFound(db: Database) {
}
```

</div>

---
layout: full
class: text-left
---

# Extensions du scan

```kotlin
package bzh.zomzog.iut.amphi.config

@Configuration
@ComponentScan("bzh.zomzog.another")
class MyConfig {
}
```

Va scanner aussi le package bzh.zomzog.another et ses sous packages

---
layout: full
class: text-left
---

# Springboot starters

```kotlin
 implementation("org.springframework.boot:spring-boot-starter-web")
```

<div v-click>

```txt
project
│
└─src
  │
  └─main
    │
    └─resources
      │
      └─META-INF
        │
        └─  org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

</div>

<div v-click>

Contenu:

```txt
bzh.zomzog.another.autoconfigure.MyAutoConfiguration
```

</div>

<div v-click>

TOUS les starters spring boot en dépendance sont chargés

</div>
