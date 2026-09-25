---
tags: [spring, kotlin, iut, relecture, cours/c2]
date: 2026-09-25
cible: spring-boot-4.1
slides: 74
---

← [[2026]]

# §3 — Cours 2 (74 slides)

`validation` → `error` → `config` → `jpa` → `jpa2`.

> [!danger] Le point majeur du cours : `@Validated` sur un contrôleur est à retirer
> Depuis **Spring Framework 6.1** (donc Spring Boot 3.2+, a fortiori 4.1), Spring MVC applique
> la validation aux paramètres de méthode de contrôleur **automatiquement**, sans
> `@Validated`. La doc est explicite :
> « In order to take advantage of the Spring MVC built-in support for method validation added
> in Spring Framework 6.1, you need to **remove the class level `@Validated` annotation** from
> the controller. »
>
> Conséquences en cascade :
> - L'exception levée est **`HandlerMethodValidationException`** (→ 400), pas
>   `ConstraintViolationException` (qui remontait en 500 avec l'ancienne approche AOP), et pas
>   `MethodArgumentNotValidException`.
> - Avec `@Validated` sur la classe, on repasse par un **proxy AOP** : on perd le support
>   natif, et on hérite des limites du proxy.
>
> Or tout le chapitre repose sur `@Validated` :
> `[[slides/pages/c2/validation|c2/validation.md:44-137]]`, `:198-206`, `:470-485`.
> Et `[[slides/pages/c2/error|c2/error.md:604-639]]` traite
> `MethodArgumentNotValidException` — qui ne concerne en réalité **que** `@Valid @RequestBody`,
> pas les `@RequestParam`/`@PathVariable` annotés.
>
> - [ ] 🟠 Refondre `validation.md` sans `@Validated` sur le contrôleur
> - [ ] 🟠 Distinguer explicitement les deux exceptions dans `error.md` :
>   `HandlerMethodValidationException` (paramètres) vs `MethodArgumentNotValidException`
>   (`@Valid` sur un body)
> - [ ] 🔵 Garder `@Validated` mais pour son **vrai** cas d'usage aujourd'hui : valider les
>   paramètres d'une méthode de **service** (hors contrôleur)

## 🔴 Erreurs — `validation.md`

- [ ] **`validation.md:150-164`** — le setter Java `public String setName(String n) { name = n; }`
  n'a pas de `return` : **ne compile pas**. Il doit être `void`.
- [ ] **`validation.md:180`** — côté Kotlin, `@set:OnGet` alors que le pendant Java est
  `@OnSet`. La slide entière sert à faire correspondre les cibles d'annotation : l'erreur
  casse exactement la démonstration.
- [ ] **`validation.md:479-481`** — `@Validated @Component class DemoController` : un
  contrôleur annoté `@Component`. Incohérent avec le reste du cours (et avec les slides
  précédentes du même fichier, où la classe n'a aucune annotation). → `@RestController`.
- [ ] 🟡 `validation.md:46-63` — `fun list(i: Int) = ...` : `...` n'est pas une expression
  Kotlin valide, et les blocs de code ne sont pas refermés. Acceptable comme extrait, mais
  `= TODO()` serait à la fois valide et plus parlant (déjà utilisé ailleurs dans le cours).

## 🔴 Erreurs — `error.md`

- [ ] **`error.md:716-736`** — le test est nommé `` `happy path` `` alors qu'il injecte une
  exception et attend un **400**. En plus :
  - la classe s'appelle `DemoDtoTest` mais teste un contrôleur
  - `ImATeapotException` n'existe pas dans Spring (il y a `HttpStatus.I_AM_A_TEAPOT`,
    `ResponseStatusException`, ou `HttpClientErrorException.ImATeapot` côté client)
  - un teapot devrait donner 418, pas 400
  Slide à reprendre entièrement, et à renommer (`unhandled exception gives 400`).
- [ ] 🟡 `error.md:595`, `:601`, `:609`, `:618`, `:628`, `:666`, `:671`, `:685` —
  `@ControllerAdvice` partout. Ça fonctionne (le type de retour `ResponseEntity` suffit à
  déclencher l'écriture du corps), mais pour une API REST l'idiome est
  **`@RestControllerAdvice`**. À corriger, avec une phrase sur la différence.

## 🔴 Erreurs — `config.md`

- [ ] **`config.md:15`, `:24`, `:36`, `:58`, `:196`, `:204`** — `src/resources/…` :
  **6 occurrences** du mauvais chemin. C'est `src/main/resources/`. Les étudiants vont créer
  le dossier au mauvais endroit et passer 20 minutes à chercher pourquoi la propriété n'est
  pas lue. (`[[slides/pages/c4/logs|c4/logs.md:60]]` donne le bon chemin, lui.)
- [ ] **`config.md:239`** — `java -jar app.jar -Dspring.profiles.active=prod,mongo` :
  **`-D` doit précéder `-jar`**. Placé après, c'est un argument applicatif, et il n'est même
  pas au format `--spring.profiles.active=…` que Spring Boot saurait lire. La commande ne fait
  donc rien du tout.
  → `java -Dspring.profiles.active=prod,mongo -jar app.jar`
  → et mentionner la forme la plus courante : `java -jar app.jar --spring.profiles.active=prod`
- [ ] **`config.md:230`** — `export spring_profiles_active=dev` : la convention est
  **`SPRING_PROFILES_ACTIVE`** en majuscules. La résolution insensible à la casse de Spring
  peut sauver la mise, mais on enseigne une forme qui n'est pas celle documentée et qui
  échouera dans certains environnements.
- [ ] **`config.md:399-405`** — `@ConfigurationProperties` avec `@NotBlank` / `@Min(10)` :
  **deux problèmes cumulés**, et la slide ne marche pas du tout.
  1. Il manque **`@Validated`** sur la classe — sans lui les contraintes sont purement
     ignorées, silencieusement.
  2. Les annotations ne portent pas `@field:`, alors que c'est enseigné 270 lignes plus haut
     (`validation.md:131-134`). Sur une `data class` liée par constructeur, la validation
     porte sur l'instance : il faut `@field:NotBlank`.
  La note orateur dit « C'est un bean, donc on peut réutiliser la validation » — l'intention
  est juste, l'exemple ne la réalise pas.
- [ ] **`config.md:443-449`** — `fun aBean(val properties: CustomProperties)` :
  **`val` est interdit sur un paramètre de fonction** en Kotlin. Ne compile pas.
- [ ] 🟡 `config.md:343-348` — `class Demo { @Value(…) }` sans `@Component` : `@Value` n'est
  résolu que sur un bean. Implicite pour l'enseignant, pas pour l'étudiant.

## 🔴 Erreurs — `jpa.md`

- [ ] **`jpa.md:779-821`** — **`@Table("PonyTable")`** : `jakarta.persistence.@Table` n'a
  **pas** d'attribut `value`. Il faut `@Table(name = "PonyTable")`. **4 occurrences** dans le
  magic-move. (La confusion vient de Spring Data JDBC/R2DBC, dont le `@Table` accepte bien
  une valeur positionnelle.) `jpa2.md` utilise correctement `name =` partout : c'est `jpa.md`
  qui est faux.
- [ ] **`jpa.md:365-378`** — **une accolade de trop** : `fun findAll()` en forme d'expression,
  suivie de `}` puis `}`. L'état final du magic-move ne compile pas.
- [ ] **`jpa.md:531`** — `fun findAllOrderByIdDesc(): List<DemoEntity>` : la dérivation Spring
  Data attend un `By`. La forme correcte est **`findAllByOrderByIdDesc()`** (ou
  `findByOrderByIdDesc()`). En l'état, le contexte devrait échouer au démarrage.
  → à confirmer en exécutant, voir [[2026/À vérifier]].
- [ ] **`jpa.md:331-348`** — étape intermédiaire du magic-move : on remplace
  `@PersistenceUnit factory` par `@PersistenceContext entityManager`, mais le corps continue
  d'appeler `factory.createEntityManager()`. L'étape ne compile pas et **n'est pas marquée**,
  alors que le cours utilise ailleurs un 🚫 pour signaler les étapes volontairement fausses
  (`di.md:698`, `validation.md:374`). Ajouter le marqueur.
- [ ] **`jpa.md:726`** — `val nameField: Path<DemoEntity> = root.get("name")` : le type
  déclaré est faux, `name` est une colonne `String`. Ça compile par inférence du générique,
  mais ça enseigne un contresens sur ce que représente un `Path`.
- [ ] ⚪ `jpa.md:439-453` — la signature simplifiée de `JpaRepository` est acceptable, mais
  préciser que la vraie hiérarchie passe par `ListCrudRepository` / `CrudRepository` évite
  que l'étudiant cherche `save` dans `JpaRepository` sans le trouver.

## 🔴 Erreurs — `jpa2.md`

- [ ] **`jpa2.md:612-632`** et **`jpa2.md:826-833`** — `@ManyToMany` : le côté inverse
  (`PhoneEntity.user`) **n'a pas de `mappedBy`**. Hibernate crée alors **une seconde table de
  jointure**, et les deux côtés de la relation sont désynchronisés. Il faut
  `@ManyToMany(mappedBy = "phones")`. L'erreur est reproduite dans le TL;DR, donc affichée
  deux fois.
  (`[[slides/pages/c3/jpa3|c3/jpa3.md:533]]` le fait correctement — bon contre-exemple à
  aligner.)
- [ ] **`jpa2.md:409-427`** — état final du One-to-One bidirectionnel :
  `UserEntity.phone: PhoneEntity?` (nullable, `var`) mais `PhoneEntity.user: UserEntity`
  **non nullable**. En Kotlin, **aucun des deux objets n'est constructible** : il faut l'autre
  pour se créer. La note dit « L'usage de var et du nullable permet de créer les objets puis
  les imbriquer » — c'est exact, mais appliqué à un seul des deux côtés. Rendre
  `user: UserEntity? = null` aussi.
- [ ] **`jpa2.md:390-408`** — étape intermédiaire cassée non signalée : on remplace
  `@Id email` par `@Id @GeneratedValue id`, alors que le côté propriétaire référence toujours
  `referencedColumnName = "email"` → colonne inexistante. L'étape suivante corrige avec
  `name = "fk_email"`, mais entre les deux la sémantique de `referencedColumnName` devient
  incompréhensible. Ajouter un 🚫 ou fusionner les deux étapes.
- [ ] **`jpa2.md:668-670`** — note orateur « ManyToMany, il faut une table de jointure »
  placée sur la slide **OneToOne unidirectionnel**. Copier-coller, la note est à déplacer sur
  `jpa2.md:812`.
- [ ] **`jpa2.md:753-778`** (« Many-To-One ») et **`jpa2.md:780-805`** (« One-To-Many ») —
  **code strictement identique** sur les deux slides du TL;DR. L'une est à supprimer, ou à
  réécrire pour montrer effectivement l'autre direction.
- [ ] **`jpa2.md:103-143`** — la slide `@ManyToOne` montre des données où **chaque poney a une
  occupation distincte** (1→1, 2→2, 3→3) : c'est du one-to-one, pas du many-to-one. Pour
  illustrer le many-to-one, il faut deux poneys partageant le même `occupation_id`.
- [ ] 🟡 **`jpa2.md:449`, `:464`, `:482`, etc.** — `val phones: List<PhoneEntity> = emptyList()`.
  Pour une collection JPA il faut **`var` + `MutableList` + `mutableListOf()`** : Hibernate
  remplace la collection par une `PersistentBag`, et avec un `val`/`List` immuable on ne peut
  ni ajouter ni retirer d'élément. Piège Kotlin+JPA très concret en TP.
- [ ] ⚪ `jpa2.md:48`, `:134` — propriété `occupations` (pluriel) pour une relation singulière.

## 🟠 Obsolescence 4.1

- [ ] `@Validated` sur contrôleur → voir l'encadré en tête de note
- [ ] 🟡 `jpa.md:128-136` — `@GeneratedValue` sans `strategy` : avec Hibernate 6 + PostgreSQL,
  `AUTO` donne une **séquence**, pas un `IDENTITY`. Les étudiants s'attendent à un
  auto-incrément. Une phrase de note suffit. (`jpa2.md` utilise
  `GenerationType.IDENTITY` explicitement — harmoniser.)

## 🔵 Manques

- [ ] **`ProblemDetail` / RFC 9457** — le meilleur rapport valeur/effort du cours.
  `[[slides/pages/c2/error|c2/error.md:662-701]]` étend **déjà**
  `ResponseEntityExceptionHandler`, qui est précisément le point d'entrée du support
  `ProblemDetail`. Il manque : `spring.mvc.problemdetails.enabled=true`, la forme du corps
  (`type`, `title`, `status`, `detail`, `instance`), le media type
  `application/problem+json`, et `ErrorResponseException`. **2 slides**, et les étudiants
  produisent des erreurs d'API au format standard au lieu d'une `String`.
  (À signaler : les erreurs passant par `BasicErrorController` gardent le format legacy —
  incohérence connue.)
- [ ] **Kotlin non-null + Jackson.** Un champ `val name: String` absent du JSON ne produit
  **pas** une erreur de validation mais un `HttpMessageNotReadableException` (400) émis par
  Jackson **avant** toute validation. C'est la question n°1 des étudiants sur ce chapitre, et
  ça explique pourquoi `@NotNull` semble « ne pas marcher ». Mentionner aussi
  `jackson-module-kotlin` (présent dans `code/build.gradle.kts:39`, jamais évoqué).
- [ ] **`FetchType` LAZY/EAGER et le problème N+1.** Absent de c2 : `jpa2.md` parle de
  cascades et de directions, jamais de chargement. Le sujet n'apparaît qu'en
  `[[slides/pages/c3/transactional|c3/transactional.md:338-374]]`, après deux séances de JPA.
  Au minimum : les **valeurs par défaut** (`@OneToMany`/`@ManyToMany` = LAZY,
  `@ManyToOne`/`@OneToOne` = EAGER), qui sont contre-intuitives et déterminent tout le reste.
- [ ] **`data class` à éviter pour les entités** : `equals`/`hashCode`/`toString` générés sur
  tous les champs → récursion infinie sur une relation bidirectionnelle, et identité cassée
  pour Hibernate. `jpa2.md` utilise `class` (bien), `c3/jpa3.md` utilise `data class`
  (problématique) : il faut trancher et le dire.
- [ ] **`@Valid` imbriqué** (`@field:Valid` sur une propriété objet) : la validation en
  cascade dans un DTO composé n'est pas montrée.
- [ ] **Groupes de validation** : l'attribut `groups` est montré dans la signature de
  l'annotation (`validation.md:221`) mais jamais utilisé — donc l'étudiant voit le paramètre
  sans savoir à quoi il sert.
- [ ] **Ordre global des sources de propriétés** : arguments de ligne de commande > variables
  d'environnement > propriétés système > fichiers. C'est **beaucoup** plus utile en pratique
  que l'ordre `.yml`/`.yaml` auquel 5 slides sont consacrées. Une slide de la liste
  (simplifiée à 5 entrées) remplacerait avantageusement `config.md:293-327`.
- [ ] **Multi-document YAML + `spring.config.activate.on-profile`** : la façon moderne de
  gérer les profils dans **un seul** fichier, plutôt qu'un fichier par profil. Également
  `spring.profiles.group` et `spring.config.import`.
- [ ] **`@ConfigurationPropertiesScan`** : alternative à `@EnableConfigurationProperties`,
  et c'est ce que génère `start.spring.io`.
- [ ] **`@DataJpaTest` est transactionnel et rollback par défaut**, et il **remplace la
  DataSource** par une base embarquée (`@AutoConfigureTestDatabase`). Les deux comportements
  surprennent : l'étudiant ne voit rien dans sa base PostgreSQL après le test.
  `jpa2.md:836-872` ne dit ni l'un ni l'autre. → suite dans [[2026/Cours 4]] avec
  Testcontainers.
- [ ] 🟡 `jpa.md:596-613` — la note dit « Ne pas hésiter à utiliser les string template » à
  propos de `@Query` : terminologie fausse (ce sont des *raw strings*) et surtout, il faut
  l'inverse comme avertissement — **ne jamais** interpoler de variable Kotlin dans un
  `@Query` (injection SQL, et les paramètres nommés existent pour ça).
- [ ] ⚪ `jpa.md:567-569` — `findByAgeLessThanEqualAndNameNotInOrKindOrderByIdDesc` mélange
  `And` et `Or` sans parenthèses : bonne occasion de dire un mot sur la précédence, sinon la
  slide n'est qu'une curiosité.

## 🟡 Notes orateur manquantes

- [ ] `config.md:53-88` — les trois formes de notation YAML (plate / imbriquée / camelCase) :
  **aucune note**, alors que la slide démontre le *relaxed binding*, qui ne se devine pas
- [ ] `config.md:170-187` — slide « Profiles » : bloc `<!-- -->` **vide**
- [ ] `config.md:90-128` — les trois slides de l'ordre `.properties`/`.yml`/`.yaml` :
  seule la troisième a une note
- [ ] `validation.md:83-137` — validation d'objets, `@field:` : aucune note
- [ ] `validation.md:321-396` — custom annotation (magic-move de 5 étapes dont une marquée
  🚫) : aucune note
- [ ] `validation.md:470-485` — slide finale d'utilisation : aucune note
- [ ] `error.md:494-504`, `:709-737` — introduction et test de couche : aucune note
- [ ] `jpa.md:430-506` — `JpaRepository` et les 3 contre-exemples barrés : aucune note, et
  les slides barrées ont besoin d'être commentées pour être comprises
- [ ] `jpa2.md:1-198` — les 5 slides de types de jointures : **aucune note sur les cinq**
- [ ] `jpa2.md:569-633` — Many-To-Many : aucune note (et c'est là qu'est l'erreur `mappedBy`)
- [ ] `jpa2.md:642-834` — les 7 slides de TL;DR : une seule note, et elle est sur la mauvaise
  slide

> [!tip] Coupes proposées
> - **`config.md:293-327` (1 slide)** : le `block-beta` des 9 combinaisons
>   `.properties`/`.yml`/`.yaml` × profils. La note dit elle-même « /!\ pas une bonne
>   pratique ». Consacrer une slide à un anti-pattern exhaustif, quand l'ordre global des
>   sources de propriétés n'est pas enseigné, est un mauvais arbitrage. **Remplacer** par la
>   slide d'ordre de précédence.
> - **`jpa.md:105-250` (1 slide, magic-move de 9 étapes)** : `EntityManager` /
>   `@PersistenceUnit` bas niveau. Le détour est justifié pédagogiquement (montrer ce que
>   Spring Data automatise), mais 9 étapes c'est trop pour du code que personne n'écrira.
>   Réduire à 4 étapes : classe vide → `persist` → transaction → JPQL.
> - **`jpa2.md:642-834` (7 slides de TL;DR)** : largement redondantes avec
>   `[[slides/pages/c3/jpa3|c3/jpa3.md]]`, qui traite les mêmes cas **avec le DDL généré** —
>   nettement plus instructif. Garder 2 slides de synthèse (les annotations + la règle
>   « `mappedBy` du côté inverse, `@JoinColumn` du côté propriétaire ») et renvoyer à c3 pour
>   le détail. Supprime au passage 3 erreurs (TL;DR `@ManyToMany`, note mal placée, doublon
>   Many-To-One/One-To-Many).
>
> Gain net : ~7 slides, qui financent les 2 slides `ProblemDetail`, la slide Jackson+Kotlin
> et la slide LAZY/EAGER.
