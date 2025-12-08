---
layout: cover
hideInToc: false
---

# Rest Client

---
layout: full
class: text-left
---

## Http-Client

Client récent JDK 11 (2018)

Remplace `java.net.URLConnection`

---
layout: full
class: text-left
---

## Http-Client

````md magic-move
```kotlin
val client = HttpClient.newBuilder()
  .version(HttpClient.Version.HTTP_2) .build()
```
```kotlin
val client = HttpClient.newBuilder()
  .version(HttpClient.Version.HTTP_2) .build()
val request = HttpRequest.newBuilder(URI("http://localhost:8080/users"))
```
```kotlin
val client = HttpClient.newBuilder()
  .version(HttpClient.Version.HTTP_2) .build()
val request = HttpRequest.newBuilder(URI("http://localhost:8080/users"))
  .version(HttpClient.Version.HTTP_2)
  .header("Content-Type", "application/json")
  .POST(
    BodyPublishers.ofString(
      """
    { "id": 1, "name": "John Doe" }
  """.trimIndent()
  )).build()
```
```kotlin
val client = HttpClient.newBuilder()
  .version(HttpClient.Version.HTTP_2) .build()
val request = HttpRequest.newBuilder(URI("http://localhost:8080/users"))
  .version(HttpClient.Version.HTTP_2)
  .header("Content-Type", "application/json")
  .POST(
    BodyPublishers.ofString(
      """
    { "id": 1, "name": "John Doe" }
  """.trimIndent()
  )).build()
val response: HttpResponse<String> = client
              .send(request, BodyHandlers.ofString())
val responseBody = response.body()
```
````

<!--
Bien que récente, 
cette API reste très bas niveau. 

Elle reste complexe à utiliser.
-->

---
layout: full
class: text-left
---

## Http clients

<div v-click>

### RestTemplate (2009)

Client historique pour API synchrone

Très bien intégré à l'écosystème

</div>

<div v-click>

### WebClient (2017)

Client reactif pour API asynchrone
  
Souvent détourné pour faire du synchrone

</div>

<div v-click>

### RestClient (2023)
  
Client synchrone qui doit remplacer RestTemplate avec une syntaxe proche de WebClient

</div>

<!--
Spring propose 3 clients HTTP

Ce sont des abstractions qui simplifient l'utilisation.

Un peut comme JPA simplifie l'utilisation de JDBC
-->

---
layout: full
class: text-left
---

## RestTemplate Example

```kotlin{all|4|5-6}
val client = RestTemplateBuilder()
                .rootUri("http://localhost:8080")
                .build()
val result = client.postForEntity("/api/v1/ponies",
                                  pony,
                                  PonyDto::class.java)
```

---
layout: full
class: text-left
---

## RestTemplate Creation

````md magic-move
```kotlin
@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplate() = RestTemplate()
}
```

```kotlin
@Configuration
class RestTemplateConfig {
  @Bean
  fun restTemplate(builder: RestTemplateBuilder) =
     builder.build()
}
```
````

**Bean:** RestTemplate est stateless,
il est donc un bon client pour un bean

<div v-click.at="1">

**Builder**: RestTemplateBuilder est un bean pre-configure par Spring

Il permet par exemple d'avoir les instrumetation de monitoring

</div>

---
layout: full
class: text-left
---

## RestTemplate Error handling

````md magic-move
```kotlin
class MyResponseErrorHandler : ResponseErrorHandler {
}
```
```kotlin
class MyResponseErrorHandler : ResponseErrorHandler {
  override fun hasError(httpResponse: ClientHttpResponse): Boolean {
    return httpResponse.statusCode.is5xxServerError ||
        httpResponse.statusCode.is4xxClientError
  }
}
```
```kotlin
class MyResponseErrorHandler : ResponseErrorHandler {
  override fun hasError(httpResponse: ClientHttpResponse): Boolean {
    return httpResponse.statusCode.is5xxServerError ||
        httpResponse.statusCode.is4xxClientError
  }

  override fun handleError(httpResponse: ClientHttpResponse) {
    if (httpResponse.statusCode.is5xxServerError) {
      throw HttpClientErrorException(httpResponse.statusCode)
    }
}
```
```kotlin
class MyResponseErrorHandler : ResponseErrorHandler {
  override fun hasError(httpResponse: ClientHttpResponse): Boolean {
    return httpResponse.statusCode.is5xxServerError ||
        httpResponse.statusCode.is4xxClientError
  }

  override fun handleError(httpResponse: ClientHttpResponse) {
    if (httpResponse.statusCode.is5xxServerError) {
      throw HttpClientErrorException(httpResponse.statusCode)
    } else if (httpResponse.statusCode.is4xxClientError) {
      if (httpResponse.statusCode == HttpStatus.NOT_FOUND)
        throw NotFoundException()
    }
  }
}
```
````

<div v-click.at='4'>

````md magic-move {at:'5'}
```kotlin
@Configuration
class RestTemplateConfig {
  @Bean
  fun restTemplate(builder: RestTemplateBuilder) =
     builder.build()
}
```
```kotlin
@Configuration
class RestTemplateConfig {
  @Bean
  fun restTemplate(builder: RestTemplateBuilder) =
     builder.errorHandler(MyResponseErrorHandler())
            .build()
}
```
````

</div>

---
layout: full
class: text-left
---

## WebClient

  implementation("org.springframework.boot:spring-boot-starter-webflux")

---
layout: full
class: text-left
---

## WebFlux ?

**non-reactive:**

<img src="/sync.png" alt="reactive" class="full-w"/>

<br/>

<div v-click>

**reactive:**

<img src="/async.png" alt="reactive" class="full-w"/>

</div>

<div v-click>

reactive != parallèle

</div>

<!--
Dessin Req -> BDD -> ApiExterne -> Response
1 thread vs reactive

RestTemplate ne répond pas aux besoins de la stack reactive.

Spring a donc dû refaire un client,
et au passage en a profité pour moderniser l'API.
-->

---
layout: full
class: text-left
---

## WebClient

```kotlin
@Configuration
class WebClientConfig {
  @Bean
  fun webCleint(builder: WebClient.Builder) =
     builder.baseUrl("http://localhost:8080")
            .build()
}
```

<v-click>

````md magic-move
```kotlin
val result: Mono<PonyDto> = client.post()
  .uri("/api/v1/ponies")
  .bodyValue(pony)
  .retrieve()
  .bodyToMono(PonyDto::class.java)
```
```kotlin {5}
val result: Mono<PonyDto> = client.post()
  .uri("/api/v1/ponies")
  .bodyValue(pony)
  .retrieve()
  .onStatus({ it.isError }, { Mono.error(RuntimeException("Error")) })
  .bodyToMono(PonyDto::class.java)
```
```kotlin {6}
val result: Mono<PonyDto> = client.post()
  .uri("/api/v1/ponies")
  .bodyValue(pony)
  .retrieve()
  .onStatus({ it.isError }, { Mono.error(RuntimeException("Error")) })
  .onStatus({ it.value() == 404 }, { Mono.error(NotFoundException()) })
  .bodyToMono(PonyDto::class.java)
```
```kotlin {8-14}
val result: Mono<PonyDto> = client.post()
  .uri("/api/v1/ponies")
  .bodyValue(pony)
  .retrieve()
  .onStatus({ it.isError }, { Mono.error(RuntimeException("Error")) })
  .onStatus({ it.value() == 404 }, { Mono.error(NotFoundException()) })
  .bodyToMono(PonyDto::class.java)
  .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
    .filter {
      when (it) {
        is ResponseStatusException -> it.statusCode.is5xxServerError
        else -> false
      }
    })
```
```kotlin {8}
val result: Mono<PonyDto> = client.post()
  .uri("/api/v1/ponies")
  .bodyValue(pony)
  .retrieve()
  .onStatus({ it.isError }, { Mono.error(RuntimeException("Error")) })
  .onStatus({ it.value() == 404 }, { Mono.error(NotFoundException()) })
  .bodyToMono(PonyDto::class.java)
  .timeout(Duration.ofSeconds(5))
  .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
    .filter {
      when (it) {
        is ResponseStatusException -> it.statusCode.is5xxServerError
        else -> false
      }
    })
```
```kotlin {1,16|all}
val result: PonyDto = client.post()
  .uri("/api/v1/ponies")
  .bodyValue(pony)
  .retrieve()
  .onStatus({ it.isError }, { Mono.error(RuntimeException("Error")) })
  .onStatus({ it.value() == 404 }, { Mono.error(NotFoundException()) })
  .bodyToMono(PonyDto::class.java)
  .timeout(Duration.ofSeconds(5))
  .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
    .filter {
      when (it) {
        is ResponseStatusException -> it.statusCode.is5xxServerError
        else -> false
      }
    })
  .block() // stop le reactif
```
````

</v-click>

---
layout: full
class: text-left
---

## RestClient

```kotlin
val client = RestClient.builder()
  .baseUrl("http://localhost:8080")
  .build()
```

<v-click>

```kotlin
val result = client.post()
  .uri("/api/v1/ponies")
  .body(pony)
  .retrieve()
  .onStatus({ it.isError },
          { req, res -> throw MyException("${req.uri} -> ${res.statusCode}") })
  .body<PonyDto>()
```

</v-click>

---
layout: full
class: text-left
---

## Http Interface

```kotlin
interface MyService {
    @GetExchange("/api/v1/ponies")
    fun findAll(): List<PonyDto>
}
```

<v-click>

## Interface -> Bean

```kotlin
@Bean
fun myBean(client: RestClient): MyService {
  val adapter = RestClientAdapter.create(client)
  val factory = HttpServiceProxyFactory.builderFor(adapter).build()
  return factory.createClient(MyService::class.java)
}
```

</v-click>

---
layout: full
class: text-left
---

## Http Interface

<v-click>

````md magic-move
```kotlin
interface MyService {
  @GetExchange("/hello")
  fun queryParam(@RequestParam name: String): Something
}
```
```kotlin
interface MyService {
  @GetExchange("/hello")
  fun queryParam(@RequestParam name: String): Something

  @GetExchange("/hello/{name}")
  fun path(@PathVariable name: String): Something
}
```
```kotlin
interface MyService {
  @GetExchange("/hello")
  fun queryParam(@RequestParam name: String): Something

  @GetExchange("/hello/{name}")
  fun path(@PathVariable name: String): Something

  @PostExchange("/hello")
  fun body(@RequestBody name: String): Something
}
```
```kotlin
interface MyService {
  @GetExchange("/hello")
  fun queryParam(@RequestParam name: String): Something

  @GetExchange("/hello/{name}")
  fun path(@PathVariable name: String): Something

  @PostExchange("/hello")
  fun body(@RequestBody name: String): Something

  @GetExchange("/hello")
  fun header(@RequestHeader name: String): Something
}
```
````

</v-click>
