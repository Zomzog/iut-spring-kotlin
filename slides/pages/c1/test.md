---
layout: full
class: text-left
---

# Tests

![code_coverage](/code_coverage.jpg)

---
layout: full
class: text-left
---

# Pyramide des Tests

![pyramide_test](/pyramide_test.webp)

---
layout: full
class: text-left
---

# Tests unitaires

Ces tests ne sont pas liés à l'utilisation de spring.

On peut utiliser un framework de test au choix (junit, spock...)
et un système d'assertion au choix (junit, assertk...).

Junit est embarqué dans les dépendances Spring.

---
layout: full
class: text-left
---

---
layout: full
class: text-left
---

# Tests unitaires

```kotlin
@Service
class DummyService {

    fun doSomething(pony: String) = if (pony.length < 3) {
        "bad"
    } else {
        "good"
    }
}
```

```kotlin
class DummyServiceTest {

    private val service = DummyService()

    @Test
    fun `length gt 3 is good`() {
        // WHEN
       val result = service.doSomething("pony")
        // THEN
        assertThat(result).isEqualTo("good") // assertK
    }
}
```

---
layout: TwoColumnsTitle
class: text-left
---

::title::

# Tests unitaires

::left::

```kotlin
@TestInstance(PER_CLASS)
class PonyTest {
    @BeforeAll
    fun beforeAll() = println("before all")
    @BeforeEach
    fun beforeEach() = println("before each")
    @Test
    fun test1() = println("test1")
    @Test
    fun test2() = println("test2")
    @AfterEach
    fun afterEach() = println("after each")
    @AfterAll
    fun afterAll() = println("after all")
}
```

::right::

```kotlin
before all
before each
test1
after each
before each
test2
after each
after all
```

---
layout: full
class: text-left
---

# Tests d'intégration

```kotlin
@Service
class DummyService {

    fun doSomething(pony: String) = if (pony.length < 3) {
        "bad"
    } else {
        "good"
    }
}
```

````md magic-move
```kotlin
@SpringBootTest // Cherche et lance le SpringBootApplication
class DummyServiceTest {
}
```
```kotlin
@SpringBootTest // Cherche et lance le SpringBootApplication
class DummyServiceTest {
    @Autowired // Récupère le bean dans le contexte spring
    private lateinit var service: DummyService
}
```
```kotlin
@SpringBootTest // Cherche et lance le SpringBootApplication
class DummyServiceTest {
    @Autowired // Récupère le bean dans le contexte spring
    private lateinit var service: DummyService

    @Test
    fun `length gt 3 is good`() {
        // WHEN
       val result = service.doSomething("pony")
        // THEN
        assertThat(result).isEqualTo("good") // assertK
    }
}
```
````

---
layout: full
class: text-left
---

<img src="/warning.png" alt="warning" class="size-48 mx-auto"/>

`@SpringBootTest` ne fonctionne que dans un sous package de l'application `@SpringBootApplication`

<img src="/warning.png" alt="warning" class="size-48 mx-auto"/>

---
layout: cover
---

# Bouchons (mock)

## Avec Mockk (Mockito en Kotlin)

---
layout: full
class: text-left
---

```kotlin
@Service
class DummyService() {
    fun callDep(pony: String) = dependency.call()
}
```

````md magic-move
```kotlin
@SpringBootTest
class DummyServiceIntTest {
    @MockkBean
    private lateinit var dependency: Dependency
    @Autowired
    private lateinit var service: DummyService

    @Test
    fun `call good`() {
        // WHEN
 🚫     val result = service.callDep("pony") // call the mock
        // THEN
        assertThat(result).isEqualTo("good")
    }
}
```

```kotlin
@SpringBootTest
class DummyServiceIntTest {
    @MockkBean
    private lateinit var dependency: Dependency
    @Autowired
    private lateinit var service: DummyService

    @Test
    fun `call good`() {
        // GIVEN
        every { dependency.call() } returns true
        // WHEN
        val result = service.callDep("pony") // call the mock
        // THEN
        assertThat(result).isEqualTo("good")
    }
}
```
````

---
layout: full
class: text-left
---

# Every

<div v-click>

## Réponse sans regarder le paramètre

```kotlin
every { dependency.call(any()) } returns true
```

</div>
<div v-click>

## Réponse seulement si le paramètre est exactement celui attendu

```kotlin
every { dependency.call(Pony("name") } returns "23"
```

</div>
<div v-click>

## Envoi d'une excéption

```kotlin
every { dependency.call(more(10), any()) } throws Exception("Nope")
```

</div>
<div v-click>

## Appel d'une fonction réelle

```kotlin
every { dependency.call(any(), any()) } answers { callRealMethod() }
```

</div>
<div v-click>

## Changer la valeur

```kotlin
every { dependency.call(eq(42), any()) } returnsMany listOf(1,2,3)
```

</div>
