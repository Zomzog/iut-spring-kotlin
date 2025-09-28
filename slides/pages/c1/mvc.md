---
layout: full
class: text-left
---

# Hello World Rest

Requête

```
curl -XGET localhost:8080/hello
```

Réponse:

```
Hello World
```

---
layout: full
class: text-left
---

# spring-boot-starter-web
[source,xml]

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

[transition#fade-out]
---
layout: full
class: text-left
---

# RestController

RestController -> Controller -> Component

[transition#fade-out]
---
layout: full
class: text-left
---

# RestController

```kotlin
@RestController
class HelloController {


}
```

[transition#fade-in]
---
layout: full
class: text-left
---

# RestController

```kotlin
@RestController
class HelloController {

    @GetMapping("/hello")
    fun hello() = "Hello World"
}
```

[.columns.is-vcentered]
---
layout: full
class: text-left
---

# Mappings

[.column]
--
GetMapping

PostMapping

PutMapping

DeleteMapping
--

[.column]
--
alias ->
--

[.column]
--
RequestMapping
--

[NOTE.speaker]
--
Tous les verbes peuvent être gérés par RequestMapping
--

---
layout: full
class: text-left
---

# API Rest

[.highlight-current-red.step]#GET# [.highlight-current-red.step]#/ponies#/[.highlight-current-red.step]#{name}#/[.highlight-current-red.step]#type#

---
layout: full
class: text-left
---

# API Rest

GET /ponies/{name}/[.highlight-red.step]#?type#earth#

---
layout: full
class: text-left
---

# Lister

[source]

```
GET /ponies?age#42
```

---
layout: full
class: text-left
---

# Créer

[source]

```
POST /ponies
```

---
layout: full
class: text-left
---

# Obtenir un élément

[source]

```
GET /ponies/{name}
```

---
layout: full
class: text-left
---

# Mettre à jour un élément

[source]

```
PUT /ponies/{name}
```

---
layout: full
class: text-left
---

# Supprimer un élément
[source]

```
POST /ponies/{name}
```

---
layout: full
class: text-left
---

# Paramètres

[fragment, step#0]

```kotlin
@GetMapping("/hello")
fun queryParam(@RequestParam name: String) = "world of $name"
```

[fragment, step#1]

```kotlin
@GetMapping("/hello/{name}")
fun path(@PathVariable name: String) = "world of $name"
```

[fragment, step#2]

```kotlin
@PostMapping("/hello")
fun body(@RequestBody name: String) = "world of $name"
```

[fragment, step#3]

```kotlin
@GetMapping("/hello")
fun header(@RequestHeader name: String) = "world of $name"
```

---
layout: full
class: text-left
---

# Code retour

```kotlin
    @GetMapping("/hello/{name}")
    fun path(@PathVariable name: String) = "world of $name"

    @GetMapping("/hello/{name}")
    fun helloPath(@PathVariable name: String) #
        ResponseEntity.status(HttpStatus.OK).body("world of $name")
```

---
layout: full
class: text-left
---

# Code retour

```kotlin
    @GetMapping("/hello/{name}")
    fun path(@PathVariable name: String) = "world of $name"

    @GetMapping("/hello/{name}")
    fun helloPath(@PathVariable name: String) #
        ResponseEntity.ok("world of $name")
```

---
layout: full
class: text-left
---

# Code retour

```kotlin
    @GetMapping("/hello/{name}")
    fun helloPath(@PathVariable name: String) = if (name.length <# 2) {
        ResponseEntity.badRequest().body("Name size must be > 2")
    } else {
        ResponseEntity.ok("world of $name")
    }
```

---
layout: full
class: text-left
---

# DTO

Desing Pattern - Data Transfert Object

Objet simple représentant la donnée

Dans le cas d'une API REST / Json il ne doit pas contenir de cycle pour être sérialisable en json.

---
layout: full
class: text-left
---

# DTO

```kotlin
data class PersonDTO(val name: String, val age: Int)
```

```kotlin
@GetMapping("/hello")
fun hello() = ResponseEntity.ok(PersonDTO("John", 42))
```
