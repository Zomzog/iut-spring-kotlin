---
tags: [spring, kotlin, iut, relecture, cours/c1]
date: 2026-09-25
cible: spring-boot-4.1
slides: 89
---

← [[2026]]

# §2 — Cours 1 (89 slides)

`jee` → `history` → `di` → `springboot` → `mvc` → `test`.

**La séance la plus chargée des quatre** (89 slides contre 64 pour c4), et celle qui contient
le plus de contenu culturel compressible. `di.md` pèse à lui seul 30 slides.

## 🔴 Erreurs

### `di.md` — injection de dépendances

- [ ] **C1-01** · **`di.md:190-215`** — l'interface déclare `fun findAll(): List<Something>` mais
  `AService` appelle `db.findAllInDb()`. **Ne compile pas.** La slide précédente
  (`di.md:106-148`) est cohérente, elle : c'est le doublon qui a dérivé.
  → cette slide est de toute façon proposée à la coupe (voir plus bas).
- [ ] **C1-02** · **`di.md:431-441`** — le pseudo-code du proxy CGLIB accumule trois problèmes sur
  10 lignes, alors que c'est la slide qui doit faire comprendre le mécanisme :
  - `val myDb: PostgresDb? = null` puis `myDb ?: base.postgresDb()` → un `val` ne peut pas
    mémoriser le résultat ; il faut `var` pour que le cache ait un sens, c'est **tout le
    point** de la slide
  - `base.postgresDb()` alors que la méthode s'appelle `myDb()`
  - `base.Other(myDb())` alors que la méthode s'appelle `another()`
  - `val aService: aService? = null` : le type est écrit en minuscule (nom d'instance)
  Le `// /!\ pseudo code` excuse l'approximation, pas l'incohérence avec la slide de gauche.
- [ ] **C1-03** · **`di.md:1200-1209`** — deux fonctions `fun nomDuBean()` dans la **même classe** :
  *conflicting overloads*, ne compile pas. L'intention est de montrer `@Bean("autreNom")` ;
  il faut deux noms de méthode différents, sinon la slide démontre l'inverse de ce qu'elle
  veut dire (que le nom du bean est découplé du nom de la méthode).
- [ ] **C1-04** · **`di.md:903-907`** — la note orateur affirme : « Les 4 sont équivalent, ils sont plus
  sémantique pour de la documentation ». **C'est faux**, et c'est le genre d'affirmation qu'un
  étudiant retient :
  - `@Repository` déclenche la **traduction des exceptions de persistance**
    (`PersistenceExceptionTranslationPostProcessor`) — comportement réel, pas décoratif
  - `@Controller` est ce que **Spring MVC détecte** pour le mapping des requêtes ; un
    `@Component` avec des `@GetMapping` ne sert aucune route
  - Seul `@Service` est effectivement un pur alias sémantique
  Reformuler : « trois spécialisations, dont deux ont un comportement propre ».
- [ ] **C1-05** · ⚪ `di.md:894` — « 3 alias pour le **DDD** » : la doc Spring parle de stéréotypes d'une
  architecture en couches, pas de DDD. Terme à retirer, il embrouille plus qu'il n'aide.

### `mvc.md`

- [ ] **C1-06** · **`mvc.md:268`** — `"Hello $person.name"` : Kotlin interpole `person` puis concatène
  le littéral `.name`. Affiche `Hello PersonDTO(name=John, age=42).name`. Il faut
  `"Hello ${person.name}"`.
  C'est **le** piège d'interpolation Kotlin, sur la slide qui introduit la désérialisation :
  à corriger, et peut-être même à exploiter comme question posée à la salle.

### `test.md`

- [ ] **C1-07** · **`test.md:186-190`** et **`test.md:307-311`** — `class DummyService() { fun callDep(pony: String) = dependency.call() }` :
  **`dependency` n'est déclaré nulle part**. Ne compile pas, et surtout : tout l'exercice
  consiste à mocker cette dépendance, donc la slide doit montrer
  `class DummyService(val dependency: Dependency)`. Sans ça, l'étudiant ne voit pas *par où*
  le mock entre.
- [ ] **C1-08** · **`test.md:205,223,243`** et **`test.md:326,344,364,384`** — `every { dependency.call() } returns true`
  puis `assertThat(result).isEqualTo("good")` : le mock renvoie un `Boolean`, l'assertion
  attend une `String`. Incohérent de bout en bout sur les deux magic-move.
- [ ] **C1-09** · **`test.md:270`** — `every { dependency.call(Pony("name") } returns "23"` : **parenthèse
  fermante manquante**. Et le `returns "23"` détonne avec les `returns true` des autres
  exemples.
- [ ] **C1-10** · **`test.md:422`** et **`test.md:525`** — `fun post()` et `fun get()` **sans `@Test`**.
  Les étudiants copient ces exemples MockMvc tels quels, et le test ne s'exécute jamais :
  vert au premier essai, aucune couverture. C'est le bug le plus coûteux du chapitre.
- [ ] **C1-11** · **`test.md:625`** — `every { demoRepository.save(any()) } returns Unit` dans un test
  qui fait un **GET**. Mocker un `save` pour tester une lecture n'a pas de sens ; et
  `returns Unit` pour un `save` de repository est faux (il renvoie l'entité).
- [ ] **C1-12** · 🟡 **`test.md:611-648`** — le `@WebMvcTest` mocke un bean nommé `Repository`, ce qui
  cumule deux problèmes : `Repository` est le nom d'une interface marqueur Spring
  (collision de vocabulaire), et **les repositories n'existent pas encore** à ce stade du
  cours (JPA arrive en c2). Utiliser un `DemoService` — c'est aussi la couche qu'on mocke
  réellement sous un contrôleur.

### `springboot.md`

- [ ] **C1-13** · **`springboot.md:160-172`** — le chemin du fichier d'auto-configuration omet le dossier
  `spring/` : `META-INF/org.springframework…imports` au lieu de
  `META-INF/spring/org.springframework…imports`. **Contredit
  `[[slides/pages/c4/auto-configuration|c4/auto-configuration.md:41,112]]`, qui est juste.**
- [ ] **C1-14** · ⚪ `springboot.md:37-38`, `:82-83`, `:101-102` — `fun main(args: Array<String>) { runApplication<…>(*args)`
  sans `}` de fermeture, sur trois slides.

## 🟠 Obsolescence 4.1

Voir [[2026/Migration 4.1]] pour le détail. Ce qui touche c1 :

- [ ] **C1-15** · `mvc.md:13,19,25` et `springboot.md:155` — `spring-boot-starter-web` →
  **`spring-boot-starter-webmvc`** (titre de slide inclus)
- [ ] **C1-16** · `test.md:334,352,371` — `@SpykBean` → **`@MockkSpyBean`** (springmockk 5.x)
- [ ] **C1-17** · `test.md:438,455,475` — `ObjectMapper` passe sous `tools.jackson` (Jackson 3), et
  gagnerait surtout à être **injecté** plutôt qu'instancié
- [ ] **C1-18** · `history.md:59-81` — ajouter Spring Boot 4.1 (2026-06) à la frise
- [ ] **C1-19** · 🔵 Mentionner `MockMvcTester` / `RestTestClient` en fin de chapitre test : `MockMvc` et
  son DSL Kotlin restent valides, ce sont des alternatives

## 🔵 Manques

- [ ] **C1-20** · **Les plugins Gradle Kotlin ne sont jamais expliqués.** `code/build.gradle.kts` utilise
  `kotlin("plugin.spring")`, `kotlin("plugin.jpa")` et un bloc `allOpen`, sans qu'aucune
  slide n'en parle. Or :
  - `plugin.spring` ouvre les classes `@Component`/`@Configuration`/`@Transactional` — sans
    lui, **rien de ce qui est enseigné en c3 et c4 ne fonctionne** (les classes Kotlin sont
    `final`, Spring ne peut pas les proxifier)
  - `plugin.jpa` génère le constructeur sans argument exigé par JPA — sans lui, les entités
    de c2 ne se chargent pas
  Une slide en c1, juste après `springboot.md`, au moment où on parle des proxies CGLIB
  (`di.md:368-408`) : c'est le bon endroit, l'enchaînement est naturel.
  → le piège correspondant côté `@Transactional` est détaillé dans [[2026/Cours 3]].
- [ ] **C1-21** · **Conventions REST** : le chapitre `mvc.md` montre `ResponseEntity.ok()` et
  `badRequest()`, mais rien sur **201 Created + header `Location`** pour un POST, ni sur
  `@ResponseStatus`. Ce sont les deux premières choses attendues d'une API REST en TP.
- [ ] **C1-22** · `@RequestMapping` au niveau **classe** pour factoriser le préfixe (`/api/v1/…`) : tous
  les exemples répètent le chemin complet, et les slides suivantes du cours utilisent
  `/api/v1/demo` sans jamais montrer d'où vient le préfixe.
- [ ] **C1-23** · `@RequestParam` optionnel / avec `defaultValue` — cas le plus fréquent en pratique,
  absent.
- [ ] **C1-24** · `@Profile` : jamais montré en c1, alors que les profils sont introduits en c2 côté
  configuration. Le lien `@Profile` sur un bean n'arrive qu'en `c2/config.md:522`.
- [ ] **C1-25** · Injection d'une **`List<T>`** de toutes les implémentations d'une interface : c'est la
  suite logique du fil conducteur `Database` / `PostgresDb` / `MySqlDb` déroulé sur 10 slides,
  et ça ne coûte qu'une slide.
- [ ] **C1-26** · **Dépendances circulaires** : erreur que les étudiants rencontreront en TP, et qui
  découle directement de l'arbre de dépendances expliqué en `c3/di-reminder.md:77-80`.
- [ ] **C1-27** · `@PostConstruct` / `@PreDestroy` — cycle de vie des beans, jamais évoqué.
- [ ] **C1-28** · 🟡 `di.md:918-925` montre le source de `@Configuration` mais **omet
  `proxyBeanMethods`**, alors que la slide juste avant (`di.md:368-408`) explique précisément
  le proxy CGLIB. `proxyBeanMethods = false` est la réponse à « comment on désactive ce
  proxy » : la question va être posée.
- [ ] **C1-29** · 🟡 `di.md:786-810` présente l'injection par propriété (`@Autowired lateinit var`) comme
  une **alternative équivalente** à l'injection par constructeur. C'est un anti-pattern
  (non testable, dépendances cachées, pas d'immutabilité). Le TL;DR de
  `[[slides/pages/c3/di-reminder|c3/di-reminder.md:90-93]]` tranche correctement — mais deux
  cours plus tard. Poser l'avertissement dès c1.

## 🟡 Notes orateur manquantes

Slides sans note où le contenu ne se lit pas seul :

- [ ] **C1-30** · `test.md:14-22` — slide image `code_coverage.jpg` : aucune note, et l'image seule ne
  porte pas le message
- [ ] **C1-31** · `test.md:18-26` — pyramide des tests : aucune note pour une slide qui est *uniquement*
  une image
- [ ] **C1-32** · `test.md:71-111` — cycle de vie JUnit (`@BeforeAll`/`@BeforeEach`/…) : aucune note,
  alors que l'ordre d'exécution est précisément ce qu'il faut commenter
- [ ] **C1-33** · `test.md:391-485` — la note existe et est excellente (28 lignes), **rien à faire** :
  c'est le modèle à reproduire ailleurs
- [ ] **C1-34** · `di.md:10-34` — premier diagramme du fil conducteur : la note est sur la slide
  *suivante* (`di.md:68-72`), donc la première slide est muette
- [ ] **C1-35** · `di.md:224-249`, `di.md:465-524`, `di.md:936-1025` — magic-move sans note
- [ ] **C1-36** · `mvc.md:116-150`, `mvc.md:183-226`, `mvc.md:228-271` — tout le chapitre paramètres /
  codes retour / DTO est sans note
- [ ] **C1-37** · `jee.md:80-244` — le grand schéma animé (10 clics) a une note très riche… qui est
  ensuite **recopiée mot pour mot** dans les 10 slides suivantes (voir coupe ci-dessous)

## 🟡 Placement

- [ ] **C1-38** · `test.md:162-172` — l'avertissement « `@SpringBootTest` ne fonctionne que dans un
  sous-package » arrive **après** les trois slides `@SpringBootTest`. C'est un prérequis pour
  que les exemples marchent : le remonter avant `test.md:118`.
  (C'est vraisemblablement le « slide warning mal placé » de `note.adoc`.)

> [!tip] Coupes proposées — ~12 slides, de quoi aligner c1 sur les autres séances
> - **C1-39** · **`jee.md:246-310` (10 slides)** : JPA, JTA, JMS, CDI, EJB, Servlet, JSP, JSF, JAX
>   reprennent **mot pour mot** le contenu de la note orateur du grand schéma animé
>   (`jee.md:204-244`). Le schéma animé fait déjà le travail, avec l'avantage de montrer les
>   dépendances entre briques. Garder le schéma + la note, supprimer les 10 slides de texte.
>   → **le meilleur gain du cours : 10 slides, zéro perte pédagogique.**
> - **C1-40** · **`di.md:155-222` (1 slide)** : quasi-doublon de `di.md:74-153` (même diagramme, seule la
>   flèche d'héritage change de style), et c'est celle qui porte l'erreur de compilation.
>   Supprimer plutôt que corriger.
> - **C1-41** · **`di.md:1039-1216` (TL;DR, 5 slides)** : utile, mais à resserrer — le TL;DR reprend
>   intégralement du code déjà montré. 3 slides suffisent, et ça élimine au passage
>   l'erreur de `di.md:1200-1209` et le titre dupliqué de `di.md:1145`.
>
> Ces ~12 slides financent : la slide plugins Gradle Kotlin, la slide conventions REST,
> et l'avertissement sur l'injection par propriété.
