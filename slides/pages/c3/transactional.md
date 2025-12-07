---
layout: cover
hideInToc: false
---

# Transactions

---
layout: full
class: text-left
---

## Transaction SQL

```sql
BEGIN;
UPDATE accounts SET balance = balance - 100.00
    WHERE name = 'Alice';
UPDATE accounts SET balance = balance + 100.00
    WHERE name = 'Bob';
COMMIT;
```

<div v-click>

````md magic-move {at: 2}
```kotlin
fun update() {
  myDb.updateBalance("Alice", -100.00)
  myDb.updateBalance("Bob", 100.00)
}
```
```kotlin
fun update() {
  val tId = myDb.startDbTransaction()
  myDb.updateBalance(tId, "Alice", -100.00)
  myDb.updateBalance(tId, "Bob", 100.00)
}
```
```kotlin
fun update() {
  val tId = myDb.startDbTransaction()
  myDb.updateBalance(tId, "Alice", -100.00)
  myDb.updateBalance(tId, "Bob", 100.00)
  myDb.commitTransaction(tId)
}
```
```kotlin
fun update() {
  val tId = myDb.startDbTransaction()
  try {
    myDb.updateBalance(tId, "Alice", -100.00)
    myDb.updateBalance(tId, "Bob", 100.00)
    myDb.commitTransaction(tId)
  } catch (e: Exception) {
    myDb.rollbackTransaction(tId)
  }
}
```
```kotlin
fun update() {
  val tId = myDb.startDbTransaction()
  try {
    myDb.updateBalance(tId, "Alice", -100.00)
    myDb.updateBalance(tId, "Bob", 100.00)
    myDb.commitTransaction(tId)
  } catch (e: Exception) {
    myDb.rollbackTransaction(tId)
  }
}
```
```kotlin
@Transactional
fun update() {
  myDb.updateBalance("Alice", -100.00)
  myDb.updateBalance("Bob", 100.00)
}
```
````

</div>

<div v-click.at="5">

## @Transactional

L'annotation `@Transactional` de Spring Boot permet de gérer les transactions de manière déclarative.
Elle permet de garantir le rollback en cas d'erreur dans les opérations qui impliquent plusieurs étapes.

</div>

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## DataSourceTransactionManager

::left::

````md magic-move
```kotlin
@Transactional
fun update() {
  myDb.updateBalance("Alice", -100.00)
  myDb.updateBalance("Bob", 100.00)
}
```
```kotlin
@Transactional(transactionManager = "transactionManager")
fun update() {
  myDb.updateBalance("Alice", -100.00)
  myDb.updateBalance("Bob", 100.00)
}
```
````

<div v-click.at="1">

Le transactionalManager est un bean créé par la dépendance Spring Data JPA

</div>

<div v-click.at="4">

## /!\ RunetimeException

```kotlin
catch (e: RuntimeException)
```

Sur une exception "simple" pas de rollback

</div>

::right::

<div v-click.at="2">

pseudo code équivalent

```kotlin
fun transactionalUpdate() {
  val rollback = false
  val transactionId = startDbTransaction()
  try {
      update()
  } catch (e: RuntimeException) {
      rollback = true
  }
  
  if (rollback)
      rollback(transactionId)
  else
      commit(transactionId)
}
```

</div>

---
layout: full
class: text-left
---

## personnalisation de @Transactional

<div v-click>

**transactionManager**: Le nom du bean de transactionManager

Requis quand il y en a au moins deux (ex: Kafka + Datasource)

</div>

<div v-click>

**rollbackFor** et **noRollbackFor**:
Spécifie les exceptions qui ne doivent ou non entraîner de rollback.

</div>

<div v-click>

**timeout** : Définit un délai d'attente pour la transaction.
Le rollback est effectué si la transaction a dépasser ce délai,
mais ne stoppe pas l'exécution en cours.

</div>

<div v-click>

**propagation** : Définit le comportement de la transaction vis à vis d'un appel à une autre méhtode annotée

- REQUIRED (par défaut) : Utilise la transaction existante ou en crée une nouvelle si aucune n'existe
- NESTED : Crée une sous-transaction (savepoint)

</div>

<div v-click>

**isolation** : Isolation de la transaction des autres transactions (pas des sous transactions)

- DEFAULT: Utilise le niveau d'isolation par défaut de la base de données
- READ_UNCOMMITTED: Permet de lire les données non validées (dirty read)
- READ_COMMITTED: Permet de lire uniquement les données validées

</div>

---
layout: full
class: text-left
---

Exemple de personnalisation :

```java
@Transactional(transactionManager = "transactionManager",
               timeout = 30,
               propagation = Propagation.REQUIRED,
               isolation = Isolation.DEFAULT)
fun updateData() {
    // Logic to update data
}
```

---
layout: full
class: text-left
---

## Transaction chain

````md magic-move
```kotlin
class KafkaService {
  @Transactional("kafkaTransaction")
  fun publish() = {
    TODO()
  }
}
```
```kotlin
class KafkaService {
  @Transactional("kafkaTransaction")
  fun publish() = {
    TODO()
  }
}

class AnotherService(val db: DBService, val kafka: KafkaService) {
}
```
```kotlin
class KafkaService {
  @Transactional("kafkaTransaction")
  fun publish() = {
    TODO()
  }
}

class AnotherService(val db: DBService, val kafka: KafkaService) {
  @Transactional("dbTransaction")
  fun saveAndPublish() {
  }
}
```
```kotlin
class KafkaService {
  @Transactional("kafkaTransaction")
  fun publish() = {
    TODO()
  }
}

class AnotherService(val db: DBService, val kafka: KafkaService) {
  @Transactional("dbTransaction")
  fun saveAndPublish() {
    db.save()
    kafka.publish()
  }
}
```
```kotlin
class KafkaService {
  @Transactional("kafkaTransaction")
  fun publish() = {
    TODO() // <-- kafkaTransaction dans une dbTransaction
  }
}

class AnotherService(val db: DBService, val kafka: KafkaService) {
  @Transactional("dbTransaction")
  fun saveAndPublish() {
    db.save()
    kafka.publish()
  }
}
```
````

<div v-click.at="5">

On peut encapsuler les transactions

Une erreur dans une sous-transaction rollback la parent.

Une erreur dans la parent ne rollback pas une sous transaction

</div>

---
layout: full
class: text-left
---

## Hibernate - FetchType

Hibernate propose deux FetchType

<div v-click>

- **FetchType.EAGER** : Récupère les données directement

```kotlin
data class User (
  @OneToMany(mappedBy = "user",
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  var wishes: MutableList<Product> = mutableListOf(),
)

jpaRepo.findOne().wishes // une seule requête, findOne a tout chargé directement
```

</div>

<div v-click>

- **FetchType.LAZY** : Récupère les données à l'accès

```kotlin
data class User (
  @OneToMany(mappedBy = "user",
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  var wishes: MutableList<Product> = mutableListOf(),
)

jpaRepo.findOne().wishes // deux requêtes, findOne puis une seconde pour wishes
```

</div>

---
layout: full
class: text-left
---

## Hibernate & Transaction

```kotlin
class MyRepo(jpa: UserJpa) {
  @Transactional
  fun findUser() = jpa.findOne()
}

class MyService(myRepo: MyRepo) {
  fun do() {
    val u = myRepo.findUser()
    u.wishes()
  }
}
```

  Failure: failed to lazily initialize a collection of role: Users.wishes: could not initialize proxy - no Session

Il y a une exception car la transaction est finie,
donc hibernate ne peut plus relancer des requêtes pour récupérer les données.

---
layout: full
class: text-left
---

## Hibernate & Transaction

```kotlin
class MyRepo(jpa: UserJpa) {
  fun findUser() = jpa.findOne()
}

class MyService(myRepo: MyRepo) {
  @Transactional
  fun do() {
    val u = myRepo.findUser()
    u.wishes()
  }
}
```

---
layout: full
class: text-left
---

## TL;DR Transaction

### @Transactional

Permet de gérer les transactions de manière déclarative et de grantir le rollback en cas d'erreur.

### Spring jpa transaction

Par défaut,
Spring ouvre une transaction à chaque requête HTTP s'il y a la dépendance JPA

Ça peut-être désacitvé avec la propriété `spring.jpa.open-in-view=false`

---
layout: full
class: text-left
---

## TL;DR FetchType

### FetchType.EAGER

Utile quand on est sûr de toujours avoir besoin des données associées.

Peut entraîner des surcharges inutiles si les données associées sont volumineuses ou rarement utilisées.

La transaction peut etre coupe apres la récupération initiale.

### FetchType.LAZY

Utile pour optimiser les performances en ne chargeant les données associées que lorsque nécessaire.

Peut entraîner des exceptions si la transaction est fermée avant l'accès aux données associées.
