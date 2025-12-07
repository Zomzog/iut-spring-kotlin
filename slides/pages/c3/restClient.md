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

<v-clik>RestTemplate (2009)</v-clik>

<v-clik>WebClient (2017)</v-clik>

<v-clik>RestClient (2023)</v-clik>

<!--
Spring propose 3 clients HTTP

Ce sont des abstractions qui simplifient l'utilisation.

Un peut comme JPA simplifie l'utilisation de JDBC
-->

---
layout: full
class: text-left
---

## RestTemplate

Client historique pour API synchrone

Très bien intégré à l'echo-système

---
layout: full
class: text-left
---

## RestTemplate Example

```kotlin
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

```kotlin
val restTemplate = RestTemplate()
```

[CAUTION.is-vcentered]
.Mauvaise pratique
====

pas de bean = pas de traces

appel direct du constructeur = pas de metrics
====

---
layout: full
class: text-left
---

## RestTemplate Creation

```kotlin
@Configuration
class RestTemplateConfig {
  @Bean
  fun restTemplate(builder: RestTemplateBuilder) =
     builder.build()
}
```

---
layout: full
class: text-left
---

## RestTemplate Error handling

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
      if (httpResponse.statusCode =---
layout: full
class: text-left
---

## HttpStatus.NOT_FOUND)
        throw NotFoundException()
    }
  }
}
```

---
layout: full
class: text-left
---

## RestTemplate Error handling

```kotlin
@Configuration
class RestTemplateConfig {
  @Bean
  fun restTemplate(builder: RestTemplateBuilder) =
     builder.errorHandler(MyResponseErrorHandler())
            .build()
}
```

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

<!--
Dessin Req -> BDD -> ApiExterne -> Response
1 thread vs reactive
-->

=---
layout: full
class: text-left
---

## Hide

image:reactive.png[]

<!--
RestTemplate ne répond pas aux besoins de la stack reactive.

Spring a donc dû refaire un client,
et au passage en a profité pour moderniser l'API.
-->

---
layout: full
class: text-left
---

## WebClient Creation

```kotlin
@Configuration
class WebClientConfig {
  @Bean
  fun webCleint(builder: WebClient.Builder) =
     builder.baseUrl("http://localhost:8080")
            .build()
}
```

---
layout: full
class: text-left
---

## WebClient Example

```kotlin
val result: Mono<PonyDto> = client.post()
  .uri("/api/v1/ponies")
  .bodyValue(pony)
  .retrieve()
  .bodyToMono(PonyDto::class.java)
```

---
layout: full
class: text-left
---

## WebClient Example - MVC

```kotlin
val result: Mono<PonyDto> = client.post()
  .uri("/api/v1/ponies")
  .bodyValue(pony)
  .retrieve()
  .bodyToMono(PonyDto::class.java)
  .block()
```

---
layout: full
class: text-left
---

## WebClient Error handling

```kotlin
val result: PonyDto = client.post()
  .uri("/api/v1/ponies")
  .bodyValue(pony)
  .retrieve()
  .onStatus({ it.isError }, { Mono.error(RuntimeException("Error")) })
  .onStatus({ it.value() ---
layout: full
class: text-left
---

## 404 }, { Mono.error(NotFoundException()) })
  .bodyToMono(PonyDto::class.java)
  .block()
```

---
layout: full
class: text-left
---

## WebClient Retry

```kotlin
val result: Mono<PonyDto> = client.post()
  .uri("/api/v1/ponies")
  .bodyValue(pony)
  .retrieve()
  .bodyToMono(PonyDto::class.java)
  .retryWhen(Retry.backoff(3, Duration.ofSeconds(1))
    .filter {
      when (it) {
        is ResponseStatusException -> it.statusCode.is5xxServerError
        else -> false
      }
    })
```

---
layout: full
class: text-left
---

## WebClient Timeout

```kotlin
val result: Mono<PonyDto> = client.post()
  .uri("/api/v1/ponies")
  .bodyValue(pony)
  .retrieve()
  .bodyToMono(PonyDto::class.java)
  .timeout(Duration.ofSeconds(5))
```

---
layout: full
class: text-left
---

## RestClient

```kotlin
val client = RestClient.builder()
  .baseUrl("http://localhost:8080")
  .build()
val result = client.post()
  .body(pony)
  .retrieve()
  .body<ponyDto>()
```

---
layout: full
class: text-left
---

## RestClient Error handling

```kotlin
val client = RestClient.builder()
  .baseUrl("http://localhost:8080")
  .build()
val result = client.post()
  .uri("/api/v1/ponies")
  .body(pony)
  .retrieve()
  .onStatus({ it.isError },
          { req, res -> throw MyException("${req.uri} -> ${res.statusCode}") })
  .body<ponyDto>()
```

---
layout: full
class: text-left
---

## Http Interface

[source, kotlin]

```
interface MyService {
    @GetExchange("/api/v1/ponies")
    fun findAll(): List<PonyDto>
}
```

---
layout: full
class: text-left
---

## Interface -> Bean

[source, kotlin]

```
@Bean
fun myBean(): MyService {
  val adapter = RestClientAdapter.create(client)
  val factory = HttpServiceProxyFactory.builderFor(adapter).build()
  return factory.createClient(MyService::class.java)
}
```

---
layout: full
class: text-left
---

## Http Interface

[fragment, step=0]

```kotlin
@GetExchange("/hello")
fun queryParam(@RequestParam name: String): Something
```

[fragment, step=1]

```kotlin
@GetExchange("/hello/{name}")
fun path(@PathVariable name: String): Something
```

[fragment, step=2]

```kotlin
@PostExchange("/hello")
fun body(@RequestBody name: String): Something
```

[fragment, step=3]

```kotlin
@GetExchange("/hello")
fun header(@RequestHeader name: String): Something
```

---
layout: full
class: text-left
---

## Http Interface

[fragment, step=0]

```kotlin
@GetExchange("/hello")
fun findAll(): List<Pony>
```

[fragment, step=1]

```kotlin
@GetExchange("/hello")
fun findAll(): ResponseEntity<List<Pony>>
```

[fragment, step=2]
[WARNING]
====
En cas d'erreur (4xx, 5xx) l'appel ne renvoi pas une ResponseEntity mais une erreur
====

[%notitle]
---
layout: full
class: text-left
---

## HttpVersus

image:httpVs.png[]

<!--
La gestion d'erreur est au niveau du restTemplate,
si on ne veut pas traiter les 404 pareil pour deux endpoint il faut plusieurs restTemplate
-->
