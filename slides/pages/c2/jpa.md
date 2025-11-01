---
layout: cover
hideInToc: false
---

# Accès à la base de données SQL

---
layout: full
class: text-left
---

## Accès à la base de données SQL

<v-click>Spring Data JPA</v-click>

<v-click>jOOQ</v-click>

<v-click>MyBatis</v-click>

<!--
Plusieurs choix, on va parler Spring Data
c'est le plus commun
-->

---
layout: full
class: text-left
---

## JPA - Pourquoi?

Spring est basé sur des POJO

Pour les contrôleurs on représente les JSON sous forme de classes

C'est pareil pour les bases de données

[.columns]
---
layout: full
class: text-left
---

## Base relationnel

[.column]
[cols="1,1,1"]
|===
|Id
|Name
|Kind

|1
|Discord
|Draconequus

|2
|Rainbow Dash
|Pegasus

|3
|Pinkie Pie
|Earth
|===

[.column]
-->
-->

[.column]
[cols="1,2"]
|===
|PonyId
|Occupation

|1
|Honorary

|1
|Ruler of Equestria

|3
|Baker
|===

<!--
Un example de BDD relationnel avec un 1-n
-->

---
layout: full
class: text-left
---

## Kotlin Version

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

<!--
En Kotlin on voudrait avoir ça, un objet avec une list d'un autre objet
-->

---
layout: full
class: text-left
---

## Entity

```kotlin
@Entity
class Pony(
    @Id @GeneratedValue
    val id: Long?,
    val name: String,
    val kind: String,
)
```

<!--
@Entity précise que l'objet sera utilisé en JPA

@Id donne l'id qui sera utilisé par JPA pour manipuler l'objet

@GeneratedValue explique comment l'id est crée, ici par la BDD
-->

[transition=fade-in, fade-out]
---
layout: full
class: text-left
---

## EntityManager

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

<!--
PersistenceUnit est une annotation spécifique,
qui permet de sortir de Spring pour obtenir l'EMF de Jakarta-JPA
-->

[transition=fade-in, fade-out]
---
layout: full
class: text-left
---

## EntityManager

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
    em.createQuery("SELECT pony from Pony pony WHERE p.name = :ponyName", Pony::class.java)
      .setParameter("ponyName", "Pinkie")
      .getResultList();
  }
}
```

<!--
JPQL pour du select
-->

---
layout: full
class: text-left
---

## JPQL

[source, sql]
-->
SELECT p
FROM Pony p
WHERE p.name = :ponyName
ORDER BY p.kind ASC
-->

<!--
Pony p fait référence à une Entity et non une Table
:ponyname est un parametre
-->

---
layout: full
class: text-left
---

## JPQL

[source, sql]
-->
SELECT NEW bzh.zomzog.Partial(p.name, p.kind)
FROM Pony p
WHERE p.name = :ponyName
ORDER BY p.kind ASC
-->

<!--
On peut appeler des constructeurs pour créer des objets sans lien avec l'entity
-->

---
layout: full
class: text-left
---

## EntiyManager with Spring

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
      em.createQuery("SELECT pony from Pony pony", Pony::class.java)
          .getResultList();
  }
}
```

<!--
Reprise de l'exemple précédent pour un diff facile
-->

---
layout: full
class: text-left
---

## EntiyManager with Spring

```kotlin
class Repository {
  
  @PersistenceContext
  private lateinit var entityManager: EntityManager

  @Transaction
  fun save(pony: Pony) = entityManager.persist(pony)

fun findAll() = entityManager.createQuery("SELECT pony from Pony pony",
                                            Pony::class.java)
      .resultList
}
```

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

```kotlin
@Entity
@NamedQuery(name = "Pony.findAll", query = "SELECT p FROM Pony p")
class Pony(
    @Id @GeneratedValue
    val id: Long?,
    val name: String,
    val kind: String,
)
```

```kotlin
fun findAll() = entityManager.createNamedQuery("Pony.findAll",
                                                Pony::class.java)
    .resultList
```

---
layout: full
class: text-left
---

## Spring JPA

```kotlin
interface JpaRepository<ENTITY, ID>
```

[source, kotlin, step=1]

```
interface DemoRepository : JpaRepository<DemoEntity, UUID>
```

---
layout: full
class: text-left
---

## Jpa Repository

```kotlin
fun save(entity: T): T

fun findAll(): List<T>;

fun findById(id: ID): Optional<T>

fun deleteById(id: ID): Unit

fun deleteAll(): Unit
```

---
layout: full
class: text-left
---

## Jpa Repository Custom Query

```kotlin
interface DemoRepository : JpaRepository<DemoEntity, UUID> {

    fun findAllByName(name: String): List<DemoEntity>

    fun findByAgeAndNameOrKindOrderByIdDesc(age: Int,
                                            name: String,
                                            kind: String): List<DemoEntity>
}
```

link:<https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.query-creation[Doc> de la syntax]

<!--
On peut faire des requêtes par convention de nommage
-->

---
layout: full
class: text-left
---

## Jpa Custom Query

```kotlin
@Query(value = "SELECT d from DemoEntity d where d.name = :name")
fun manual(name: String): List<DemoEntity>
```

[source, kotlin, step=1]

```
@Query(value = """SELECT d from DemoEntity d
    where (:name is null or d.name = :name)""")
fun manual(name: String?): List<DemoEntity>
```

<!--
On peut utiliser du JPQL

Ne pas hésiter à utiliser les string template
-->

---
layout: full
class: text-left
---

## Jpa Criteria

```kotlin
interface DemoRepositoryCustom {
    fun criteria(name: String?): List<DemoEntity>
}

interface DemoRepository :
        JpaRepository<DemoEntity, UUID>,
        DemoRepositoryCustom
```

<!--
On peut étendre le JpaRepository pour faire du Criteria custom
-->

---
layout: full
class: text-left
---

## Jpa Criteria

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

<!--
On retrouve le PersistenceContext bas niveau.

On peut faire une requête par criteria
-->

---
layout: full
class: text-left
---

## Entity

```kotlin
@Entity

class Pony(
    @Id @GeneratedValue
    val id: Long?,

    val name: String,
    val kind: String,
)
```

---
layout: full
class: text-left
---

## Entity

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

<!--
On peut changer le nom de la table
Utile pour ne pas lier le code
et en cas de conflit (table User réservé)
-->

---
layout: full
class: text-left
---

## Entity

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
<!--
On peut spécifier et renommer les colonnes

Utile si on utilise hibernate pour créer les tables
-->

---
layout: full
class: text-left
---

## Jointures

En SQL pour gérer des données hiérarchiques on utilise des jointures.

En JPA elles sont représenté par quatres annotations:

- @OneToOne
- @OneToMany
- @ManyToOne
- @ManyToMany

---
layout: full
class: text-left
---

## Cascades

La cascade est la propagation d'une modification aux enfants de l'entité.

Si l'objet A contient l'objet B,
lors d'un "update" de A en base, je peux vouloir
modifier/ajouter/supprimer l'objet B ou ignorer toutes les modifications de B

---
layout: full
class: text-left
---

## Direction

Une relation peut être uni-directionnel ie je ne peux aller que de l'objet A vers l'objet B

ou bi-directionnel ie je peux aller de A à B et de B à A.

---
layout: full
class: text-left
---

## Join-Column

L'annotation @JoinColumn permet de fournir à hibernate des informations sur la manière de lier les entités.

name: nom de la foreign key

referencedColumnName : le nom de la colonne de l'autre entité utilisé pour la jointure.

```kotlin
@JoinColumn(referencedColumnName = "email")
```

---
layout: full
class: text-left
---

## One-To-One uni-directionnel

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phone: PhoneEntity,
) {
@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id // Doit être unique, peut aussi être un @Column(unique = true)
        val email: String,
        val number: String,
)
```

<!--
Reference par nom de colonnes, la plus simple mais uni-directionnel
-->

---
layout: full
class: text-left
---

## One-To-One

```kotlin
class UserEntity(
        @Id val email: String,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "fk_email")
        var phone: PhoneEntity?,
)

class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @OneToOne(mappedBy = "phone")
        val user: UserEntity,
        val number: String,
)
```

<!--
L'usage de var et du nullable permet de créer les objets puis les imbriquer

Le mapping devient bi-directionnel
-->

---
layout: full
class: text-left
---

## One-To-One

```kotlin
class UserEntity(
        @Id val email: String,
        @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
        var phone: PhoneEntity?,
)

class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @OneToOne
        val user: UserEntity,
        val number: String,
)
```

<!--

-->

---
layout: full
class: text-left
---

## One-To-Many uni-directionnel

```kotlin
class UserEntity(
        @Id val email: String,
        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phones: List<PhoneEntity> = emptyList(),
)

class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        val email: String,
        val number: String,
)
```

<!--
One-to-many uni-directionnel
-->

---
layout: full
class: text-left
---

## Many-To-One

```kotlin
class UserEntity(
        @Id val email: String,
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
        var phones: List<PhoneEntity> = emptyList(),
)

class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @ManyToOne
        @JoinColumn(name="fk_email")
        val user: UserEntity?,
        val number: String,
)
```

<!--
ManyToOne pour le rendre bi-directionnel
-->

---
layout: full
class: text-left
---

## Many-To-Many

```kotlin
class UserEntity(
        @Id val email: String,
        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "user_phone",
                joinColumns = [JoinColumn(name = "email")],
                inverseJoinColumns = [JoinColumn(name = "id")])
        var phones: List<PhoneEntity> = emptyList(),
)
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @ManyToMany
        val user: List<UserEntity>,
        val number: String,
)
```

<!--
ManyToMany, il faut une table de jointure
-->

---
layout: full
class: text-left
---

## Test Jpa

Spring propose des tests de "Layer".

Ces tests ne lancent qu'une partie de l'application.

Pour JPA, il faut remplacer @SpringBootTest par @DataJpaTest.

---
layout: full
class: text-left
---

## Test Jpa

```kotlin
@DataJpaTest
class DemoRepositoryTest {
    @Autowired
    private lateinit var jpaRepository: DemoRepository

    @Test
    fun `find one existing`() {
       // GIVEN
        jpaRepository.save(DemoEntity(randomUUID(), "name"))
        // WHEN
        val result = jpaRepository.findAllByName("name")
        // THEN
        assertThat(result).hasSize(1)
    }
}
```
