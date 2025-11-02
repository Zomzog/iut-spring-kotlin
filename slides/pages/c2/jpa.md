---
layout: cover
hideInToc: false
---

# Accès à la base de données SQL

---
layout: full
---

## Accès à la base de données SQL

<div v-click>

<span v-mark.box.red="4"> Spring Data JPA</span>

</div>

<div v-click>

jOOQ

</div>

<div v-click>

MyBatis

</div>

<!--
Plusieurs choix, on va parler Spring Data
c'est le plus commun
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Base relationnel

::left::

| id  | name         | kind        |
| --- | ------------ | ----------- |
| 1   | Discord      | DRACONEQUUS |
| 2   | Rainbow Dash | PEGASUS     |
| 3   | Pinkie Pie   | EARTH       |

<br>

<div v-click>

|id| PonyId | Occupation |
|--| -- | -- |
| 1 | 1 | Spirit of Chaos|
| 2 | 1 | Ruler of Equestria|
| 3 | 3 | Baker|

</div>

::right::

<div v-click>

```json
{
  "id": 1,
  "name": "Dsicord",
  "kind": "DRACONEQUUS",
  "occupation": [
    "Spirit of Chaos",
    "Ruler of Equestria"
  ]
}
```

</div>

<div v-click>

```kotlin
class Pony(
    val id: Long?,
    val name: String,
    val kind: String,
    val occupations: List<Occupation>,
)

class Occupation(
    val name: String,
)
```

</div>

<!--
Un example de BDD relationnel avec un 1-n
-->

---
layout: full
class: text-left
---

## Entity

````md magic-move
```kotlin
class Pony(
    val id: Long?,
    val name: String,
    val kind: String,
)
```
```kotlin
@Entity
class Pony(
    val id: Long?,
    val name: String,
    val kind: String,
)
```
```kotlin
@Entity
class Pony(
    @Id @GeneratedValue
    val id: Long?,
    val name: String,
    val kind: String,
)
```
````

<div v-click.at="3">

## EntityManager

````md magic-move {at:'3'}
```kotlin
```
```kotlin
class Repository {
  @PersistenceUnit
  private lateinit var factory: EntityManagerFactory
}
```
```kotlin
class Repository {
  @PersistenceUnit
  private lateinit var factory: EntityManagerFactory

  fun save(pony: Pony) = factory.createEntityManager().use { em ->
    em.transaction.begin()
  }
}
```
```kotlin
class Repository {
  @PersistenceUnit
  private lateinit var factory: EntityManagerFactory

  fun save(pony: Pony) = factory.createEntityManager().use { em ->
    em.transaction.begin()
    em.persist(pony)
  }
}
```
```kotlin
class Repository {
  @PersistenceUnit
  private lateinit var factory: EntityManagerFactory

  fun save(pony: Pony) = factory.createEntityManager().use { em ->
    em.transaction.begin()
    em.persist(pony)
    em.transaction.commit()
  }
}
```

```kotlin
class Repository {
  @PersistenceUnit
  private lateinit var factory: EntityManagerFactory

  fun save(pony: Pony) = factory.createEntityManager().use { em ->
    em.transaction.begin()
    em.persist(pony)
    em.transaction.commit()
  }
  fun findAll(): List<Pony> = factory.createEntityManager().use { em ->
    em.createQuery()
  }
}
```
```kotlin
class Repository {
  @PersistenceUnit
  private lateinit var factory: EntityManagerFactory

  fun save(pony: Pony) = factory.createEntityManager().use { em ->
    em.transaction.begin()
    em.persist(pony)
    em.transaction.commit()
  }
  fun findAll(): List<Pony> = factory.createEntityManager().use { em ->
    em.createQuery("SELECT p from Pony p WHERE p.name = :ponyName")
      .setParameter("ponyName", "Pinkie")
  }
}
```
```kotlin
class Repository {
  @PersistenceUnit
  private lateinit var factory: EntityManagerFactory

  fun save(pony: Pony) = factory.createEntityManager().use { em ->
    em.transaction.begin()
    em.persist(pony)
    em.transaction.commit()
  }
  fun findAll(): List<Pony> = factory.createEntityManager().use { em ->
    em.createQuery("SELECT p from Pony p WHERE p.name = :ponyName", Pony::class.java)
      .setParameter("ponyName", "Pinkie")
  }
}
```
```kotlin
class Repository {
  @PersistenceUnit
  private lateinit var factory: EntityManagerFactory

  fun save(pony: Pony) = factory.createEntityManager().use { em ->
    em.transaction.begin()
    em.persist(pony)
    em.transaction.commit()
  }
  fun findAll(): List<Pony> = factory.createEntityManager().use { em ->
    em.createQuery("SELECT p from Pony p WHERE p.name = :ponyName", Pony::class.java)
      .setParameter("ponyName", "Pinkie")
      .getResultList();
  }
}
```
````

</div>

<!--
@Entity précise que l'objet sera utilisé en JPA

@Id donne l'id qui sera utilisé par JPA pour manipuler l'objet

@GeneratedValue explique comment l'id est crée, ici par la BDD

PersistenceUnit est une annotation spécifique,
qui permet de sortir de Spring pour obtenir l'EMF de Jakarta-JPA

JPQL pour du select
-->

---
layout: full
class: text-left
---

## JPQL

```sql{all|2|3|all}
SELECT p
FROM Pony p
WHERE p.name = :ponyName
ORDER BY p.kind ASC
```

<div v-after>

````md magic-move {at:'3'}
```sql
SELECT NEW bzh.zomzog.Partial(p.name, p.kind)
```

```sql{1|all}
SELECT NEW bzh.zomzog.Partial(p.name, p.kind)
FROM Pony p
WHERE p.name = :ponyName
ORDER BY p.kind ASC
```
````

</div>

<!--
Pony p fait référence à une Entity et non une Table
:ponyname est un parametre

On peut appeler des constructeurs pour créer des objets sans lien avec l'entity
-->

---
layout: full
class: text-left
---

## EntiyManager with Spring

````md magic-move
```kotlin
class Repository {

  @PersistenceUnit
  private lateinit var factory: EntityManagerFactory

  fun save(pony: Pony) = factory.createEntityManager().use { em ->
    em.transaction.begin()
    em.persist(pony)
    em.transaction.commit()
  }
  fun findAll(): List<Pony> = factory.createEntityManager().use { em ->
    em.createQuery("SELECT p from Pony p WHERE p.name = :ponyName", Pony::class.java)
      .setParameter("ponyName", "Pinkie")
      .getResultList();
  }
}
```
```kotlin {3-4}
class Repository {

  @PersistenceContext
  private lateinit var entityManager: EntityManager

  fun save(pony: Pony) = factory.createEntityManager().use { em ->
    em.transaction.begin()
    em.persist(pony)
    em.transaction.commit()
  }
  fun findAll(): List<Pony> = factory.createEntityManager().use { em ->
    em.createQuery("SELECT p from Pony p WHERE p.name = :ponyName", Pony::class.java)
      .setParameter("ponyName", "Pinkie")
      .getResultList();
  }
}
```
```kotlin{6-7}
class Repository {

  @PersistenceContext
  private lateinit var entityManager: EntityManager

  @Transactional
  fun save(pony: Pony) = entityManager.persist(pony)

  fun findAll(): List<Pony> = factory.createEntityManager().use { em ->
    em.createQuery("SELECT p from Pony p WHERE p.name = :ponyName", Pony::class.java)
      .setParameter("ponyName", "Pinkie")
      .getResultList();
  }
}
```
```kotlin{9-13}
class Repository {

  @PersistenceContext
  private lateinit var entityManager: EntityManager

  @Transactional
  fun save(pony: Pony) = entityManager.persist(pony)

  fun findAll(): List<Pony> = entityManager
      .createQuery("SELECT p from Pony p", Pony::class.java)
      .resultList
  }
}
```
````

<!--
1. Passage à PersistenceContext
2. Passage à EntityManager directement
3. Utilisation de @Transaction pour gérer la transaction (car on ne peut plus utilisé celle de jakarta)
-->

---
layout: full
class: text-left
---

## Named Query

````md magic-move
```kotlin
@Entity
class Pony(
    @Id @GeneratedValue
    val id: Long?,
    val name: String,
    val kind: String,
)
```
```kotlin {2|all}
@Entity
@NamedQuery(name = "Pony.findAll", query = "SELECT p FROM Pony p")
class Pony(
    @Id @GeneratedValue
    val id: Long?,
    val name: String,
    val kind: String,
)
```
````

````md magic-move {at: '2'}
```kotlin
fun findAll(): List<Pony> = entityManager
    .createQuery("SELECT p from Pony p", Pony::class.java)
    .resultList
```
```kotlin {2|all}
fun findAll() = entityManager
    .createNamedQuery("Pony.findAll", Pony::class.java)
    .resultList
```
````

---
layout: full
class: text-left
---

## Spring JPA

````md magic-move
```kotlin
interface JpaRepository<ENTITY, ID> {
```
```kotlin
interface JpaRepository<ENTITY, ID> {

  fun save(entity: ENTITY): ENTITY

  fun findAll(): List<ENTITY>

  fun findById(id: ID): Optional<ENTITY>

  fun deleteById(id: ID): Unit

  fun deleteAll(): Unit
}
```
````

<div v-click>

```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID>
```

</div>

---
layout: full
class: text-left
---

```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID>
```

<div v-click.at='1'>
<span v-mark.red="{ at: 2, strokeWidth: 3, type: 'crossed-off' }">

```kotlin
class DemoRepository : JpaRepository<DemoEntity, UUID>
```

</span>

</div>

<div v-click.at='3'>
<span v-mark.red="{ at: 4, strokeWidth: 3, type: 'crossed-off' }">

```kotlin
@Repository
interface DemoRepository : JpaRepository<DemoEntity, UUID>
```

</span>
</div>

<div v-click.at='5'>

<span v-mark.red="{ at: 6, strokeWidth: 3, type: 'crossed-off' }">

```kotlin
interface DemoRepository : JpaRepository<UUID, DemoEntity>
```

</span>

</div>

---
layout: full
class: text-left
---

## Jpa Repository Custom Query

````md magic-move
```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID> {
}
```
```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID> {

    fun findAllByName(name: String): List<DemoEntity>
}
```
```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID> {

    fun findAllByName(name: String): List<DemoEntity>

    fun findAllByIdDesc(): List<DemoEntity>
}
```
```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID> {

    fun findAllByName(name: String): List<DemoEntity>

    fun findAllByIdDesc(): List<DemoEntity>

    fun findAllByAgeLessThanEqual(age: Int): List<DemoEntity>
}
```
```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID> {

    fun findAllByName(name: String): List<DemoEntity>

    fun findAllByIdDesc(): List<DemoEntity>

    fun findAllByAgeLessThanEqual(age: Int): List<DemoEntity>

    fun findAllByNameNotIn(names: List<String> ): List<DemoEntity>
}
```
```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID> {

    fun findAllByName(name: String): List<DemoEntity>

    fun findAllByIdDesc(): List<DemoEntity>

    fun findAllByAgeLessThanEqual(age: Int): List<DemoEntity>

    fun findAllByNameNotIn(names: List<String> ): List<DemoEntity>

    fun findByAgeLessThanEqualAndNameNotInOrKindOrderByIdDesc(age: Int,
                                            name: List<String>,
                                            kind: String): List<DemoEntity>
}
```
````

<div v-click>

[Documentation de la syntax](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.query-creation)
</div>

<!--
On peut faire des requêtes par convention de nommage
-->

---
layout: full
class: text-left
---

## Jpa Custom Query

````md magic-move
```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID> {
}
```

```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID> {
  @Query(value = "SELECT d from DemoEntity d where d.name = :name")
  fun manual(name: String): List<DemoEntity>
}
```

```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID> {
  @Query(value = "SELECT d from DemoEntity d where d.name = :name")
  fun manual(name: String): List<DemoEntity>

  @Query(value = """SELECT d from DemoEntity d
    where (:name is null or d.name = :name)""")
  fun manualOrNul(name: String?): List<DemoEntity>
}
```
````

<!--
On peut utiliser du JPQL

Ne pas hésiter à utiliser les string template
-->

---
layout: full
class: text-left
---

## Jpa Criteria

````md magic-move
```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID> {
}
```
```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID>,
                           DemoRepositoryCustom {
}
```
````

<div v-click.at='1'>

````md magic-move {at: '0'}
```kotlin
interface DemoRepositoryCustom {
}
```
```kotlin
interface DemoRepositoryCustom {
    fun criteria(name: String?): List<DemoEntity>
}
```
````

</div>

<div v-click.at='3'>

````md magic-move {at: '3'}
```kotlin
class DemoRepositoryCustomImpl : DemoRepositoryCustom {
}
```
```kotlin
class DemoRepositoryCustomImpl : DemoRepositoryCustom {
    @PersistenceContext
    private lateinit var entityManager: EntityManager
}
```
```kotlin
class DemoRepositoryCustomImpl : DemoRepositoryCustom {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun criteria(name: String?): List<DemoEntity> {
        val criteriaBuilder = entityManager.criteriaBuilder
    }
}
```
```kotlin
class DemoRepositoryCustomImpl : DemoRepositoryCustom {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun criteria(name: String?): List<DemoEntity> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val queryBuilder = criteriaBuilder.createQuery(DemoEntity::class.java)
    }
}
```
```kotlin
class DemoRepositoryCustomImpl : DemoRepositoryCustom {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun criteria(name: String?): List<DemoEntity> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val queryBuilder = criteriaBuilder.createQuery(DemoEntity::class.java)
        val root: Root<DemoEntity> = queryBuilder.from(DemoEntity::class.java)
    }
}
```
```kotlin
class DemoRepositoryCustomImpl : DemoRepositoryCustom {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun criteria(name: String?): List<DemoEntity> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val queryBuilder = criteriaBuilder.createQuery(DemoEntity::class.java)
        val root: Root<DemoEntity> = queryBuilder.from(DemoEntity::class.java)
        var query = queryBuilder.select(root)
    }
}
```
```kotlin
class DemoRepositoryCustomImpl : DemoRepositoryCustom {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun criteria(name: String?): List<DemoEntity> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val queryBuilder = criteriaBuilder.createQuery(DemoEntity::class.java)
        val root: Root<DemoEntity> = queryBuilder.from(DemoEntity::class.java)
        var query = queryBuilder.select(root)
        if (name != null) {
            val nameField: Path<DemoEntity> = root.get("name")
            query = query.where(criteriaBuilder.equal(nameField, name))
        }
    }
}
```
```kotlin
class DemoRepositoryCustomImpl : DemoRepositoryCustom {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    override fun criteria(name: String?): List<DemoEntity> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val queryBuilder = criteriaBuilder.createQuery(DemoEntity::class.java)
        val root: Root<DemoEntity> = queryBuilder.from(DemoEntity::class.java)
        var query = queryBuilder.select(root)
        if (name != null) {
            val nameField: Path<DemoEntity> = root.get("name")
            query = query.where(criteriaBuilder.equal(nameField, name))
        }
        return entityManager.createQuery(query).resultList
    }
}
```
````

</div>

<!--
On peut étendre le JpaRepository pour faire du Criteria custom

On retrouve le PersistenceContext bas niveau.

On peut faire une requête par criteria
-->

---
layout: full
class: text-left
---

## Entity

````md magic-move
```kotlin
@Entity
class Pony(
    @Id @GeneratedValue
    val id: Long?,
    val name: String,
    val kind: String,
)
```
```kotlin
@Entity
@Table("PonyTable")
class Pony(
    @Id @GeneratedValue
    val id: Long?,
    val name: String,
    val kind: String,
)
```
```kotlin
@Entity
@Table("PonyTable")
class Pony(
    @Id @GeneratedValue
    val id: Long?,
    @Column(name = "n")
    val name: String,
    val kind: String,
)
```
```kotlin
@Entity
@Table("PonyTable")
class Pony(
    @Id @GeneratedValue
    val id: Long?,
    @Column(name = "n", nullable = false)
    val name: String,
    val kind: String,
)
```
```kotlin
@Entity
@Table("PonyTable")
class Pony(
    @Id @GeneratedValue
    val id: Long?,
    @Column(name = "n", nullable = false, unique = false)
    val name: String,
    val kind: String,
)
```
````

<!--
On peut changer le nom de la table
Utile pour ne pas lier le code
et en cas de conflit (table User réservé)

On peut spécifier et renommer les colonnes

Utile si on utilise hibernate pour créer les tables
-->
