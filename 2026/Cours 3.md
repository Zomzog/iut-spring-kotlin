---
tags: [spring, kotlin, iut, relecture, cours/c3]
date: 2026-09-25
cible: spring-boot-4.1
slides: 68
---

← [[2026]]

# §4 — Cours 3 (68 slides)

`di-reminder` → `jpa3` → `transactional` → `rest-client` → `filters` → `security`.

C'est le cours qui contient **le plus de code faux** (transactions et sécurité) et le
**manque le plus structurant** du programme (les proxies Kotlin).

> [!danger] Le manque le plus rentable de tout le cours : les proxies Spring en Kotlin
> `@Transactional` (ici) et `@Cacheable`
> ([[slides/pages/c4/caching|c4/caching.md]]) reposent tous deux sur un **proxy**. En Kotlin
> cela impose deux choses que rien n'enseigne :
>
> 1. **Les classes Kotlin sont `final` par défaut.** Spring ne peut pas les proxifier par
>    CGLIB. C'est `kotlin("plugin.spring")` — présent dans `code/build.gradle.kts:3`, jamais
>    mentionné dans les slides — qui ouvre les classes annotées `@Component`,
>    `@Transactional`, `@Async`, `@Cacheable`. Sans lui, rien de ce chapitre ne fonctionne.
> 2. **L'auto-invocation ne passe pas par le proxy.** Un appel `this.methodeTransactionnelle()`
>    depuis la même classe **ignore silencieusement** `@Transactional`. Pas d'erreur, pas de
>    log : la transaction n'existe simplement pas. C'est le bug le plus fréquent et le plus
>    difficile à diagnostiquer sur ce sujet.
>
> Le cours explique pourtant très bien le proxy CGLIB en
> `[[slides/pages/c1/di|c1/di.md:368-408]]` (avec la stack de breakpoint) : la matière est
> là, il manque le lien avec `@Transactional`.
>
> - [ ] 🔵 **2 slides** : « pourquoi `kotlin("plugin.spring")` » + « pourquoi
>   `this.maMethode()` ne déclenche pas la transaction », à placer juste après
>   `transactional.md:92-161`.

## 🔴 Erreurs — `transactional.md`

- [ ] **`transactional.md:144-158`** — le pseudo-code du `DataSourceTransactionManager`
  déclare `val rollback = false` puis fait `rollback = true`. **Un `val` n'est pas
  réassignable** : ne compile pas. → `var`.
- [ ] **`transactional.md:240`, `:248`, `:259`, `:273`, `:290`, `:307`** —
  `fun publish() = { TODO() }` : en Kotlin, `= { … }` renvoie **un lambda**, la fonction ne
  fait rien. `TODO()` n'est jamais exécuté. **5 occurrences** sur le magic-move. → `fun publish() { TODO() }`
  ou `fun publish() = TODO()`.
  Erreur d'autant plus fâcheuse qu'elle est exactement du type que les étudiants reproduisent.
- [ ] **`transactional.md:390`** et **`:416`** — `fun do()` : **`do` est un mot-clé réservé**
  en Kotlin. Ne compile pas sans backticks. → renommer (`fun process()`).
- [ ] **`transactional.md:392`** et **`:418`** — `u.wishes()` alors que `wishes` est une
  **propriété** (déclarée `var wishes: MutableList<Product>` en `:351`). → `u.wishes`.
  Sur la slide qui démontre `LazyInitializationException`, c'est le geste précis qu'il faut
  montrer juste.
- [ ] **`transactional.md:60-71`** — les étapes 4 et 5 du magic-move sont **strictement
  identiques** : un clic qui ne produit rien. Supprimer l'une des deux.
- [ ] **`transactional.md:437-442`** — « Par défaut, Spring ouvre une **transaction** à chaque
  requête HTTP s'il y a la dépendance JPA ». **Imprécis et trompeur** : l'*Open Session In
  View* garde ouverte une **`Session`/`EntityManager`** pour la durée de la requête, il
  n'ouvre **pas** de transaction. C'est justement pour ça que le lazy loading fonctionne hors
  transaction et que le comportement est déroutant. Le nom de propriété
  (`spring.jpa.open-in-view`) est correct, et mentionner que Spring Boot loggue un warning au
  démarrage serait un bon ajout.
- [ ] **`transactional.md:219-227`** — bloc annoté ` ```java ` contenant du Kotlin
  (`fun updateData()`). Coloration syntaxique fausse.
- [ ] 🟡 **`transactional.md:323-330`** — « Une erreur dans une sous-transaction rollback la
  parent. Une erreur dans la parent ne rollback pas une sous transaction ». L'affirmation
  mélange deux situations différentes :
  - **même** `transactionManager` + propagation `REQUIRED` : il n'y a **qu'une** transaction,
    l'exception marque l'ensemble en rollback-only
  - **deux** managers distincts (le cas de l'exemple : Kafka + DB) : deux transactions
    indépendantes, et le commit Kafka est déjà parti — ce que dit très bien le commentaire de
    `:317`
  Reformuler en distinguant les deux cas.
- [ ] ⚪ `transactional.md:354`, `:371` — `jpaRepo.findOne()` n'existe pas sur
  `JpaRepository` (c'est `findById`). `data class User` sans `@Entity`.

## 🔴 Erreurs — `security.md`

- [ ] **`security.md:186-202`** — « Cette partie ajoute une **JSP** pour pouvoir
  s'authentifier ». **Faux** : la page de login par défaut est générée en HTML par
  `DefaultLoginPageGeneratingFilter`, il n'y a aucune JSP (ni moteur de vue) dans
  Spring Security. Dire « une page HTML générée ».
- [ ] **`security.md:265-275`** — l'ordre des règles est faux et enseigne le contraire de ce
  qu'il faut :
  ```
  authorize("/ponies", permitAll)
  authorize(HttpMethod.GET, "/**", permitAll)   // ← attrape TOUT en GET
  authorize("/admin", hasRole("ADMIN"))          // ← mort pour les GET
  authorize(anyRequest, authenticated)
  ```
  **La première règle qui matche gagne.** `GET /**` permitAll rend la règle `ADMIN`
  inopérante en lecture : `/admin` est en accès libre. Mettre les règles **du plus spécifique
  au plus général**, et le dire explicitement — c'est LE principe de
  `authorizeHttpRequests`.
- [ ] **`security.md:395-414`** — `JdbcUserDetailsManager` : il manque **l'accolade de
  fermeture de la classe**. Le snippet ne compile pas.
- [ ] 🟡 **`security.md:403-412`** — `JdbcUserDetailsManager(dataSource).apply { createUser(user1) }`
  dans un `@Bean` : l'utilisateur est créé **à chaque démarrage** → violation de clé primaire
  au second lancement. Sur une slide titrée « Version prod », c'est à encadrer d'un
  avertissement (ou à conditionner à un profil / `userExists()`).
- [ ] **`security.md:455-460`** — `fun admin(principal: Principal): ResponseEntity<String> { println(…) }` :
  type de retour déclaré, aucun `return`. Ne compile pas.
- [ ] **`security.md:468-472`** —
  `SecurityContextHolder.getContext().authentication.principal.let { println("Login: ${principal.name}") }` :
  la lambda utilise `principal`, qui **n'existe pas dans cette portée** (c'est `it`). Ne
  compile pas.
- [ ] **`security.md:362-372`** — les deux utilisateurs in-memory reçoivent `.roles("ADMIN")`.
  Le second (`login`/`password`) devrait être `USER`, sinon les tests `@WithMockUser` des
  slides suivantes (403 attendu pour un non-admin) n'ont plus de sens.
- [ ] **`security.md:12-18`** — slide « ## Vocabulaire » **entièrement vide** (titre seul).
- [ ] ⚪ `security.md:491` — `@Import(MySecurityFilterConfig::class)` alors que la classe
  s'appelle `MySecurityConfig` partout ailleurs.
- [ ] ⚪ `security.md:499-502` — « happy path » sur `/openEndpoint` qui attend
  `isIAmATeapot()` : la méthode existe bien dans le DSL, mais l'exemple est gratuitement
  déroutant.
- [ ] 🟡 `security.md:518-529` — `@WithAnonymousUser` + attente d'un **401**. Avec
  `formLogin { }` **et** `httpBasic { }` tous deux activés (ce que fait
  `security.md:151-172`), l'entry point par défaut est celui de `formLogin` → **302 vers
  `/login`**, pas 401. Le test tel qu'écrit devrait échouer.
  → à confirmer en exécutant, voir [[2026/À vérifier]].
- [ ] 🟡 `security.md:60-66` — « **UserDetail** : API Spring pour faire la phase
  d'authentification ». Les types s'appellent `UserDetails` et `UserDetailsService`, et
  `UserDetails` décrit un utilisateur — ce n'est pas l'API d'authentification.

## 🔴 Erreurs — `rest-client.md`

- [ ] **`rest-client.md:198-201`** — `if (statusCode.is5xxServerError) throw HttpClientErrorException(…)` :
  c'est **`HttpServerErrorException`** pour une 5xx. Lever une exception « client » sur une
  erreur serveur inverse la sémantique.
- [ ] **`rest-client.md:320-336`** — l'ordre des `onStatus` est faux :
  ```
  .onStatus({ it.isError }, { Mono.error(RuntimeException("Error")) })   // ← matche tout
  .onStatus({ it.value() == 404 }, { Mono.error(NotFoundException()) }) // ← jamais atteint
  ```
  `isError` couvre 4xx **et** 5xx, donc le handler 404 est mort. **Même classe d'erreur que
  `authorizeHttpRequests` ci-dessus** : deux fois dans le même cours, le cas spécifique est
  placé après le cas général. Bonne occasion d'en faire un principe transverse énoncé une
  fois.
- [ ] ⚪ `rest-client.md:370-386` — `val result: PonyDto = … .block()` : `block()` renvoie un
  type **nullable**, l'affectation à un `PonyDto` non-null ne compile pas.
- [ ] ⚪ `rest-client.md:304` — `fun webCleint`.
- [ ] ⚪ `rest-client.md:15` — « Client récent JDK 11 (2018) » : 8 ans, « récent » a vieilli.

## 🔴 Erreurs — `jpa3.md`

- [ ] **`jpa3.md:232-240`** — `@JoinColumn(name = "phone_id") val phone: PhoneEntity`
  **sans `@OneToOne`**. Sans annotation de relation, Hibernate ne mappe pas l'association.
  La plage surlignée (`{all|6-7,13-14}`) montre bien que deux lignes étaient attendues : le
  `@OneToOne` a été perdu.
- [ ] 🟡 **`jpa3.md` utilise `data class` pour toutes les entités**, alors que
  `[[slides/pages/c2/jpa2|c2/jpa2.md]]` utilise `class`. Deux problèmes :
  - **incohérence** entre deux cours sur le même sujet, à une semaine d'intervalle
  - c'est la version `data class` qui est **problématique** : `equals`/`hashCode`/`toString`
    portent sur tous les champs, donc récursion infinie sur une relation bidirectionnelle
    (`UserEntity.phone` ↔ `PhoneEntity.user`, littéralement le cas de `jpa3.md:232-248`)
  Trancher pour `class`, et faire du « pourquoi pas `data class` » une slide (voir manque
  correspondant dans [[2026/Cours 2]]).
- [ ] 🟡 Les entités bidirectionnelles de `jpa3.md` ont des deux côtés non-nullables et sans
  valeur par défaut (`val user: UserEntity`, `val phones: List<PhoneEntity>`) → mêmes
  problèmes de construction que `jpa2.md:409-427`.
- [ ] ⚪ `jpa3.md:429` — plage de surlignage `{all|9,11-131}` : `131` au lieu de `13`.

## 🔴 Erreurs — `di-reminder.md`

- [ ] **`di-reminder.md:98`** — `spring-configuration-processor` : l'artefact s'appelle
  **`spring-boot-configuration-processor`** (c'est bien ce qu'utilise
  `code/build.gradle.kts:46`).

## 🟠 Obsolescence 4.1

- [ ] **`rest-client.md:437-444`** — la construction manuelle du proxy
  (`RestClientAdapter.create` + `HttpServiceProxyFactory.builderFor(...)` + `createClient`)
  est remplacée en Spring Boot 4 par **`@ImportHttpServices`** :
  ```kotlin
  @SpringBootApplication
  @ImportHttpServices(group = "ponies", basePackages = ["bzh.zomzog.clients"])
  class App
  ```
  ```yaml
  spring.http.serviceclient.ponies.base-url: http://localhost:8080
  spring.http.serviceclient.ponies.read-timeout: 2s
  ```
  Gain pédagogique réel : on passe de 5 lignes de plomberie à une annotation + de la config,
  et ça introduit au passage les **timeouts**.
- [ ] **`rest-client.md:79-115`** — l'ordre du chapitre est à inverser. Aujourd'hui :
  RestTemplate (2 slides + gestion d'erreur détaillée), WebClient (3 slides), RestClient
  (1 slide, 8 lignes). La doc Boot 4 dit de RestTemplate : « Legacy option ; use only for
  existing code you don't want to migrate », et recommande **RestClient** pour tout code
  impératif neuf.
  → **RestClient d'abord et en détail**, RestTemplate réduit à une slide « ce que vous
  trouverez dans le code existant », WebClient positionné comme le client **réactif**
  (ce qui justifie le détour WebFlux de `rest-client.md:256-291`).
- [ ] **`security.md:100-103`** — `@EnableWebSecurity` n'est **pas nécessaire** avec Spring
  Boot (auto-configuré dès que le starter est présent). À garder éventuellement comme
  explication, mais en disant que Boot le fait pour vous.
- [ ] **`security.md:123`, `:139`, `:159`** — `open fun filterChain(...)` : le `open` est
  inutile (`kotlin("plugin.spring")` ouvre déjà les `@Configuration`) et **incohérent** avec
  tous les autres `@Bean` du cours, qui ne le portent pas.
- [ ] 🔵 **Spring Security 7** (embarqué par Boot 4) : `AntPathRequestMatcher` et
  `MvcRequestMatcher` sont **supprimés**, `PathPatternRequestMatcher` est le défaut. Le DSL
  Kotlin `authorize("/ponies", permitAll)` continue de fonctionner, donc rien à corriger —
  mais deux conséquences valent une phrase :
  - **le match du slash final disparaît** : une règle sur `/admin` ne couvre plus `/admin/`
  - sous un servlet path, les patterns doivent inclure le préfixe
- [ ] ⚪ `rest-client.md:254` — récupérer `WebClient` via
  `spring-boot-starter-webflux` reste correct, mais préciser qu'on n'embarque pas pour autant
  un serveur réactif.

## 🔵 Manques

- [ ] **Proxies Kotlin / auto-invocation** → encadré en tête de note. Le plus important.
- [ ] **Le problème N+1 et ses remèdes.** `transactional.md:338-374` enseigne LAZY vs EAGER
  sans jamais nommer le N+1 ni montrer **`@EntityGraph`** ou `join fetch`. On donne le
  dilemme sans la sortie : LAZY → `LazyInitializationException`, EAGER → tout charger. La
  vraie réponse (charger explicitement ce dont on a besoin) manque. **1 slide.**
- [ ] **`@Transactional(readOnly = true)`** : bonne pratique standard sur les lectures
  (optimisation Hibernate, intention documentée), jamais citée alors que la slide
  `transactional.md:168-210` énumère `transactionManager`, `rollbackFor`, `timeout`,
  `propagation`, `isolation`.
- [ ] **`OncePerRequestFilter`** : c'est la classe de base **recommandée** par Spring pour
  écrire un filtre (gère les dispatches async et les forwards, qui sinon exécutent le filtre
  plusieurs fois). `filters.md` enseigne `Filter` puis `HttpFilter`, et s'arrête juste avant.
  Le filtre de log de `filters.md:630-640` est précisément un cas où ça compte.
- [ ] **`HandlerInterceptor` vs `Filter`** : quand utiliser l'un plutôt que l'autre (avant/
  après le `DispatcherServlet`, accès au handler). Le schéma de `filters.md:540-546` est déjà
  en place pour le dire.
- [ ] **`@AuthenticationPrincipal`** : bien plus idiomatique que `Principal`
  (`security.md:455`) ou `SecurityContextHolder` (`security.md:468`), et permet de récupérer
  directement son propre type d'utilisateur.
- [ ] **API stateless** : `sessionManagement { sessionCreationPolicy = STATELESS }`. Le cours
  active `formLogin` (donc une session + `JSESSIONID`) alors que tout le reste du programme
  construit des **API REST**. C'est cette incohérence qui rend confuse la slide CSRF
  (voir ci-dessous). Et au moins une mention de **JWT / `oauth2-resource-server`** : c'est ce
  que les étudiants rencontreront en stage.
- [ ] **Tester un client HTTP** : `@RestClientTest` / `MockRestServiceServer` (ou WireMock).
  Le cours teste les contrôleurs (c1), les repositories (c2), la sécurité (c3) — mais jamais
  le code qui **appelle** un service externe, alors qu'un chapitre entier y est consacré.
- [ ] **Timeouts sur les appels sortants** : `spring.http.clients.connect-timeout` /
  `read-timeout`. Première cause d'incident en production, zéro slide.
- [ ] **La dépendance `kotlin-logging` n'est jamais déclarée** alors que
  `filters.md:632` utilise `KotlinLogging.logger {}` (et `c4/caching.md:683` aussi). Elle est
  introduite en `[[slides/pages/c4/logs|c4/logs.md:230-254]]`, soit **une séance après** son
  premier usage.
- [ ] 🔵 Une phrase de transition entre `filters.md` et `security.md` : « Spring Security,
  c'est cette chaîne de filtres » — le schéma de `filters.md:540` vient d'être posé, le lien
  est gratuit et très éclairant.

## 🟡 Mal expliqué

- [ ] **`security.md:236-254`** — CSRF et CORS présentés côte à côte comme « des sécurités
  qu'on peut désactiver », avec un ⚠️ « Ne pas faire en PROD sauf cas particuliers ». Deux
  problèmes :
  - **CORS n'est pas une sécurité qu'on désactive.** `cors { disable() }` désactive
    l'*intégration CORS de Spring Security*, ce qui rend le comportement **plus** restrictif,
    pas moins. Amalgame conceptuel.
  - **Pour une API REST stateless, désactiver le CSRF *est* la pratique normale** (pas de
    cookie de session → pas de vecteur CSRF). L'avertissement induit donc en erreur pour
    exactement le type d'application que le cours construit.
  Reformuler : CSRF = protection utile **quand on utilise des cookies de session**, inutile
  sur une API à jeton ; CORS = mécanisme navigateur à **configurer**, pas à désactiver.
- [ ] 🟡 `transactional.md:196-199` — la liste des propagations ne donne que `REQUIRED` et
  `NESTED`. `REQUIRES_NEW` est le cas d'usage réel le plus fréquent (journaliser malgré un
  rollback) et manque.

## 🟡 Notes orateur manquantes

- [ ] `jpa3.md` — les 14 slides ont **toutes** une note d'une ligne : correct, mais elles
  décrivent le schéma sans dire **quand choisir** telle cartographie. C'est la question que
  les étudiants posent.
- [ ] `transactional.md:163-227` — personnalisation de `@Transactional` : aucune note
- [ ] `transactional.md:376-421` — les deux slides `LazyInitializationException` : aucune
  note, alors que c'est le passage le plus subtil du cours
- [ ] `transactional.md:423-463` — TL;DR : aucune note
- [ ] `rest-client.md:117-170` — RestTemplate + création du bean : aucune note
- [ ] `rest-client.md:171-245` — gestion d'erreur (magic-move de 4 étapes) : aucune note
- [ ] `rest-client.md:292-388` — WebClient (magic-move de 6 étapes, `retryWhen`, `block()`) :
  aucune note, et c'est là que se trouve le bug d'ordre des `onStatus`
- [ ] `rest-client.md:392-502` — RestClient et Http Interface : aucune note
- [ ] `security.md:12-66` — vocabulaire, authentication, authorization : aucune note sur les
  4 slides
- [ ] `security.md:291-318` — `@EnableMethodSecurity` / `@PreAuthorize` : note très courte
- [ ] `security.md:320-337` — Password Encoder : la note dit l'essentiel mais rien sur
  **pourquoi BCrypt** ni sur `DelegatingPasswordEncoder` (le défaut de Spring, avec ses
  préfixes `{bcrypt}`) — que les étudiants verront dans les colonnes de leur base
- [ ] `security.md:513-565` — les 3 slides `@WithMockUser`/`@WithAnonymousUser` : aucune note

## 🟡 Ordre

- [ ] **`transactional.md:238-320`** utilise **Kafka** comme exemple de second
  `transactionManager` (`@Transactional("kafkaTransaction")`), alors que Kafka n'est
  introduit qu'en `[[slides/pages/c4/messaging|c4/messaging.md]]`, la séance suivante.
  Les étudiants ne savent pas ce qu'est un producteur Kafka à ce stade.
  → Prendre un exemple déjà connu : deux datasources, ou simplement « DB + envoi d'un mail ».
  Puis, en c4, ajouter une remarque « souvenez-vous du chaînage de transactions » quand Kafka
  arrive. Le raisonnement (le commit du premier système est déjà parti) reste identique et
  devient compréhensible.
