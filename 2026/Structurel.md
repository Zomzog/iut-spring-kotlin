---
tags: [spring, kotlin, iut, relecture, structurel]
date: 2026-09-25
cible: spring-boot-4.1
---

← [[2026]]

# §0 — Problèmes structurels

Tout ce qui ne relève pas du contenu pédagogique : plomberie Slidev, notes orateur,
équilibre des séances.

## Références `src:` mortes

> [!danger] 4 fichiers référencés mais supprimés — invisible au build
> `[[slides/pages/c2|c2.md:26,30]]` référence `c2/logs.md` et `c2/actuators.md`.
> `[[slides/pages/c3|c3.md:34,38]]` référence `c3/logs.md` et `c3/actuators.md`.
> **Les quatre fichiers n'existent plus** : ils ont été supprimés par le commit `91b0044`
> (« Zomzog/c4 »), quand le contenu logs/actuators a été déplacé vers `c4/`.
> Slidev **ignore silencieusement** un `src:` introuvable — vérifié en lançant
> `npx slidev build` : le build passe, aucun warning, aucun slide vide.
> Conséquence : les blocs morts sont indétectables sans relire les fichiers.

- [ ] 🔴 Supprimer les 2 blocs `src:` en fin de `[[slides/pages/c2|c2.md]]`
- [ ] 🔴 Supprimer les 2 blocs `src:` en fin de `[[slides/pages/c3|c3.md]]`

À noter que les inclusions **imbriquées** fonctionnent bien, elles : `c4.md` inclut
`c4/observability.md`, qui inclut lui-même `logs.md`, `actuators.md` et `telemetry.md` via
`src:`. C'est pour cette raison que ces trois fichiers n'apparaissent pas dans `c4.md` — ce
n'est pas un oubli.

## Speaker notes

**192 slides sur 298 (64 %) n'ont aucune note.** Le détail des slides code-lourdes
concernées est dans chaque note de cours (section « Notes manquantes »). Les problèmes
transverses :

- [ ] 🟡 **Deux conventions incompatibles.** c1 → c3 utilisent des blocs `<!-- … -->`
  multi-lignes ; c4 utilise des `<!-- Speaker: … -->` d'une ligne. Les deux sont bien
  reconnus par le parser, mais le préfixe « Speaker: » se retrouve **dans** le texte de la
  note affichée en mode présentateur. Harmoniser sur le style c1-c3.
- [ ] 🔴 `[[slides/pages/c4/messaging|c4/messaging.md:30]]` : le commentaire est placé
  **à l'intérieur** d'un `<div v-click.at='3'>`. Vérifié au parser : cette slide ressort avec
  une note **vide**. Le sortir du `div`.
- [ ] ⚪ `[[slides/pages/c2/validation|c2/validation.md:67-81]]` : deux blocs `<!-- -->`
  successifs sur la même slide. Slidev ne retient que **le dernier** (vérifié : la note de
  cette slide est « On peut cumuler les contraintes, même sans cohérence »). L'explication de
  `@Validated` et `@Min` — le cœur de la slide — est **perdue**. Fusionner les deux blocs.
- [ ] 🟡 `[[slides/pages/c3/filters|c3/filters.md:603-621]]` : même problème, deux blocs
  successifs. Le second (« il est possible de modifier la requête avant… ») écrase le premier
  qui explique `doFilter`, `FilterChain` et le wrapper de réponse.

## Cohérence interne

- [ ] 🔴 **Contradiction entre deux cours sur le même chemin.**
  `[[slides/pages/c1/springboot|c1/springboot.md:345-357]]` place le fichier d'imports dans
  `src/main/resources/META-INF/` — il manque le dossier `spring/`.
  `[[slides/pages/c4/auto-configuration|c4/auto-configuration.md:41,112]]` donne le bon
  chemin : `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`
  (confirmé inchangé en Boot 4.1). C'est c1 qu'il faut corriger.
- [ ] 🔴 `[[slides/pages/c4/auto-configuration|c4/auto-configuration.md:55]]` : lien
  markdown `[Voir aussi — Cours 2](/slides/pages/c2/config.md)`. C'est un chemin de fichier
  source dans un deck rendu → lien mort pour l'étudiant. Utiliser un numéro de slide Slidev
  (`[…](12)`) ou retirer le lien.
- [ ] 🟡 `[[slides/pages/c4/auto-configuration|c4/auto-configuration.md:53]]` : un `---` nu
  à l'intérieur d'un `<div v-click>`. Il passe aujourd'hui parce qu'il est indenté de deux
  espaces, mais c'est fragile : désindenté, il couperait la slide en deux. Le remplacer par
  `<hr/>` ou le supprimer.
- [ ] 🟡 **Deux styles de layout deux colonnes cohabitent** : les layouts maison
  `TwoColumns` / `TwoColumnsTitle` (`layouts/`) et le `two-cols` natif de Slidev, utilisé
  une seule fois en `[[slides/pages/c1/di|c1/di.md:411]]`. Les deux fonctionnent (même slot
  `::right::`), mais le rendu diffère. Harmoniser sur les layouts maison.
- [ ] ⚪ `[[slides/pages/c4/actuators|c4/actuators.md:387,397,409]]` : des étiquettes de
  bloc de code (`[select endpoints]`, `[all endpoints do not use in production]`) sont
  utilisées comme phrases explicatives, hors d'un `code-group`. Détourne un mécanisme prévu
  pour des noms de fichiers ; mettre le texte sur la slide.

## `note.adoc` — les remarques de l'auteur, non traitées

Le fichier `slides/note.adoc` contient 4 constats de l'an dernier, aucun n'est adressé :

- [ ] 🔵 « dossier exo images KO » — hors périmètre de cette relecture (TP hors dépôt),
  mais à ne pas perdre.
- [ ] 🟡 « slide warning mal placé » — vise vraisemblablement
  `[[slides/pages/c1/test|c1/test.md:162-172]]`, l'avertissement « `@SpringBootTest` ne
  fonctionne que dans un sous-package ». Il arrive **après** les trois slides de
  `@SpringBootTest`, alors que c'est la condition préalable pour que les exemples marchent.
  À remonter juste avant `test.md:118`.
- [ ] 🟡 « découpe en 3 complexe sans gestion d'erreur » — à clarifier avec l'auteur ; sans
  doute la découpe Controller / Service / Repository introduite en
  `[[slides/pages/c1/test|c1/test.md:564-604]]` sans que la gestion d'erreur (vue seulement
  en c2) soit encore posée.
- [ ] 🔵 « pas de BeforeEach AfterEach dans les exemples » — le cycle de vie JUnit est bien
  montré une fois (`[[slides/pages/c1/test|c1/test.md:82-111]]`), mais **aucun** exemple de
  test du cours n'utilise `@BeforeEach` pour préparer un état, alors que c'est exactement ce
  dont les étudiants ont besoin en TP (reset de la base entre deux tests).

## Cadrage général

- [ ] 🟡 **Déséquilibre de densité** : c1 = 89 slides, c2 = 74, c3 = 68, c4 = 64, pour
  1h30 chacun. c1 est 39 % plus dense que c4. Les coupes de [[2026/Cours 1]] (~12 slides)
  suffisent à l'aligner.
- [ ] ⚪ **Pas de slide de conclusion.** Le cours s'achève sur
  `[[slides/pages/c4/telemetry|c4/telemetry.md:788-791]]`, une image `/flam.avif` seule,
  sans texte. Ajouter un récapitulatif + pointeurs (doc Spring, Baeldung, `start.spring.io`).
- [ ] ⚪ `[[slides/pages/c3/security|c3/security.md:12-18]]` : slide « ## Vocabulaire »
  **entièrement vide** (titre seul). Soit la remplir, soit la supprimer — les trois slides
  suivantes font déjà le travail.

> [!info] Faux positifs — vérifiés, **ne pas** les « re-trouver » l'an prochain
> Cinq choses qui ressemblent à des bugs et qui n'en sont pas. Vérifiées, pas supposées.
>
> - **`:: code-group` avec un espace** (6 occurrences : `c1/mvc.md:15`,
>   `c2/validation.md:14`, `c4/flyway.md:219`, `c4/caching.md:429`,
>   `c4/actuators.md:351,514`) → **fonctionne**. Inspection du bundle produit : le composant
>   `CodeGroup` est bien instancié, avec les onglets « gradle » et « maven ».
> - **`c1/springboot.md:209`** : une fence ``` orpheline en tête du bloc `md magic-move`.
>   Slidev la tolère. Décompression du `steps-lz` du bundle : 2 étapes, correctes,
>   correctement colorées.
> - **`c2/config.md:109-168`** : « `.properties` > `.yml` > `.yaml` » est **correct**. La doc
>   Spring Boot le dit explicitement : « If you have configuration files with both
>   `.properties` and YAML format in the same location, `.properties` takes precedence. »
>   (L'ordre `.yml` vs `.yaml` reste, lui, non documenté → [[2026/À vérifier]].)
> - **`c2/config.md:248-291`** : l'ordre des profils actifs (`prod,mongo` → `mongo` gagne)
>   est **correct**, la doc parle d'une stratégie « last-wins ».
> - **`c4/messaging.md:26`** `spring-boot-starter-kafka` : **existe bien**, c'est un
>   nouveau starter introduit par Spring Boot 4. Correct pour la cible 4.1 (il n'existait
>   pas en 3.x, où il fallait `org.springframework.kafka:spring-kafka`).
