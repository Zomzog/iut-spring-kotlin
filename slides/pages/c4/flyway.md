---
layout: cover
hideInToc: false
---

# Flyway

---
layout: full
class: text-left
---

## Qu'est-ce que Flyway ?

<v-click>

- Outil de gestion des **migrations SQL**.

</v-click>
<v-click>

- Fournit également une **CLI**, des **plugins Gradle/Maven** et une **bibliothèque** utilisable dans les applications.

</v-click>

<v-click>

- Intégration native avec **Spring Boot** pour exécuter les migrations **au démarrage**.

</v-click>

<v-click>

<br/>

## Concepts clés

</v-click>

<v-clicks>

- **Migration SQL** : chaque étape est un script SQL.
- **Version** : chaque script porte un identifiant
- **Compatibilité** : supporte H2, PostgreSQL, MySQL, etc.
- **Immuable** : une fois appliqué, modifier un script bloque la migration (sauf réparation).
- **Réparation** : Flyway peut réparer l'historique en cas de problème.

</v-clicks>

---
layout: full
class: text-left
---

## Pourquoi l'utiliser avec Spring Boot ?

- Automatise les migrations au démarrage.
- Garantit la cohérence du schéma entre environnements.
- Permet un contrôle fin du schéma (préférable au DDL auto en production).

---
layout: full
class: text-left
---

## Dépendances

:: code-group

```kotlin [gradle]
dependencies {
  implementation("org.flywaydb:flyway-core")
  implementation("org.flywaydb:flyway-database-postgresql")
}
```

```xml [maven]

<dependencies>
  <dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
  </dependency>
  <dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>flyway-database-postgresql</artifactId>
  </dependency>
</dependencies>
```

::

<br/>

<div v-click>

## Remarques pratiques

- Pour les demos/**local** : H2 est pratique (pas besoin d'ajouter un driver Flyway spécifique).
- En **production** : ajoutez le driver correspondant (Postgres/MySQL) et **testez** les migrations
  dans un environnement proche de la production.

</div>

---
layout: full
class: text-left
---

## Configuration Spring Boot

<div v-click.at='1'>

````md magic-move {at:'2'}

```yaml
spring:
  flyway:
    enabled: true
    url: jdbc:postgresql://localhost:5432/demo-iut
    user: iut
    password: iut
```


```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/demo-iut
    driverClassName: org.postgresql.Driver
    username: iut
    password: iut
  flyway:
    enabled: true
    url: ${spring.datasource.url}
    user: ${spring.datasource.username}
    password: ${spring.datasource.password}
```
````

<div v-click.at='2'>

Astuce : réutiliser la datasource

</div>

</div>

---
layout: full
class: text-left
---

## Emplacement des migrations

<v-click>

Par défaut : `src/main/resources/db/migration`

</v-click>
<v-click>

Pour le changer : `spring.flyway.locations=somewhere/else`

</v-click>
<v-click>

## Convention de nommage

</v-click>
<v-click>

Versioned Migration:

`V<version>__<description>.sql` (ex : `V1__create_users_table.sql`)

</v-click>
<v-click>

Undo Migration:

`U<version>__<description>.sql` (ex : `U1__revert_users_table.sql`)

</v-click>
<v-click>

Repeatable Migration:

`R__<description>.sql` (ex : `R__recreate_view.sql`)

</v-click>

---
layout: full
class: text-left
---

## Exemple SQL (V1__create_users_table.sql)

<div v-click>

```sql
CREATE TABLE pony (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL
);
```

</div>

<div v-click.at='2'>

## flyway_schema_history

| installed_rank | version | description | type | script       | checksum   | installed_by | installed_on            | execution_time | success |
|----------------|---------|-------------|------|--------------|------------|--------------|-------------------------|----------------|---------|
| 1              | "1"     | init        | SQL  | V1__init.sql | 1429062969 | iut          | 2026-01-01 21:48:01.252 | 4              | true    |

</div>

---
layout: full
class: text-left
---

## Historique / repair

- Flyway stocke l'historique des migrations dans une table `flyway_schema_history`.
- Si un script est modifié après application, vous devez :
  - corriger le script et incrémenter la version ;
  - utiliser `flyway repair` pour réparer l'historique (marquer comme supprimée, corriger les checksums...).
- Si des scripts ont été appliqués à la main :
  - utiliser `flyway baseline` pour mettre à jour uniquement l'historique.
