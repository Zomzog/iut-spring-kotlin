---
tags: [spring, kotlin, iut, relecture, a-verifier]
date: 2026-09-25
cible: spring-boot-4.1
---

← [[2026]]

# §8 — Points que je n'ai pas tranchés

Cinq constats que je n'ai **pas** pu confirmer sans exécuter le code. Ils sont signalés comme
« probables » dans les notes de cours, pas comme des faits. Chacun se vérifie en quelques
minutes dans `code/`.

## 1. `findAllOrderByIdDesc()` échoue-t-il au démarrage ?

**Où** : `[[slides/pages/c2/jpa|c2/jpa.md:531]]`

**Ce que je pense** : la dérivation de requête Spring Data (`PartTree`) exige un `By` pour
séparer le sujet du prédicat. Sur `findAllOrderByIdDesc`, le préfixe consommé serait
`findAllOrderBy`, laissant `IdDesc` comme prédicat → recherche d'une propriété `idDesc`
inexistante → `PropertyReferenceException` au démarrage du contexte.
La forme documentée est **`findAllByOrderByIdDesc()`** (ou `findByOrderByIdDesc()`).

**Comment vérifier** : ajouter la méthode telle quelle à un repository de `code/` et lancer
l'application. Si le contexte démarre, mon analyse est fausse.

- [ ] Vérifié → conclusion : ______

## 2. `@WithAnonymousUser` donne-t-il 401 ou 302 ?

**Où** : `[[slides/pages/c3/security|c3/security.md:518-529]]`

**Ce que je pense** : la configuration de `security.md:151-172` active **à la fois**
`formLogin { }` et `httpBasic { }`. Dans ce cas, l'entry point par défaut retenu est celui de
`formLogin` (`LoginUrlAuthenticationEntryPoint`) → une requête non authentifiée sur `/admin`
répond **302 vers `/login`**, pas 401. Le test attend `isUnauthorized()` et devrait donc
échouer.

Le comportement dépend de la négociation de contenu (`DelegatingAuthenticationEntryPoint`) :
avec un `Accept: application/json` ou un `X-Requested-With`, Spring peut préférer le 401. En
MockMvc sans header, je m'attends à la 302.

**Comment vérifier** : écrire le test dans `code/` avec la configuration exacte de la slide.

**Si confirmé**, deux corrections possibles, et la seconde est la meilleure :
- attendre `isFound()` / `is3xxRedirection()` — masque le problème
- **retirer `formLogin`** de la configuration d'une API REST, ce qui rend le 401 correct et
  cohérent avec le manque « API stateless » relevé dans [[2026/Cours 3]]

- [ ] Vérifié → conclusion : ______

## 3. `@DataJpaTest` + `@ServiceConnection` : faut-il `replace = NONE` ?

**Où** : `[[slides/pages/c4/integration-testing|c4/integration-testing.md:35-145]]`

**Ce que je pense** : `@DataJpaTest` applique `@AutoConfigureTestDatabase`, qui **remplace la
`DataSource`** par une base embarquée si elle est disponible sur le classpath. Or
`code/build.gradle.kts:45` déclare `runtimeOnly("com.h2database:h2")` : H2 **est** sur le
classpath. Le conteneur PostgreSQL démarrerait alors pour rien, et le test tournerait en fait
sur H2 — ce qui annule tout l'intérêt de la slide.

Deux issues possibles : soit Spring Boot 3.1+ détecte le `@ServiceConnection` et ne remplace
pas, soit il faut `@AutoConfigureTestDatabase(replace = Replace.NONE)`.

**Comment vérifier** : `code/src/test/kotlin/.../UserRepositoryServiceConnectionTest.kt`
existe déjà. Lancer le test et regarder les logs Hibernate : quel dialecte ? Ou simplement
inspecter l'URL de la `DataSource` injectée.

**Dans tous les cas**, ce point mérite une note orateur : c'est le genre de détail qui fait
qu'un test « passe » sans tester ce qu'on croit.

- [ ] Vérifié → conclusion : ______

## 4. springmockk 5.x fonctionne-t-il réellement avec Spring Boot 4.1 ?

**Où** : `[[slides/pages/c1/test|c1/test.md]]` (`@MockkBean`, `@SpykBean`), et par ricochet
c2 et c3.

**Ce que je sais** : le README annonce « Version 5.x : compatible with **Spring Framework 7**,
Java 17+ ». Spring Boot 4.1 embarque bien Spring Framework 7, donc a priori c'est bon. Mais la
matrice de compatibilité est exprimée en versions de **Framework**, pas de **Boot**, et le
projet prévient lui-même : « This is not an official Spring Boot project, so it might not work
out of the box for newest versions if backwards incompatible changes are introduced ».

**Ce qui est sûr** : `@SpykBean` est renommé **`@MockkSpyBean`** en 5.x (alignement sur
`@MockitoSpyBean`) → correction déjà listée dans [[2026/Migration 4.1]].

**Comment vérifier** : ajouter `testImplementation("com.ninja-squad:springmockk:5.0.1")` à
`code/` après migration en 4.1 et lancer un test avec `@MockkBean`.

**Plan B si ça casse** : basculer les slides sur `@MockitoBean` / `@MockitoSpyBean` (natifs,
donc sans risque de décrochage l'an prochain) et garder MockK uniquement pour les tests
unitaires purs, sans contexte Spring. Ce serait un changement pédagogique notable — à décider
tôt, pas la veille de la rentrée.

- [ ] Vérifié → conclusion : ______

## 5. Précédence `.yml` vs `.yaml`

**Où** : `[[slides/pages/c2/config|c2/config.md:109-168]]`, `:293-327`

**Ce que j'ai confirmé** : `.properties` **prime bien** sur YAML dans le même emplacement. La
doc Spring Boot est explicite : « If you have configuration files with both `.properties` and
YAML format in the same location, `.properties` takes precedence. » → **la slide est juste sur
ce point.**

**Ce que je n'ai pas confirmé** : l'ordre entre `.yml` et `.yaml`. Ce n'est **documenté nulle
part** — c'est un détail d'implémentation (ordre des `PropertySourceLoader`). Le schéma
`block-beta` de `config.md:293-327` en fait un enchaînement de 9 cases.

**Recommandation** : ne pas chercher à vérifier, mais **ne pas enseigner** un comportement non
documenté comme une règle. La slide dit déjà en note « /!\ pas une bonne pratique » : autant
aller au bout et la supprimer (proposition de coupe dans [[2026/Cours 2]]), en gardant
seulement la règle documentée `.properties` > YAML et la consigne « choisissez un format et
tenez-vous-y ».

- [ ] Décidé → conclusion : ______

---

## Méthode de vérification utilisée pour le reste du rapport

Pour mémoire, afin de savoir à quel point faire confiance aux autres constats :

- **Lecture intégrale** des 27 fichiers de `slides/pages/` (~10 600 lignes), de
  `slides/note.adoc` et de `code/build.gradle.kts`.
- **Build Slidev réel** (`npx slidev build`) puis comptages via `@slidev/parser` : les 298
  slides et les 192 sans notes sont des mesures, pas des estimations (un comptage naïf des
  `---` donne le double, à cause des délimiteurs de frontmatter).
- **Inspection du bundle produit** pour trancher 5 suspicions de bugs Slidev — toutes se sont
  révélées fausses, elles sont documentées dans [[2026/Structurel]] pour éviter de les
  « re-trouver ».
- **Vérification documentaire** de chaque point « obsolète 4.1 » contre la doc Spring
  officielle (liens en bas de [[2026]]), jamais de mémoire.
- **Non vérifié par exécution** : aucun snippet n'a été compilé. Les erreurs de compilation
  signalées reposent sur la lecture du langage — fiables pour les cas nets (`val` réassigné,
  `do` mot-clé, accolade manquante, `return` absent), et c'est précisément pour les cas non
  nets que cette note existe.
