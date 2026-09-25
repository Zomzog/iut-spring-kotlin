---
tags: [spring, kotlin, iut, relecture, migration]
date: 2026-09-25
cible: spring-boot-4.1
---

← [[2026]]

# §1 — Migration Spring Boot 4.1

Transverse aux 4 cours **et** au projet `code/`. À traiter une fois, en premier : plusieurs
corrections des notes de cours en dépendent.

> [!info] État des lieux
> - **Spring Boot 4.1.0** est sorti le **10 juin 2026** ; la doc courante est en 4.1.1.
> - Spring Boot 4.0 (nov. 2025) embarque Spring Framework 7.0, Spring Security 7.0,
>   Spring Data 2025.1, **Jackson 3.0** et Kotlin 2.2.20. 4.1 monte à Kotlin 2.3.
> - `code/build.gradle.kts` est en **Spring Boot 3.5.6 / Kotlin 1.9.25** : deux majeures de
>   retard, et Kotlin est sous la baseline exigée.

## Ruptures qui touchent les slides

### Renommage des starters

Spring Boot 4 éclate les gros jars en modules plus petits et renomme les starters
correspondants.

- [ ] 🟠 `spring-boot-starter-web` → **`spring-boot-starter-webmvc`**
  - `[[slides/pages/c1/mvc|c1/mvc.md:19]]` (gradle) et `:25` (maven) — la slide est titrée
    « ## spring-boot-starter-web », le titre change aussi
  - `[[slides/pages/c1/springboot|c1/springboot.md:340]]`
- [ ] 🟠 Vérifier les autres starters cités : `spring-boot-starter-validation`,
  `-security`, `-actuator`, `-cache`, `-data-jpa`, `-webflux`, `-data-r2dbc` sont inchangés.
  `spring-boot-starter-kafka` **existe désormais** (nouveau en 4.x) → `c4/messaging.md:26`
  est bon pour la cible, mais mérite une note orateur : en 3.x il fallait
  `org.springframework.kafka:spring-kafka`.
- [ ] 🔵 Chaque techno a maintenant un starter **et** son starter de test
  (ex. `spring-boot-starter-graphql` / `-graphql-test`). À mentionner une fois en c1 quand
  on parle des starters.

### Jackson 3

- [ ] 🟠 GroupId `com.fasterxml.jackson` → **`tools.jackson`**.
  - `code/build.gradle.kts:39` : `com.fasterxml.jackson.module:jackson-module-kotlin` →
    `tools.jackson.module:jackson-module-kotlin`
  - `[[slides/pages/c1/test|c1/test.md:438,455,475]]` : `ObjectMapper()` — l'import change
    (`tools.jackson.databind.ObjectMapper`)
- [ ] 🟡 Au passage, dans les exemples de test : **injecter le bean `ObjectMapper`** plutôt
  que l'instancier. `ObjectMapper()` nu n'a pas la configuration Spring (modules Kotlin,
  dates, `FAIL_ON_UNKNOWN_PROPERTIES`), donc le test ne sérialise pas comme l'application.
  C'est un vrai piège, et la correction est d'une ligne : `@Autowired lateinit var objectMapper: ObjectMapper`.
- [ ] 🟠 Propriétés déplacées : `spring.jackson.read.*` → `spring.jackson.json.read.*`.
  Aucune slide concernée aujourd'hui, mais à savoir si le TP en utilise.
- [ ] 🟡 Un module de compatibilité `spring-boot-jackson2` existe, si la migration du TP doit
  être étalée.

### Annotations de test supprimées

- [ ] 🟠 `@MockBean` et `@SpyBean` sont **supprimés** → `@MockitoBean` / `@MockitoSpyBean`.
  Aucune slide ne les utilise directement (le cours est sur MockK), mais les étudiants les
  croiseront dans tous les tutoriels en ligne : une phrase suffit.
- [ ] 🔴 Côté MockK, **springmockk 5.x** (compatible Spring Framework 7) renomme
  `@SpykBean` → **`@MockkSpyBean`**, pour s'aligner sur `@MockitoSpyBean`.
  `@MockkBean` est inchangé.
  - `[[slides/pages/c1/test|c1/test.md:334,352,371]]` : trois occurrences de `@SpykBean`
- [ ] 🔵 **La dépendance springmockk n'est citée nulle part** dans les 4 cours, alors que
  `@MockkBean` apparaît dès c1 (`test.md:213`) puis en c2 et c3. Ajouter une ligne sur la
  slide des dépendances de test :
  `testImplementation("com.ninja-squad:springmockk:5.0.1")` — en précisant que ce n'est
  **pas** un projet Spring officiel (cf. [[2026/À vérifier]]).
- [ ] 🟠 `TestRestTemplate` exige désormais `@AutoConfigureTestRestTemplate` explicite et la
  dépendance `spring-boot-resttestclient`. Non utilisé dans les slides, mais fréquent en TP.

### Imports déplacés

- [ ] 🟠 L'éclatement des modules déplace des annotations de test. Exemple :
  `@WebMvcTest` vit désormais dans
  `org.springframework.boot.webmvc.test.autoconfigure`. Les slides ne montrent pas les
  imports, donc rien à corriger visuellement — mais à vérifier dans `code/` et à signaler
  aux étudiants qui copieront du code 3.x.

### Baselines

- [ ] 🟠 **Java 17 minimum** (LTS récente encouragée), **Kotlin 2.2+** obligatoire,
  GraalVM 25+ pour le native-image, Gradle 9 supporté (8.14+ toujours accepté).
  `code/` est en Kotlin **1.9.25** : à monter.

## Nouveautés à introduire dans le cours

Pas des corrections, mais ce qui différencie un cours 2026 d'un cours 2024.

- [ ] 🔵 **`@ImportHttpServices`** + `spring.http.serviceclient.<group>.*` : Spring Boot 4
  auto-configure les interfaces clientes `@HttpExchange`/`@GetExchange`. Remplace la
  construction manuelle du `HttpServiceProxyFactory` montrée en
  `[[slides/pages/c3/rest-client|c3/rest-client.md:437-444]]` → détail dans [[2026/Cours 3]].
- [ ] 🔵 **`spring.http.clients.*`** : timeouts (`connect-timeout`, `read-timeout`),
  redirections, choix du connecteur, pour **tous** les clients d'un coup. Le cours ne parle
  aujourd'hui **jamais** de timeouts sur un appel HTTP sortant, alors que c'est la première
  cause d'incident en production.
- [ ] 🔵 **`RestTestClient`** et **`MockMvcTester`** : les deux nouvelles API de test web.
  `MockMvc` + le DSL Kotlin enseignés en c1 restent parfaitement valides (`@WebMvcTest`
  auto-configure toujours `MockMvc`) → à présenter comme alternatives, pas comme
  remplacement. Une slide en fin de chapitre test suffit.
- [ ] 🔵 **Threads virtuels** (`spring.threads.virtual.enabled=true`) : c'est l'alternative
  pragmatique au réactif depuis Java 21, et `[[slides/pages/c4/reactive|c4/reactive.md]]`
  n'en dit rien. Parler de WebFlux en 2026 sans mentionner les threads virtuels donne une
  vision fausse du choix d'architecture.
- [ ] 🔵 **`ProblemDetail` / RFC 9457** — voir [[2026/Cours 2]], c'est le meilleur
  rapport valeur/effort du cours 2.
- [ ] 🔵 **Logs structurés JSON** natifs depuis Boot 3.4
  (`logging.structured.format.console=ecs`) — voir [[2026/Cours 4]].
- [ ] ⚪ Anecdotes 4.1 utiles si le temps le permet : auto-configuration gRPC, mitigation
  SSRF sur les clients HTTP (`InetAddressFilter`), connexions datasource paresseuses
  (`spring.datasource.connection-fetch=lazy`), propagation automatique du contexte Micrometer
  sur `@Async`.

## Frise historique à mettre à jour

- [ ] ⚪ `[[slides/pages/c1/history|c1/history.md:59-81]]` : le `gitGraph` s'arrête sur
  Spring Boot 4.0.0 en 2025-11. Ajouter **4.1 en 2026-06**, ce qui permet au passage
  d'expliquer la cadence (une mineure tous les 6 mois, une majeure alignée sur Spring
  Framework) — information directement utile aux étudiants qui liront de la doc.

## Checklist d'alignement de `code/`

Le projet de référence doit être migré avant la rentrée, sinon les slides et le code
divergent (et les étudiants comparent).

- [ ] `kotlin("jvm")` et `kotlin("plugin.spring")` / `plugin.jpa` : `1.9.25` → **`2.2.x`+**
- [ ] `id("org.springframework.boot")` : `3.5.6` → **`4.1.x`**
- [ ] `spring-boot-starter-web` → **`spring-boot-starter-webmvc`**
- [ ] `com.fasterxml.jackson.module:jackson-module-kotlin` → **`tools.jackson.module:…`**
- [ ] `org.springframework.kafka:spring-kafka` → **`spring-boot-starter-kafka`**
- [ ] Ajouter `testImplementation("com.ninja-squad:springmockk:5.x")` — utilisé dans les
      slides depuis c1, absent du build
- [ ] `KafkaContainer(confluentinc/cp-kafka:7.4.0)` → `org.testcontainers.kafka.KafkaContainer`
      + image `apache/kafka` (voir [[2026/Cours 4]])
- [ ] `io.micrometer:micrometer-registry-prometheus` : vérifier le renommage éventuel des
      propriétés `management.*`
- [ ] Toolchain Java : 21 aujourd'hui, OK (≥ 17 exigé)
- [ ] Relancer `./gradlew build` et les tests Testcontainers après migration
- [ ] 🟡 `allOpen { … }` ne déclare que les annotations JPA (`Entity`,
      `MappedSuperclass`, `Embeddable`). C'est `kotlin("plugin.spring")` qui ouvre
      `@Component`/`@Transactional`/`@Cacheable` — bien vérifier qu'il reste actif, c'est ce
      qui fait fonctionner les exemples de c3 et c4 (cf. le manque « proxies » dans
      [[2026/Cours 3]]).
