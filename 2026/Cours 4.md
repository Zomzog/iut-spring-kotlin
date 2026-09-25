---
tags: [spring, kotlin, iut, relecture, cours/c4]
date: 2026-09-25
cible: spring-boot-4.1
slides: 64
---

← [[2026]]

# §5 — Cours 4 (64 slides)

`auto-configuration` → `flyway` → `caching` → `messaging` → `integration-testing` →
`observability` (qui inclut `logs`, `actuators`, `telemetry`) → `reactive`.

**La séance la plus légère** (64 slides), donc celle qui a de la marge pour absorber des
ajouts. C'est aussi celle qui a le moins de speaker notes.

## 🔴 Erreurs — `auto-configuration.md`

- [ ] **`auto-configuration.md:70`, `:82`, `:95`** — les classes d'auto-configuration sont
  annotées **`@Configuration`** alors qu'il faut **`@AutoConfiguration`** depuis Spring
  Boot 2.7/3.0. Vérifié dans la doc 4.1.1 : « Classes that implement auto-configuration are
  annotated with `@AutoConfiguration` ».
  C'est l'erreur la plus gênante du cours : elle est **sur la slide dont le sujet est
  précisément l'auto-configuration**. Et ça ouvre sur `@AutoConfigureBefore`/`@AutoConfigureAfter`,
  qui n'existent que sur `@AutoConfiguration`.
- [ ] **`auto-configuration.md:80`, `:93`** — package `bzh.zomzog.iut.amphi.autoconfig.autoconfig`
  (segment dupliqué), alors que le fichier `.imports` de `:113` déclare
  `bzh.zomzog.iut.amphi.autoconfig.MyAutoConfiguration`. Les deux ne peuvent pas être vrais
  en même temps — et c'est justement le lien que la slide doit faire comprendre.
- [ ] **`auto-configuration.md:55`** — lien markdown
  `[Voir aussi — Cours 2](/slides/pages/c2/config.md)` : chemin de fichier source dans un deck
  rendu → **lien mort**. Utiliser un numéro de slide Slidev, ou retirer.
- [ ] 🟡 **`auto-configuration.md:53`** — un `---` nu dans un `<div v-click>`. Il ne casse
  rien aujourd'hui parce qu'il est indenté, mais désindenté il couperait la slide en deux.
  → `<hr/>`.
- [ ] 🟡 `auto-configuration.md:144-149` — « **@Bean** quand ils utilisent les annotations
  `@Conditional...` / **@Primary** sinon » : formulation elliptique. L'idée juste est :
  déclarer son propre bean suffit **si** l'auto-configuration utilise
  `@ConditionalOnMissingBean` (le cas général) ; sinon il faut `@Primary`.

## 🔴 Erreurs — `flyway.md`

- [ ] **`flyway.md:235-238`** — dépendance Maven avec
  `<groupId>org.postgresql</groupId>` pour l'artefact `flyway-database-postgresql`.
  C'est **`org.flywaydb`** (le bloc Gradle juste au-dessus est correct).
- [ ] **`flyway.md:329-334`** — les **Undo migrations (`U<version>__…sql`) sont une
  fonctionnalité Flyway Teams/Enterprise**, payante, indisponible en édition Community. La
  slide les présente au même niveau que les migrations versionnées et répétables. Les
  étudiants vont écrire un `U1__…sql` et se demander pourquoi il est ignoré. → ajouter la
  mention, ou retirer la slide.
- [ ] ⚪ `flyway.md:349-369` — la slide est titrée « Exemple SQL
  (`V1__create_users_table.sql`) », le SQL crée une table **`pony`**, et la table
  d'historique affiche **`V1__init.sql`** avec la description `init`. Trois noms pour un seul
  exemple ; c'est la slide qui explique le lien nom de fichier ↔ historique, donc la
  cohérence compte.

## 🔴 Erreurs — `caching.md`

- [ ] **`caching.md:551`** — `key = "#root.methodName : #max"` : **`:` n'est pas un opérateur
  SpEL**. L'expression ne s'évalue pas.
  → `key = "#root.methodName + ':' + #max"`.
- [ ] **`caching.md:509-514`** — `@Cacheable("myCache")` sur une fonction présentée hors
  classe. `@Cacheable` exige un **bean** et un **proxy** : sur une fonction top-level Kotlin,
  ça ne peut pas fonctionner. Même problème que `@Transactional` en c3 → renvoyer
  explicitement au manque « proxies Kotlin » de [[2026/Cours 3]], **y compris
  l'auto-invocation** : `@Cacheable` appelé depuis la même classe ne met rien en cache.
- [ ] **`caching.md:681-688`** et **`:705-712`**, **`:733-740`** —
  `class CacheErrorHandlerConfig : CacheErrorHandler` n'implémente que
  `handleCacheGetError`. L'interface en déclare **quatre** (`handleCacheGetError`,
  `handleCachePutError`, `handleCacheEvictError`, `handleCacheClearError`), toutes abstraites.
  **Ne compile pas.**
- [ ] ⚪ `caching.md:634-637` — `@CachePut … fun updateUser(…): User { /* commentaire */ }` :
  type de retour non-`Unit` sans `return`.
- [ ] ⚪ `caching.md:550` — `fun chacheWithKey`.
- [ ] ⚪ Le nom `CacheErrorHandlerConfig` pour un handler (pas une `@Configuration`) prête à
  confusion.

## 🔴 Erreurs — `actuators.md`

- [ ] **`actuators.md:479-490`** — `val info = this.delegate.info()` puis
  `info["custom.value"] = "pony"`. `InfoEndpoint.info()` renvoie une **`Map` non modifiable**.
  Le code compile (types plateforme Java côté Kotlin) mais lève
  `UnsupportedOperationException` à l'exécution.
  → `val info = this.delegate.info().toMutableMap()`.

## 🔴 Erreurs — `messaging.md`

- [ ] **`messaging.md:169`** — `return DefaultErrorHandler(lbackoff)` : **`lbackoff` n'existe
  pas** (la variable s'appelle `backoff`).

## 🔴 Erreurs — `integration-testing.md`

- [ ] **`integration-testing.md:277`, `:301`, `:326`** — `@Container` sur un `val` dans un
  `companion object` **sans `@JvmStatic`**. L'extension JUnit de Testcontainers n'inspecte que
  les champs **statiques** : sans `@JvmStatic`, le conteneur **n'est jamais démarré**.
  Piège Kotlin classique, et il rend l'exemple entier non fonctionnel.
  → `@Container @JvmStatic val postgres = …`
- [ ] **`integration-testing.md:250`**, `:267`, `:290`, etc. —
  `userRepository.findById(user.id.toString())` : l'entité est créée avec `id = 2L`, donc le
  repository est un `JpaRepository<UserEntity, Long>`. Passer une `String` ne compile pas.
- [ ] **`integration-testing.md:251`** — **`assert(found.isPresent)`** : l'`assert` de Kotlin
  est **désactivé** sans `-ea` sur la JVM. Le test passe donc **toujours**, quoi qu'il arrive.
  C'est le pire cas possible dans un cours sur les tests, et le reste du programme utilise
  assertk (`assertThat(...)`) — à aligner.
- [ ] ⚪ `integration-testing.md:277` — `PostgreSQLContainer("postgres:18-alpine")` : le
  constructeur prenant une `String` est déprécié au profit de `DockerImageName.parse(...)`
  (forme que la slide Kafka utilise, d'ailleurs).

## 🔴 Erreurs — `reactive.md`

- [ ] **`reactive.md:495-501`** — trois problèmes sur une seule méthode :
  - `@GetMapping("/products/{productId}")` mais paramètre `@PathVariable id` : **les noms ne
    correspondent pas** → échec au démarrage (sans `@PathVariable("productId")`)
  - corps en bloc `{ … }` avec un type de retour `ProductStockDTO` et **aucun `return`** →
    ne compile pas
  - `productRepository.findById(id.id)` sur un `CrudRepository` renvoie un
    **`Optional<Product>`**, pas un nullable → `product?.let` est faux
  - accessoirement, `@PathVariable id: ProductId` sur un type métier exigerait un converter
- [ ] 🟡 **`reactive.md:520-534`** — étape intermédiaire où `val product = async { … }` est
  suivi de `product?.let` : un `Deferred` n'est pas nullable. Corrigé à l'étape suivante avec
  `.await()`, mais l'étape fausse **n'est pas marquée** (le cours utilise un 🚫 ailleurs).

## 🔴 Erreurs — `telemetry.md`

- [ ] ⚪ **`telemetry.md:779`** — la commande `java -javaagent:… -jar myapp.jar` est en
  **paragraphe**, pas dans un bloc de code : pas de coloration, et le rendu casse la
  lisibilité d'une ligne longue.

## 🟠 Obsolescence 4.1

- [ ] **`integration-testing.md:385`** — `KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))` :
  `org.testcontainers.containers.KafkaContainer` est déprécié, et l'image Confluent 7.4.0
  date de 2023. → `org.testcontainers.kafka.KafkaContainer` avec l'image `apache/kafka`.
  Même correction à porter dans `code/`.
- [ ] **`telemetry.md:697-698`** — `@Service class SpanDemoService(private val tracer: io.opentelemetry.api.trace.Tracer)` :
  l'injection directe d'un `Tracer` OpenTelemetry est douteuse (le starter expose un
  `OpenTelemetry`, dont on tire un `Tracer` via `getTracer(name)`). Surtout, l'API idiomatique
  côté Spring est **Micrometer Observation** — `ObservationRegistry`, `@Observed` — et
  `code/build.gradle.kts:41` utilise **déjà** `micrometer-tracing-bridge-otel`.
  → basculer l'exemple sur `@Observed` / `ObservationRegistry` : plus court, plus Spring, et
  ça marche.
- [ ] **`reactive.md`** — parler de réactif en 2026 sans mentionner les **threads virtuels**
  (`spring.threads.virtual.enabled=true`, Java 21+) donne une vision fausse du choix
  d'architecture : pour la majorité des applications, c'est aujourd'hui l'alternative
  pragmatique au passage en réactif. → voir la proposition d'extension ci-dessous.
- [ ] ⚪ `auto-configuration.md:41` — le chemin du `.imports` est **correct** et confirmé
  inchangé en 4.1 : c'est `c1/springboot.md:355` qu'il faut corriger (voir [[2026/Cours 1]]).

## 🔵 Manques

- [ ] **Sécuriser `/actuator`.** `actuators.md:397` dit « all endpoints **do not use in
  production** » sans jamais montrer **comment** protéger. Or la sécurité vient d'être vue en
  c3 : le lien est gratuit et important.
  → `management.server.port` (port de management séparé), ou une règle
  `authorize("/actuator/**", hasRole("ADMIN"))`. **1 slide**, et elle referme la boucle c3 → c4.
- [ ] **`HealthIndicator` et `InfoContributor` custom.** `actuators.md:458-492` montre
  `@EndpointWebExtension` — un mécanisme obscur, rarement utilisé, et dont l'exemple ne
  fonctionne pas (voir plus haut). Un `HealthIndicator` custom est plus simple, plus utile, et
  c'est ce que les étudiants auront réellement à écrire. → remplacer.
- [ ] **`management.tracing.sampling.probability`.** La valeur par défaut est **0.1** : sans
  la changer, les étudiants ne verront **aucune trace** dans Jaeger et concluront que leur
  configuration est fausse. C'est la première chose à mettre à 1.0 en développement.
  Absent des slides et du `code/application.yml` visible.
- [ ] **Logs structurés JSON**, natifs depuis Spring Boot 3.4 :
  `logging.structured.format.console=ecs` (ou `logstash`, `gelf`). Une ligne de configuration
  pour des logs exploitables par une stack d'agrégation — exactement le sujet du chapitre
  « production ready » de `actuators.md:336-342`. Aujourd'hui `logs.md` ne parle que de
  `logback.xml` et de Jansi (couleurs en dev).
- [ ] **MDC / correlation-id dans les logs.** Spring Boot ajoute automatiquement
  `logging.pattern.correlation` quand Micrometer Tracing est présent : le `traceId` apparaît
  dans chaque ligne de log. C'est **le** pont entre le chapitre logs et le chapitre telemetry,
  et le dépôt contient déjà un `CorrelationIdWebFilter.kt`.
- [ ] **SLF4J et les placeholders `{}`.** `logs.md:218` montre
  `logger.trace("trace of ${name}")` — l'interpolation est évaluée **même si TRACE est
  désactivé**. C'est précisément la raison d'être de l'API lambda de `kotlin-logging`
  présentée juste après (`logs.md:251`), mais le lien n'est jamais fait. Deux lignes de note
  transforment une slide descriptive en slide qui enseigne quelque chose.
- [ ] **Flyway vs `spring.jpa.hibernate.ddl-auto`.** c2 laisse Hibernate générer le schéma ;
  c4 introduit Flyway. Les deux ensemble se marchent dessus. La transition doit être
  explicite : avec Flyway, on passe `ddl-auto` à `validate` (ou `none`).
  `flyway.md:206-210` effleure le sujet (« préférable au DDL auto en production ») sans
  donner la propriété.
- [ ] **Caffeine par propriétés.** `caching.md:459-503` construit un `CacheManager` à la main
  sur 4 étapes, alors que Spring Boot auto-configure Caffeine depuis
  `spring.cache.caffeine.spec=maximumSize=500,expireAfterWrite=10m`. Montrer la version
  propriétés **d'abord** (c'est un cours sur Spring **Boot**), puis le bean manuel pour les
  cas complexes.
- [ ] **Dépendances Testcontainers jamais montrées.** `integration-testing.md` utilise
  `@Testcontainers`, `@Container`, `@ServiceConnection`, `PostgreSQLContainer`,
  `KafkaContainer` sans une seule slide de dépendances, alors que tous les autres chapitres
  en ont une. Il en faut 4 : `spring-boot-testcontainers`,
  `org.testcontainers:junit-jupiter`, `:postgresql`, `:kafka` (toutes présentes dans
  `code/build.gradle.kts:49-53`).
- [ ] **`@SpringBootTest(webEnvironment = RANDOM_PORT)` + `RestTestClient`** : le test
  bout-en-bout sur un vrai serveur. Le cours couvre le test de couche (c1), le test de
  repository (c2/c4), mais jamais le test complet.
- [ ] **Sérialisation JSON avec Kafka.** `messaging.md:54-88` ne configure que des
  `StringSerializer`. En pratique on envoie des objets → `JsonSerializer`/`JsonDeserializer`
  et le piège `spring.json.trusted.packages`. Également : `KafkaTemplate.send` renvoie un
  `CompletableFuture` (depuis Boot 3), que `messaging.md:111` ignore silencieusement.
- [ ] **`reactive.md` : on ne peut pas utiliser JPA/JDBC avec WebFlux.** C'est la raison
  d'être de R2DBC, qui apparaît en `reactive.md:477` sans explication. Sans cette phrase, les
  deux slides de ce chapitre sont incompréhensibles.
- [ ] ⚪ `caching.md` : `@Caching`, `condition`/`unless`, et le fait qu'un retour `null` est
  mis en cache. Et les métriques de cache exposées via Actuator — lien naturel avec la
  section suivante.

## 🟡 Notes orateur manquantes

c4 est la séance la moins annotée, et celle qui contient le plus de configuration à commenter.

- [ ] **`caching.md` — aucune note sur les 7 slides du fichier.** Y compris la slide
  d'invalidation (`:584-639`) où la citation de Phil Karlton appelle manifestement un
  commentaire, et la slide `CacheErrorHandler` (`:641-742`) qui est la plus technique du
  chapitre.
- [ ] **`integration-testing.md` — aucune note sur les 5 slides.** Il faudrait au minimum :
  ce que fait `@ServiceConnection` (la note de `:346` existe, bien), pourquoi `@JvmStatic` est
  nécessaire, et l'interaction `@DataJpaTest` / `@AutoConfigureTestDatabase` (voir
  [[2026/À vérifier]]).
- [ ] **`auto-configuration.md` — aucune note sur les 5 slides**, alors que le sujet est
  précisément un mécanisme invisible.
- [ ] **`flyway.md` — aucune note sur les 8 slides**, y compris `repair` / `baseline`
  (`:373-385`) qui sont des opérations délicates.
- [ ] `logs.md:256-293` — la slide `logback**-spring**.xml` fait apparaître le suffixe par un
  `v-if` malin… mais **aucune note n'explique pourquoi** il est nécessaire (les balises
  `<springProfile>` et `<springProperty>` ne fonctionnent que dans `logback-spring.xml`, parce
  que `logback.xml` est chargé trop tôt par Logback lui-même). C'est l'information utile de la
  slide, et elle est absente.
- [ ] `logs.md:295-323` — alternative `application.yml` / variables d'environnement : aucune
  note
- [ ] `actuators.md:380-416` — configuration des endpoints : aucune note
- [ ] `actuators.md:418-451` — le JSON de `/actuator/health` : aucune note, alors qu'il y a
  beaucoup à dire (composants, `show-details`, agrégation du statut global)
- [ ] `actuators.md:453-492` — customisation : aucune note
- [ ] `actuators.md:494-609` — Metrics, Prometheus, métriques custom : aucune note sur
  l'ensemble du bloc
- [ ] `telemetry.md` — les 9 slides sont sans note ; le contenu est en listes à puces
  auto-portantes, mais `:683-723` (exemple de span manuel) et `:770-786` (agent Java) en
  auraient besoin
- [ ] `reactive.md` — les 2 slides sont **sans aucune note**, pour le sujet le plus dense du
  cours
- [ ] `messaging.md:6-32` — slide d'introduction + dépendance : note vide (le commentaire est
  piégé dans un `<div>`, voir [[2026/Structurel]])
- [ ] `messaging.md:152-199` — DLQ / `DeadLetterPublishingRecoverer` : note présente mais
  d'une ligne, pour un sujet qui mérite plus (pourquoi une DLQ, que faire des messages dedans)

> [!tip] Arbitrages proposés — c4 a de la marge (64 slides contre 89 pour c1)
> **À remplacer plutôt qu'à ajouter :**
> - `actuators.md:453-492` (`@EndpointWebExtension`, 1 slide, exemple cassé) →
>   `HealthIndicator` custom. Même volume, bien plus utile.
> - `caching.md:459-503` : inverser l'ordre — `spring.cache.caffeine.spec` d'abord, bean
>   manuel ensuite. Même volume.
>
> **À ajouter (le budget existe) :**
> - 1 slide « sécuriser /actuator » — referme la boucle avec c3
> - 1 slide « logs structurés JSON + correlation-id » — referme la boucle logs ↔ telemetry
> - 1 slide dépendances Testcontainers — aligne le chapitre sur tous les autres
> - 1 ligne `management.tracing.sampling.probability` sur la slide de config OTel
> - 1 ligne `ddl-auto: validate` sur la slide de config Flyway
>
> **`reactive.md` : trancher.** 2 slides pour WebFlux + R2DBC + coroutines + `async`, c'est
> trop peu et ça produit du code qui ne compile pas. Deux options honnêtes :
> - **le réduire** à 1 slide franchement présentée comme une ouverture (« ça existe, voilà à
>   quoi ça ressemble, on ne le fait pas ici »), en corrigeant le code ; ou
> - **le porter à ~6 slides** : pourquoi (le schéma `sync.png`/`async.png` de
>   `c3/rest-client.md:256-291` est déjà fait et peut être réutilisé), pourquoi JPA est exclu,
>   R2DBC, `suspend` et `Flow`, puis **les threads virtuels comme alternative**.
> La seconde option est plus utile et c4 a la place ; la première est acceptable si le temps
> manque. L'état actuel n'est ni l'un ni l'autre.
