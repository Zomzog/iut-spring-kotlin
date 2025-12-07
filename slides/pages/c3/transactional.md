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
    myDb.updateBalance("Alice", -100.00)
    myDb.updateBalance("Bob", 100.00)
    myDb.commitTransaction(tId)
  } catch (e: Exception) {
    myDb.rollbackTransaction(tId)
  }
}
```
````

</div>

---
layout: full
class: text-left
---

## @Transactional

L'annotation `@Transactional` de Spring Boot permet de gérer les transactions de manière déclarative.
Elle permet de garantir le rollback en cas d'erreur dans les opérations qui impliquent plusieurs étapes.

---
layout: full
class: text-left
---

## Transactional example

Ce code va faire les deux updates,
les valider en base,
puis sortir en erreur.

---
layout: full
class: text-left
---

## Transactional example

```kotlin
@Transactional
fun updateAndNotify() {
  myDb.updateValueAInDb()
  myDb.updatrValueBInDb()
  throw RuntimeException("Failure")
}
```

Ce code va faire les deux updates,
puis sortir en erreur.

Le transactionalManager va annuler la transaction.

---
layout: full
class: text-left
---

## DataSourceTransactionManager

Le transactionalManager va faire un proxy qui en pseudo code donne:

```kotlin
val rollback = false
val transactionId = startDbTransaction()
try {
    updateAndNotify()
} catch (e: RuntimeException) {
    rollback = true
}

if (rollback)
    rollback(transactionId)
else
    commit(transactionId)
```

---
layout: full
class: text-left
---

## RunetimeException

[WARNING]
====
} catch (e: RuntimeException) {

Sur une exception "simple" pas de rollback
====

---
layout: full
class: text-left
---

## personnalisation de @Transactional

**transactionManager**: Le nom du bean de transactionManager

Requis quand il y en a au moins deux (ex: Kafka + Datasource)

---
layout: full
class: text-left
---

## personnalisation de @Transactional

**rollbackFor** et **noRollbackFor**

Spécifie les exceptions qui ne doivent ou non entraîner de rollback.

---
layout: full
class: text-left
---

## personnalisation de @Transactional

**timeout** : Définit un délai d'attente pour la transaction.

---
layout: full
class: text-left
---

## personnalisation de @Transactional

**propagation** : Définit le comportement de la transaction vis à vis d'un appel à une autre méhtode annotée

---
layout: full
class: text-left
---

## personnalisation de @Transactional

**isolation** : Isolation de la transaction des autres transactions (pas des sous transactions)

Avec une isolation faible,
on peut avoir accès aux données non commitées par une autre transaction

Exemple de personnalisation :

[source,java]

```
@Transactional(rollbackFor = SQLException.class, timeout = 30, isolation = Isolation.READ_COMMITTED)
public void updateData() {
    // Logic to update data
}
```

---
layout: full
class: text-left
---

## Transaction chain

```kotlin
class A {
  @Transactional("kafkaTransaction")
  fun call()
}
class B(val a: A) {
  @Transactional("dbTransaction")
  fun call() {
    a.call()
    throw RuntimeException("Nope")
  }
}
```

On peut encapsuler les transactions

Une erreur dans une sous-transaction rollback la parent.

Une erreur dans la parent ne rollback pas une sous transaction

---
layout: full
class: text-left
---

## Hibernate - FetchType

Hibernate propose deux FetchType

* **FetchType.EAGER** : Récupère les données directement
* **FetchType.LAZY** : Récupère les données à l'accès

---
layout: full
class: text-left
---

## Hibernate - FetchType

```kotlin
data class User (
  @OneToMany(mappedBy = "user",
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  var wishes: MutableList<Product> = mutableListOf(),
)
```

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

## Spring jpa transaction

[TIP]
====
Par défaut,
Spring ouvre une transaction à chaque requête HTTP s'il y a la dépendance JPA

Ça peut-être désacitvé avec

`spring.jpa.open-in-view=false`
====
