---
layout: cover
---

# Spring validation

---
layout: full
class: text-left
transition: fade
---

## Pourquoi valider ?

````md magic-move
```bash
curl -i "http://localhost:8080/starwars/movies?episode=11"

HTTP/1.1 200
Content-Type: application/json
```
```bash
curl -i "http://localhost:8080/starwars/movies?episode=11"

HTTP/1.1 400
Content-Type: application/problem+json
```
````

````md magic-move {at: 0}
```json
[]
```
```json
{
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failure",
  "instance": "/starwars/movies"
}
```
````

<!--
Un appel avec l'épisode 11 de Star Wars, ça répond 200 avec un body vide.

En fait la requête de base n'a pas de sens :
on voudrait plutôt spécifier que 11 n'est pas un numéro d'épisode valide.
Du coup, on va faire de la validation de ce paramètre.

La réponse devient un 400 avec un body d'erreur.
Ce body est au format ProblemDetail (RFC 9457, application/problem+json),
c'est la cible. Comment l'activer et le personnaliser :
on le verra dans la partie sur les erreurs (ControllerAdvice et formats de réponse).
-->

---
layout: full
class: text-left
---

## Spring validation

:: code-group

```kotlin [gradle]

 implementation("org.springframework.boot:spring-boot-starter-validation")
```

```xml [maven]
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

::

Bean Validation with Hibernate validator.

<!--
Permet d'ajouter de la validation sur les paramètres des méthodes.
-->

---
layout: full
class: text-left
---

## Valider paramètres et body

````md magic-move
```kotlin
@RestController
@RequestMapping("/starwars/movies")
class StarWarsMovieController {

  @GetMapping
  fun search(@RequestParam episode: Int) {
  }
}
```
```kotlin
@RestController
@RequestMapping("/starwars/movies")
class StarWarsMovieController {

  @GetMapping
  fun search(@RequestParam @Min(4) episode: Int) {
  }
}
```
```kotlin
@RestController
@RequestMapping("/starwars/movies")
class StarWarsMovieController {

  @GetMapping
  fun search(@RequestParam @Min(4) @Max(6) episode: Int) {
  }
}
```
```kotlin
@RestController
@RequestMapping("/starwars/movies")
class StarWarsMovieController {

  @GetMapping
  fun search(@RequestParam @Min(6) @Max(4) episode: Int) { // ⚠️ aucune valeur n'est valide
  }
}
```
```kotlin
@RestController
@RequestMapping("/starwars/movies")
class StarWarsMovieController {

  @GetMapping
  fun search(@RequestParam @Min(4) @Max(6) episode: Int) {
  }
}
```
```kotlin
@RestController
@RequestMapping("/starwars/movies")
class StarWarsMovieController {

  @GetMapping
  fun search(@RequestParam @Min(4) @Max(6) episode: Int) {
  }

  @PostMapping
  fun create(@RequestBody dto: StarWarsMovieDto) {
  }
}

data class StarWarsMovieDto(
    val episode: Int,
    val name: String
)
```
```kotlin
@RestController
@RequestMapping("/starwars/movies")
class StarWarsMovieController {

  @GetMapping
  fun search(@RequestParam @Min(4) @Max(6) episode: Int) {
  }

  @PostMapping
  fun create(@RequestBody @Valid dto: StarWarsMovieDto) {
  }
}

data class StarWarsMovieDto(
    val episode: Int,
    val name: String
)
```
```kotlin{all|15}
@RestController
@RequestMapping("/starwars/movies")
class StarWarsMovieController {

  @GetMapping
  fun search(@RequestParam @Min(4) @Max(6) episode: Int) {
  }

  @PostMapping
  fun create(@RequestBody @Valid dto: StarWarsMovieDto) {
  }
}

data class StarWarsMovieDto(
    @field:Min(4)
    @field:Max(6)
    val episode: Int,
    @field:NotBlank
    val name: String
)
```
````

<!--
Si on part de cette méthode, tous les entiers sont acceptés.

@Min(4) ajoute une contrainte sur la valeur.
Pas besoin de @Validated : depuis Spring Framework 6.1, Spring MVC valide
directement les paramètres de méthode qui portent une contrainte.
L'exception levée est HandlerMethodValidationException, qui donne un 400.

On peut cumuler les contraintes : @Min(4) et @Max(6)
Seule la première trilogie est valide (les épisodes 4, 5 et 6 sont sortis en premier,
dans les années 70-80).

⚠️ Bean Validation ne vérifie pas la cohérence des contraintes :
avec @Min(6) @Max(4) l'application démarre, mais plus aucune valeur n'est valide.

C'est pareil pour un body : on veut aussi le valider.
On ajoute le POST et son DTO, qui acceptent pour l'instant n'importe quoi.

@Valid sur le paramètre dit à Spring de valider les contraintes de l'objet.
Sans @Valid, les contraintes du DTO sont ignorées.

Le DTO a ses propres contraintes (episode entre 4 et 6, name non vide).
Ici l'exception est MethodArgumentNotValidException (400 aussi),
pas HandlerMethodValidationException : il n'y a aucune contrainte sur le paramètre lui-même.

Pourquoi @field: ? C'est la slide suivante.
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Annotation target

::left::

### Java

````md magic-move
```java
class Pony {
  @OnName
  private String name;
}
```
```java
class Pony {
  @OnName
  private String name;

  @OnGet
  public String getName() {
    return name;
  }
}
```
```java
class Pony {
  @OnName
  private String name;

  @OnGet
  public String getName() {
    return name;
  }

  @OnSet
  public void setName(String n) {
    name = n;
  }
}
```
````

::right::

<div v-click>

### Kotlin

```kotlin
class Pony(
  @field:OnName


  @get:OnGet



  @set:OnSet
  var name: String
)
```

</div>

<!--
La notion de getter/setter étant caché par Kotlin,
on doit fournir la cible des annotations

C'est pour ça que, dans le DTO, on écrit @field:Min(4) :
sans cible, Kotlin met l'annotation sur le paramètre du constructeur,
et Bean Validation ne la voit pas sur le champ.
-->

---
layout: full
class: text-left
transition: fade
---

## Activation validation

````md magic-move {at: 0}
```kotlin
@RestController
class StarWarsMovieController {

  @GetMapping
  fun search(@RequestParam @Min(4) @Max(6) episode: Int) {
  }
}
```
```kotlin
@RestController
🚫 @Validated
class StarWarsMovieController {

  @GetMapping
  fun search(@RequestParam @Min(4) @Max(6) episode: Int) {
  }
}
```
```kotlin
@RestController
class StarWarsMovieController {

  @GetMapping
  fun search(@RequestParam @Min(4) @Max(6) episode: Int) {
  }
}
```
````

<div v-click.at='3'>

````md magic-move {at: 3}
```kotlin
@Service
@Validated
class StarWarsMovieService {

  fun search(@Min(4) @Max(6) episode: Int) {
  }
}
```
````

Sur un `@RestController`, la validation des paramètres est **automatique** (Spring 6.1+).

Sur les autres beans, comme un `@Service`, il faut **explicitement** poser `@Validated`.

<small>Doc Spring : [validation d'un contrôleur](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-validation.html) · [validation de méthode sur un bean](https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html#validation-beanvalidation-spring-method)</small>

</div>

<!--
Sur un contrôleur, on ne met plus @Validated.
La validation est native depuis Spring Framework 6.1 (Spring Boot 3.2).

Si on le laisse, on repasse par un proxy AOP, on perd le support natif :
l'exception devient ConstraintViolationException et donne un 500 (pas de handler par défaut).
C'est ce qu'on trouve dans beaucoup de tutoriels, il faut le retirer en migrant.

Par contre, sur les autres beans comme un @Service, il le faut toujours.
@Validated marque la classe comme ayant besoin de validation.
Comme @Scope, on ajoute des informations pour Spring : il crée un proxy
qui vérifie les contraintes à chaque appel de méthode.

Sans @Validated, les contraintes d'un @Service sont ignorées en silence.

Le proxy a besoin d'une classe ouverte : c'est kotlin("plugin.spring") qui s'en charge.
-->

---
layout: full
class: text-left
---

## Constraint Annotation

````md magic-move
```kotlin
@Service
@Validated
class StarWarsMovieService {

  fun search(@Min(4) @Max(6) episode: Int) {
  }
}
```
```java {all|6|2|1,3|5|7|9-11|13|all}
@Target({METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER,TYPE_USE})
@Retention(RUNTIME)
@Repeatable(List.class)
@Documented
@Constraint(validatedBy = { })
public @interface Min {
 String message() default "{jakarta.validation.constraints.Min.message}";

 Class<?>[] groups() default { };

 Class<? extends Payload>[] payload() default { };

 long value();
}
```
````

<div style="min-height: 4.5em">
<v-switch at="1">
<template #1>

Que cache `@Min(4)` ? Sa définition dans Jakarta Validation : une annotation Java classique.

</template>
<template #2>

`@interface` déclare une annotation : ici `Min`, que l'on pose avec `@Min`.

</template>
<template #3>

`@Retention(RUNTIME)` : l'annotation reste lisible à l'exécution, ce dont Spring a besoin.

</template>
<template #4>

`@Target` dit où on peut la poser (paramètre, champ...), `@Repeatable` autorise de la répéter.

</template>
<template #5>

`@Constraint(validatedBy)` : la classe qui valide. Vide ici, Hibernate Validator fournit celles par défaut.

</template>
<template #6>

`message` : le texte de l'erreur quand la contrainte n'est pas respectée.

</template>
<template #7>

`groups` : valider seulement dans un contexte (création, modification). `payload` : métadonnées libres.

</template>
<template #8>

`value` : la valeur propre à `@Min`, c'est ce qui change d'une contrainte à l'autre.

</template>
<template #9-10>

Pour être valide, une contrainte a besoin de `message`, `groups` et `payload`.

</template>
</v-switch>
</div>

<!--
@Target: là où on peut utiliser l'annotation

@Retention: Elle doit rester lors de l'exécution,
contrairement à d'autres qui sont là pour changer la compilation (ex lombok)

@Repeatable On peut l'utiliser plusieurs fois sur un élément

@Constraint validatedBy: La class qui valide,
spring/hibernate fournit ceux par défaut

Il faut pour qu'elle soit valide:

- message
- groups : permet de ne déclencher la contrainte que dans un contexte donné
  (ex : à la création mais pas à la modification), en choisissant le groupe validé
- payload : métadonnées libres attachées à la contrainte (ex : un niveau de sévérité),
  que le code qui lit les violations peut exploiter ; Spring ne s'en sert pas par défaut

-->

---
layout: full
class: text-left
---

## Meta annotation - Combiner des contraintes

````md magic-move {at: 0}
```kotlin {all|all|all|all|all|all}
@RestController
class DemoController {

  @GetMapping("/meta")
  fun list(@RequestParam @Min(0) @Max(10) i: Int) = i
}
```
```kotlin
@RestController
class DemoController {

  @GetMapping("/meta")
  fun list(@RequestParam @MinMax i: Int) = i
}
```
````

<div v-click.at='1'>

````md magic-move {at: 2}
```kotlin {all|3}
@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
@Retention(RUNTIME)
annotation class MinMax(
)
```
```kotlin
@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
@Retention(RUNTIME)
@Constraint(validatedBy = [])
annotation class MinMax(
    val message: String = "Value must be between min and max",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
```
```kotlin
@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
@Retention(RUNTIME)
@Constraint(validatedBy = [])
@Min(value = 0)
annotation class MinMax(
    val message: String = "Value must be between min and max",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
```
```kotlin
@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
@Retention(RUNTIME)
@Constraint(validatedBy = [])
@Min(value = 0)
@Max(value = 10)
annotation class MinMax(
    val message: String = "Value must be between min and max",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
```
````

</div>

<!--
On utilise @Min(0) et @Max(10) sur ce paramètre.
Si on veut les réutiliser à plusieurs endroits, on les groupe dans une meta annotation.

On crée MinMax : en Kotlin, c'est `annotation class` qui crée une annotation.
Puis on lui dit que c'est une contrainte avec @Constraint
et on lui ajoute @Min et @Max.

Ensuite on remplace @Min(0) @Max(10) par @MinMax sur le paramètre.

Avantage: c'est rapide

Inconvénient: les valeurs sont en dur

Les valeurs annotations doivent être des constantes
-->

---
layout: TwoColumnsTitle
class: text-left
transition: fade
---

::title::

## Custom annotation

::left::

````md magic-move {at: 0}
```kotlin
@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
@Retention(RUNTIME)
@Constraint(validatedBy = [])
@Min(value = 0)
@Max(value = 10)
annotation class MinMax(
    val message: String = "Value must be between min and max",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
```
```kotlin
@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
@Retention(RUNTIME)
@Constraint(validatedBy = [])
@Min(value = 0)
@Max(value = 10)
annotation class MinMax(
  val min: Int,
  val max: Int,
  val message: String = "Value must be between min and max",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
)
```
```kotlin
@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
@Retention(RUNTIME)
@Constraint(validatedBy = [])
🚫 @Min(value = min)
🚫 @Max(value = max)
annotation class MinMax(
  val min: Int,
  val max: Int,
  val message: String = "Value must be between min and max",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
)
```
```kotlin
@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
@Retention(RUNTIME)
@Constraint(validatedBy = [MinMaxValidator::class])
annotation class MinMax(
  val min: Int,
  val max: Int,
  val message: String = "Value must be between min and max",
  val groups: Array<KClass<*>> = [],
  val payload: Array<KClass<out Payload>> = [],
)
```
````

<div v-click="[2, 3]" style="position: absolute; width: 28rem">

Impossible : les valeurs d'une annotation doivent être des **constantes connues à la compilation**, et `min` n'en est pas une.

</div>

<div v-click.at='11'>

```kotlin
@RestController
class DemoController {

  @GetMapping("/custom")
  fun list(@RequestParam @MinMax(0, 10) i: Int) = i
}
```

</div>

::right::

<div v-click.at='3'>

````md magic-move {at: 4}
```kotlin
class MinMaxValidator: ConstraintValidator<MinMax, Int> {
}
```
```kotlin
class MinMaxValidator: ConstraintValidator<MinMax, Int> {
  private var min: Int = 0
  private var max: Int = 0
}
```
```kotlin {all|1,5}
class MinMaxValidator: ConstraintValidator<MinMax, Int> {
  private var min: Int = 0
  private var max: Int = 0

  override fun initialize(annotation: MinMax) {
  }
}
```
```kotlin
class MinMaxValidator: ConstraintValidator<MinMax, Int> {
  private var min: Int = 0
  private var max: Int = 0

  override fun initialize(annotation: MinMax) {
    min = annotation.min
    max = annotation.max
  }
}
```
```kotlin {all|1,11}
class MinMaxValidator: ConstraintValidator<MinMax, Int> {
  private var min: Int = 0
  private var max: Int = 0

  override fun initialize(annotation: MinMax) {
    min = annotation.min
    max = annotation.max
  }

  override fun isValid(
    value: Int?,
    context: ConstraintValidatorContext?
  ): Boolean {
  }
}
```
```kotlin
class MinMaxValidator: ConstraintValidator<MinMax, Int> {
  private var min: Int = 0
  private var max: Int = 0

  override fun initialize(annotation: MinMax) {
    min = annotation.min
    max = annotation.max
  }

  override fun isValid(
    value: Int?,
    context: ConstraintValidatorContext?
  ): Boolean {
    return value != null && value in min..max
  }
}
```
````

</div>

<!--
Ajout de deux valeurs variables min et max

On ne peut pas les passer à @Min et @Max : les valeurs d'une annotation sont des constantes.

Ajout d'une classe de validation

Un validateur étend l'interface ConstraintValidator

initialize reçoit l'annotation MinMax : c'est le premier type générique de ConstraintValidator<MinMax, Int>
isValid reçoit la valeur à valider : c'est le second type générique, ici Int

Il y a deux phases à la validation,
la création du validateur (initialize)
puis l'utilisation sur une valeur (isValid)

Comme pour @Min, la contrainte custom est appliquée directement sur le paramètre,
sans @Validated.
-->

---
layout: full
class: text-left
---

## TL;DR Validation

| Où | Comment | Exception |
|---|---|---|
| Paramètre de contrôleur | Contrainte sur le paramètre (`@Min`, `@Max`...) | `HandlerMethodValidationException` (400) |
| `@RequestBody` | `@Valid` + contraintes `@field:` dans le DTO | `MethodArgumentNotValidException` (400) |
| `@Service` & co | `@Validated` sur la classe | `ConstraintViolationException` |
| Contrainte custom | Meta annotation ou `@Constraint(validatedBy = ...)` | Comme une contrainte native |

- Pas de `@Validated` sur un contrôleur depuis Spring Framework 6.1
- Bean Validation ne vérifie pas la cohérence des contraintes (`@Min(6) @Max(4)` ⚠️)

<!--
Sur un contrôleur : rien à ajouter, la validation est native.
Ailleurs : @Validated.

Les contraintes sont cumulables, mais rien ne vérifie qu'elles sont cohérentes entre elles.
-->
