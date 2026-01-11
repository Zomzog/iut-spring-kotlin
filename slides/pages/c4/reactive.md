---
layout: full
class: text-left
---

# Réactif (WebFlux + Coroutines)

<v-click>

- Utiliser WebFlux avec les coroutines Kotlin pour des endpoints non-bloquants

</v-click>
<v-click>

- Garder le modèle de threads simple : fonctions `suspend`, éviter les appels bloquants

</v-click>

---
layout: full
class: text-left
---

## Exemple : contrôleur coroutine

<v-click>

```kotlin
@RestController
class ReactiveController {
  @GetMapping("/reactive/hello")
  suspend fun hello(): String { delay(50); return "ok" }
}
```

</v-click>
