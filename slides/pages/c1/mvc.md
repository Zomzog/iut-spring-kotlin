---
layout: cover
hideInToc: false
---

# Spring MVC

---
layout: full
class: text-left
---

## spring-boot-starter-web

:: code-group

```kotlin [gradle]

 implementation("org.springframework.boot:spring-boot-starter-web")
```

```xml [maven]
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
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
curl -XGET <http://localhost:8080/hello?name=me>
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

## DTO & serialization

Desing Pattern - Data Transfert Object

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
fun body(@RequestBody person: PersonDTO) = "Hello $person.name"
```

</div>
