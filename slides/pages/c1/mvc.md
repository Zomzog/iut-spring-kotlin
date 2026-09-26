---
layout: cover
hideInToc: false
---

# Spring MVC

---
layout: full
class: text-left
---

## spring-boot-starter-webmvc

:: code-group

```kotlin [gradle]

 implementation("org.springframework.boot:spring-boot-starter-webmvc")
```

```xml [maven]
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>
```

::

Build web, including RESTful, applications using Spring MVC.

Uses Apache Tomcat as the default embedded container.

<div v-click>

<br/>

## @RestController

@RestController -> @Controller -> @Component

</div>

---
layout: full
class: text-left
---

## RestController

````md magic-move
```kotlin
@RestController
class HelloController {
}
```

```kotlin
@RestController
class HelloController {

  @GetMapping("/hello")
  fun getCall() = "Hello World"
}
```

```kotlin
@RestController
class HelloController {

  @GetMapping("/hello")
  fun getCall() = "Hello World"

  @PostMapping("/hello")
  fun postCall() = "Hello World"

  @PutMapping("/hello")
  fun putCall() = "Hello World"

  @DeleteMapping("/hello")
  fun deleteCall() = "Hello World"
}
```

```kotlin
@RestController
class HelloController {

  //@GetMapping("/hello")
  @RequestMapping(method = [RequestMethod.GET], path = ["/hello"])
  fun getCall() = "Hello World"

  @PostMapping("/hello")
  fun postCall() = "Hello World"

  @PutMapping("/hello")
  fun putCall() = "Hello World"

  @DeleteMapping("/hello")
  fun deleteCall() = "Hello World"
}
```
````

<div v-if="1 == $clicks">
curl -X GET http://localhost:8080/hello

Hello World
</div>

<!--
Tous les verbes peuvent être gérés par RequestMapping
-->

---
layout: full
class: text-left
---

## Paramètres

<div v-click>

## Query param

```bash
curl -XGET "http://localhost:8080/hello?name=me"
```

```kotlin
@GetMapping("/hello")
fun queryParam(@RequestParam name: String) = "Hello $name"
```

</div>
<div v-click>

## Path param

```bash
curl -XGET http://localhost:8080/hello/world
```

```kotlin
@GetMapping("/hello/{name}")
fun path(@PathVariable name: String) = "Hello $name"
```

</div>

---
layout: full
class: text-left
---

## Query param optionnel

Par défaut un `@RequestParam` est **obligatoire** : absent, Spring répond 400

```kotlin
@GetMapping("/hello")
fun optional(@RequestParam name: String?) = "Hello ${name ?: "world"}"
```

<div v-click>

Trois façons de le rendre optionnel

```kotlin
// type nullable
fun a(@RequestParam name: String?) = "Hello $name"

// valeur par défaut
fun b(@RequestParam(defaultValue = "world") name: String) = "Hello $name"

// required = false
fun c(@RequestParam(required = false) name: String?) = "Hello $name"
```

```bash
curl -XGET "http://localhost:8080/hello"
curl -XGET "http://localhost:8080/hello?name=me"
```

</div>

---
layout: full
class: text-left
---

## Body param

```bash
curl -XPOST 'http://localhost:8080/hello' -d 'world'
```

```kotlin
@PostMapping("/hello")
fun body(@RequestBody name: String) = "Hello $name"
```

<div v-click>

## Header param

```bash
curl -XGET 'http://localhost:8080/hello' -H 'name: world'
```

```kotlin
@GetMapping("/hello")
fun header(@RequestHeader name: String) = "Hello $name"
```

</div>

---
layout: full
class: text-left
---

## Préfixe commun : `@RequestMapping` sur la classe

Le chemin de la classe est ajouté devant celui de chaque méthode

```kotlin
@RestController
@RequestMapping("/api/v1/hello")
class HelloController {

    @GetMapping
    fun hello() = "Hello world"

    @GetMapping("/{name}")
    fun path(@PathVariable name: String) = "Hello $name"

    @PostMapping
    fun body(@RequestBody name: String) = "Hello $name"
}
```

```bash
curl -XGET  http://localhost:8080/api/v1/hello
curl -XGET  http://localhost:8080/api/v1/hello/world
curl -XPOST http://localhost:8080/api/v1/hello -d 'world'
```

---
layout: full
class: text-left
---

## Code retour

```kotlin
@GetMapping("/hello/{name}")
fun path(@PathVariable name: String) = "Hello $name"
```

<div v-click>

```kotlin
@GetMapping("/hello/{name}")
fun helloPath(@PathVariable name: String) =
    ResponseEntity.status(HttpStatus.OK).body("Hello $name")
```

</div>

<div v-click>

```kotlin
@GetMapping("/hello/{name}")
fun helloPath(@PathVariable name: String) =
    ResponseEntity.ok("Hello $name")
```

</div>

<div v-click>

```kotlin
@GetMapping("/hello/{name}")
fun helloPath(@PathVariable name: String) = if (name.length <= 2) {
    ResponseEntity.badRequest().body("Name size must be > 2")
} else {
    ResponseEntity.ok("Hello $name")
}
```

</div>

---
layout: full
class: text-left
---

## Création : 201 + `Location`

Un `POST` qui crée une ressource répond **201 Created** avec l'URL de la ressource dans le header `Location`

```kotlin
@PostMapping("/hello")
fun create(@RequestBody name: String): ResponseEntity<String> {
    val uri = URI.create("/hello/${URLEncoder.encode(name, Charsets.UTF_8)}")
    return ResponseEntity.created(uri).body("Hello $name")
}
```

```bash
curl -v -XPOST 'http://localhost:8080/hello' -d 'world'
< HTTP/1.1 201
< Location: /hello/world
```

<div v-click>

Pour un code fixe sans logique, `@ResponseStatus` suffit

```kotlin
@PostMapping("/hello")
@ResponseStatus(HttpStatus.CREATED)
fun create(@RequestBody name: String) = "Hello $name"
```

</div>

---
layout: full
class: text-left
---

## DTO & serialization

Design Pattern - Data Transfer Object

Objet simple représentant la donnée

Dans le cas d'une API REST / Json il ne doit pas contenir de cycle pour être sérialisable en json.

---
layout: full
class: text-left
---

## DTO & serialization

```kotlin
data class PersonDTO(val name: String, val age: Int)
```

<div v-click>

## Serialization des réponses de base en JSON

```kotlin
@GetMapping("/hello")
fun hello() = ResponseEntity.ok(PersonDTO("John", 42))
```

</div>
<div v-click>

## Deserialization des body suivant le content-type

```kotlin
@PostMapping("/hello")
fun body(@RequestBody person: PersonDTO) = "Hello ${person.name}"
```

</div>
