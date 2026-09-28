---
layout: cover
hideInToc: false
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
  val context: ApplicationContext =
     AnnotationConfigApplicationContext(MyConfig::class.java)
  val service = context.getBean(AService::class.java)
}
```

```kotlin
@SpringBootApplication
class PocApplication

fun main(args: Array<String>) {
    runApplication<PocApplication>(*args)
}
```
````

---
layout: full
class: text-left
---

# runApplication

<div v-click>

Choix du type d'application (servlet, cli...)
</div>

<div v-click>

Démarre le serveur web si besoin
</div>

<div v-click>

Chargement des configurations (variables d'environnement...)
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
}
```

Cherche les @Component (@Service, @Configuration...) dans les sous packages (ex: bzh.zomzog.iut.amphi.service)

---
layout: full
class: text-left
---

# Scan des sous packages

```kotlin {all|all|all|1|all}{at:'0'}
package bzh.zomzog.iut.amphi

@SpringBootApplication
class PocApplication

fun main(args: Array<String>) {
    runApplication<PocApplication>(*args)
}
```

<div v-click>

```kotlin {all|all|all|9|all}{at:'1'}
package bzh.zomzog.iut.amphi.services

@Service
class AServiceOk(db: Database) {
}
```

</div>

<div v-click>

```kotlin {all|1|all}{at:'3'}
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
package bzh.zomzog.iut.amphi

@SpringBootApplication
class PocApplication

fun main(args: Array<String>) {
    runApplication<PocApplication>(*args)
}
```

<div v-click.at="1">

````md magic-move {at: '2'}
```kotlin
package bzh.zomzog.iut.amphi.config

@Configuration
class MyConfig {
}
```
```kotlin
package bzh.zomzog.iut.amphi.config

@Configuration
@ComponentScan("bzh.zomzog.another")
class MyConfig {
}
```
````

</div>

<div v-click.at="3">

Va scanner aussi le package bzh.zomzog.another et ses sous packages

</div>

---
layout: full
class: text-left
zoom: 0.9
---

# Springboot starters

```kotlin
 implementation("org.springframework.boot:spring-boot-starter-webmvc")
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
        └─spring
          │
          └─  org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

</div>

<div v-click>

Contenu:

```txt
org.springframework.boot.webmvc.autoconfigure.DispatcherServletAutoConfiguration
org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration
org.springframework.boot.webmvc.autoconfigure.WebMvcObservationAutoConfiguration
org.springframework.boot.webmvc.autoconfigure.actuate.endpoint.web.WebMvcHealthEndpointExtensionAutoConfiguration
org.springframework.boot.webmvc.autoconfigure.actuate.web.mappings.WebMvcMappingsAutoConfiguration
org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration
```

</div>

<div v-click>

TOUS les starters spring boot en dépendance sont chargés

</div>

<!--

Un starter tire le module d'auto-configuration de sa techno (ex. `spring-boot-webmvc`),
qui contient ce fichier `.imports`. Au démarrage, Spring lit tous les `.imports` du classpath.
Ici, même structure pour notre propre lib `bzh.zomzog.another` — détaillé en C4.
-->
