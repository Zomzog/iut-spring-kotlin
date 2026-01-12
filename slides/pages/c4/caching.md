---
layout: cover
---


# Cache & Performance

---
layout: full
class: text-left
---

## Pourquoi utiliser des caches ?

<v-click>

Réduire la latence pour les opérations coûteuses (requêtes BD, appels distants)

</v-click>
<v-click>

Diminuer la charge sur les systèmes en aval et réduire les coûts

</v-click>
<v-click>

Améliorer l'expérience utilisateur avec des réponses plus rapides

</v-click>

<v-click>

Exemple de backend : **Caffeine** (en mémoire) ou **Redis** (cache distribué)

</v-click>

---
layout: full
class: text-left
---

## Les caches dans Spring

:: code-group

```kotlin [gradle]
dependencies {
 implementation("org.springframework.boot:spring-boot-starter-cache")
 implementation("com.github.ben-manes.caffeine:caffeine")
}
```

```xml [maven]
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<dependency>
    <groupId>com.github.ben-manes.caffeine</groupId>
    <artifactId>caffeine</artifactId>
</dependency>
```

::

<v-click>

Configurer un cache en Spring se fait via un bean `CacheManager`

L'utiliser se fait via l'annotation `@Cacheable`

</v-click>

---
layout: full
class: text-left
---

## Configuration du `CacheManager`

<v-click>

````md magic-move
```kotlin
@Configuration
class MyCaffeineConfig {
}
```
```kotlin
@Configuration
@EnableCaching
class MyCaffeineConfig {
}
```
```kotlin
@Configuration
@EnableCaching
class MyCaffeineConfig {
    @Bean
    fun cacheManager(): CacheManager = CaffeineCacheManager()
}
```
```kotlin
@Configuration
@EnableCaching
class MyCaffeineConfig {
    @Bean
    fun cacheManager(): CacheManager = CaffeineCacheManager()
        .apply {
            setCaffeine(
                Caffeine.newBuilder()
                    .maximumSize(500)
                    .expireAfterWrite(10, MINUTES)
            )
        }
}
```
````

</v-click>

<v-click>

```kotlin
@Cacheable("myCache")
fun randomOnlyOnce(max: Int): Int {
    return Random.nextInt(until = max + 1)
}
```

</v-click>

---
layout: full
class: text-left
---

## Gestion des clés de cache et paramètres

<v-click>

La génération par défaut de la clé utilise les paramètres de la méthode.

Pour contrôler, utilisez `key` :

</v-click>
<v-click>

````md magic-move

```kotlin
@Cacheable("myCache")
fun randomOnlyOnce(max: Int): Int = ..
```
```kotlin
@Cacheable("myCache")
fun randomOnlyOnce(max: Int): Int = ..
@Cacheable("myCache", key = "#max")
fun chacheWithKey(max: Int) = ...
```
```kotlin
@Cacheable("myCache")
fun randomOnlyOnce(max: Int): Int = ..
@Cacheable("myCache", key = "#max")
fun chacheWithKey(max: Int) = ..
@Cacheable("myCache", key = "#root.methodName : #max")
fun chacheWithComplexKey(max: Int) = ..
```
````

</v-click>
<v-click>

ou `keyGenerator` :

````md magic-move {at:'5'}
```kotlin
```
```kotlin
@Bean fun myKeyGenerator(): KeyGenerator = KeyGenerator { target, method, params ->
    method.name + params.joinToString("_") { it.toString() }
}

@Cacheable("myCache", keyGenerator = "myKeyGenerator")
fun withKeyGenerator(max: Int): Int {
    return Random.nextInt(until = max + 1)
}
```
````

</v-click>

<v-click>

Les clés doivent être stables et compactes ; évitez d'y inclure de gros objets.

</v-click>

---
layout: full
class: text-left
---

## Invalidation du cache

<v-click>

> There are only two hard things in Computer Science: cache invalidation and naming things.
>
> — Phil Karlton

</v-click>

<v-click>

Utilisez `@CacheEvict` pour supprimer des entrées lorsque les données changent :

</v-click>
<v-click>

```kotlin
@CacheEvict(value = ["users"], key = "#id")
fun updateUser(id: String, dto: UserDto) { /* mettre à jour la BD puis evict */ }
```

</v-click>
<v-click>

Eviction programmatique via `CacheManager` :

</v-click>
<v-click>

```kotlin
fun invalidate(id: String) {
 cacheManager.getCache("users")?.evict(id)
}
```

</v-click>

<v-click>

Utilisez `@CachePut` pour remplacer des entrées lorsque les données changent :

</v-click>
<v-click>

```kotlin
@CachePut(value = ["users"], key = "#id")
fun updateUser(id: String, dto: UserDto): User { /* mettre à jour la BD puis retourner la valeur  */ }
```

</v-click>

---
layout: full
class: text-left
---

## Gestion des erreurs

````md magic-move


```kotlin
@Configuration
@EnableCaching
class MyCaffeineConfig {
    @Bean
    fun cacheManager(): CacheManager = CaffeineCacheManager()
        .apply {
            setCaffeine(
                Caffeine.newBuilder()
                    .maximumSize(500)
                    .expireAfterWrite(10, MINUTES)
            )
        }
}
```
```kotlin
@Configuration
@EnableCaching
class MyCaffeineConfig {
    @Bean
    fun cacheManager(): CacheManager = CaffeineCacheManager()
        .apply {
            setCaffeine(
                Caffeine.newBuilder()
                    .maximumSize(500)
                    .expireAfterWrite(10, MINUTES)
            )
        }
}

class CacheErrorHandlerConfig : CacheErrorHandler {

    private val logger = KotlinLogging.logger {}

    override fun handleCacheGetError(exception: RuntimeException, cache: Cache, key: Any) {
        logger.warn(exception) { "Unable to get from cache ${cache.name} : $key" }
    }
}
```
```kotlin
@Configuration
@EnableCaching
class MyCaffeineConfig : CachingConfigurer {
    @Bean
    fun cacheManager(): CacheManager = CaffeineCacheManager()
        .apply {
            setCaffeine(
                Caffeine.newBuilder()
                    .maximumSize(500)
                    .expireAfterWrite(10, MINUTES)
            )
        }
}

class CacheErrorHandlerConfig : CacheErrorHandler {

    private val logger = KotlinLogging.logger {}

    override fun handleCacheGetError(exception: RuntimeException, cache: Cache, key: Any) {
        logger.warn(exception) { "Unable to get from cache ${cache.name} : $key" }
    }
}
```
```kotlin
@Configuration
@EnableCaching
class MyCaffeineConfig : CachingConfigurer {
    @Bean
    fun cacheManager(): CacheManager = CaffeineCacheManager()
        .apply {
            setCaffeine(
                Caffeine.newBuilder()
                    .maximumSize(500)
                    .expireAfterWrite(10, MINUTES)
            )
        }

    override fun errorHandler(): CacheErrorHandler {
        return CacheErrorHandlerConfig()
    }
}

class CacheErrorHandlerConfig : CacheErrorHandler {

    private val logger = KotlinLogging.logger {}

    override fun handleCacheGetError(exception: RuntimeException, cache: Cache, key: Any) {
        logger.warn(exception) { "Unable to get from cache ${cache.name} : $key" }
    }
}
```
````
