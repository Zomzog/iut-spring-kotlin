---
layout: cover
---

# Spring validation

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

## Activation validation

````md magic-move
```kotlin
class DemoController(val demoRepository: DemoRepository) {
  fun list(
     i: Int // >= 10
  ) = ...
```

```kotlin
@Validated
class DemoController(val demoRepository: DemoRepository) {
  fun list(
     i: Int // >= 10
  ) = ...
```
```kotlin
@Validated
class DemoController(val demoRepository: DemoRepository) {
  fun list(
     @Min(10) i: Int
  ) = ...
```
```kotlin
@Validated
class DemoController(val demoRepository: DemoRepository) {
  fun list(
     @Max(5) @Min(10) i: Int
  ) = ...
```
````

<!--
Si on part de cette méthode où i doit supérieur à 10

@Validated marque la class comme ayant besoin de validation

Comme @Scope, on ajoute des informations pour spring.
Il va donc rajouter des vérifications sur les appels de méthodes

@Min(10) ajoute une contrainte sur la valeur
-->

<!--
On peut cumuler les contraintes,
même sans cohérence
-->

---
layout: full
class: text-left
---

## Constraint Annotation

```java
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

<!--
@Target: là où on peut utiliser l'annotation

@Retention: Elle doit rester lors de l'éxecution,
contrairement à d'autres qui sont là pour changer la compilation (ex lombok)

@Repeatable On peut l'utiliser plusieurs fois sur un élément

@Constraint validatedBy: La class qui valide,
spring/hibernate fourni ceux par default

Il faut pour qu'elle soit valide:

- message
- groups
- payload

-->

---
layout: full
class: text-left
---

## Meta annotation

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

<div v-click>

```kotlin
@Validated
class DemoController(val demoRepository: DemoRepository) {
  fun list(
     @MinMax i: Int
  ) = ...
```

</div>

<!--
On peut réutiliser Min et Max en les groupant dans une meta annotation

Avantage: c'est rapide

Inconvénient: les valeurs sont en dur

Les valeurs annotations doivent être des constantes
-->

---
layout: full
class: text-left
---

## Custom annotation

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

<div v-click>

```kotlin
class MinMaxValidator: ConstraintValidator<MinMax, Int> {
  private var min: Int = 0
  private var max: Int = 0

  override fun initialize(annotation: MinMax) {
    min = annotation.min
    max = annotation.max
  }

  override fun isValid(value: Int?, 
                       context: ConstraintValidatorContext?): Boolean {
    return value != null && value in min..max
  }
}
```

</div>

<!--
Ajout de deux valeurs variables min et max

Ajout d'une classe de validation

Un validateur étend l'interface ConstraintValidator

Il y a deux phases à la validation,
la création du validateur (initialize)
puis l'utilisation sur une valeur (isValid)
-->

---
layout: full
class: text-left
---

## Custom annotation

```kotlin
@Validated
class DemoController(val demoRepository: DemoRepository) {
  fun list(
     @MinMax(0, 10) i: Int
  ) = ...
```

---
layout: full
class: text-left
---

## API Validation

```kotlin
@RestController
@Validated
class DemoController(val demoRepository: DemoRepository) {
  @GetMapping
  fun list(@RequestParam(required = false) @Size(min=2, max=20) name: String?)
      = if (name == null) { TODO() }
```

<!--
La validation peut s'utiliser directement sur les Controlleurs
-->

---
layout: full
class: text-left
---

## Validation du body

```kotlin
@RestController
@Validated
class DemoController(val demoRepository: DemoRepository) {
  @PostMapping
  fun save(@Demo  @RequestBody demo: DemoDTO) = ...
```

[.hideCode]

```
data class DemoDTO(
        val id: UUID = UUID.randomUUID(),
        @field:Size(min#5, max#10)
        val name: String,
)
```

<!--
Pour valider un objet complet on peut mettre une annotation personnalisée
avec son validateur
-->

[transition#none-in, none-out]
---
layout: full
class: text-left
---

## Validation du body

```kotlin
@RestController
@Validated
class DemoController(val demoRepository: DemoRepository) {
  @PostMapping
  fun save(@Valid @RequestBody demo: DemoDTO) = ...
```

[fragment, step#1]

```kotlin
data class DemoDTO(
        val id: UUID = UUID.randomUUID(),
        @field:Size(min#5, max#10)
        val name: String,
)
```

<!--
Pour valider un objet complet on peut mettre une annotation personnalisée
avec son validateur
-->

[.columns]
---
layout: full
class: text-left
---

## Annotation target

[.column.is-three-fifths]
[source, java]

```
class Pony {
  @OnName
  private String name;

  @OnGet
  public String getName() {
    return name;
  }

  @OnSet
  public String setName(String n) {
    name = n;
  }
}
```

[fragment, step#1]
[.column]
[source, java]

```
class Pony(
  @field:OnName
  @get:OnGet
  @set:OnGet
  var name: String
)
```

<!--
La notion de getter/setter étant caché par Kotlin,
on doit fournir la cible des annotations
-->
