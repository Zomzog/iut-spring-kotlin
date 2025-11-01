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
transition: fade
---

## Validation simples

````md magic-move
```kotlin
class DemoController {
  fun list( i: Int) = ...
```

```kotlin
@Validated
class DemoController {
  fun list(i: Int) = ...
```
```kotlin
@Validated
class DemoController {
  fun list(@Min(10) i: Int) = ...
```
```kotlin
@Validated
class DemoController {
  fun list(@Max(5) @Min(10) i: Int) = ...
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

## Validation d'objets

````md magic-move
```kotlin
@Validated
class DemoController {
  fun list(@Max(5) @Min(10) i: Int) = ...
```
```kotlin
@Validated
class DemoController {
  fun list(@Max(5) @Min(10) i: Int) = ...

  fun create(@RequestBody dto: DemoDto) = ...
}

data class DemoDto(
    val i: Int,
    val s: String
)
```
```kotlin
@Validated
class DemoController {
  fun list(@Max(5) @Min(10) i: Int) = ...

  fun create(@RequestBody @Valid dto: DemoDto) = ...
}

data class DemoDto(
    val i: Int,
    val s: String
)
```
```kotlin
@Validated
class DemoController {
  fun list(@Max(5) @Min(10) i: Int) = ...

  fun create(@RequestBody @Valid dto: DemoDto) = ...
}

data class DemoDto(
    @field:Min(5)
    val i: Int,
    @field:NotBlank
    val s: String
)
```
````

---
layout: full
class: text-left
transition: fade
---

## Activation validation

```kotlin
@Validated
class DemoController {
  fun list(
     @Max(5) @Min(10) i: Int
  ) = ...
```

<div v-click>

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

</div>

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

````md magic-move
```kotlin
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

<div v-click>

```kotlin
@Validated
class DemoController {
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
transition: fade
---

## Custom annotation

````md magic-move
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
@Min(value = min)
@Max(value = max)
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

---
layout: full
class: text-left
transition: fade
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

````md magic-move
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
````

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
@Component
class DemoController {
  fun list(
     @MinMax(0, 10) i: Int
  ) = ...
```

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Annotation target

::left::

```java
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

::right::

<div v-click>

```kotlin
class Pony(
  @field:OnName


  @get:OnGet



  @set:OnGet
  var name: String
)
```

</div>

<!--
La notion de getter/setter étant caché par Kotlin,
on doit fournir la cible des annotations
-->
