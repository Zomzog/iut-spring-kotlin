---
layout: full
class: text-left
---

## Jointures

En SQL pour gérer des données hiérarchiques on utilise des jointures.

```sql
SELECT * FROM pony p
LEFT JOIN occupation o ON p.id = o.pony_id
WHERE o.name = 'Spirit of Chaos';
```

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Jointures - @OneToOne

::left::

| id  | name         | kind        |
| --- | ------------ | ----------- |
| 1   | Discord      | DRACONEQUUS |
| 2   | Rainbow Dash | PEGASUS     |
| 3   | Pinkie Pie   | EARTH       |

| PonyId | Occupation |
| -- | -- |
| 1 | Spirit of Chaos|
| 2 | Ruler of Equestria|
| 3 | Baker|

::right::

<div v-click>

```kotlin
class Pony(
    val id: Long?,
    val name: String,
    val kind: String,
    val occupation: Occupation,
)

class Occupation(
    val ponyId: Long,
    val name: String,
)
```

</div>

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Jointures - @OneToMany

::left::

| id  | name         |
| --- | ------------ |
| 1   | Discord      |
| 2   | Rainbow Dash |
| 3   | Pinkie Pie   |

|id| PonyId | Occupation |
|--| -- | -- |
| 1 | 1 | Spirit of Chaos|
| 2 | 1 | Ruler of Equestria|
| 3 | 3 | Baker|

::right::

<div v-click>

```kotlin
class Pony(
    val id: Long?,
    val name: String,
    val kind: String,
    val occupations: List<Occupation>,
)

class Occupation(
    val id: Long?,
    val ponyId: Long,
    val name: String,
)
```

</div>

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Jointures - @ManyToOne

::left::

| id  | name         | occupation_id |
| --- | ------------ | ------------- |
| 1   | Discord      | 1             |
| 2   | Rainbow Dash | 2             |
| 3   | Pinkie Pie   | 3             |

|id| Occupation |
|--| -- |
| 1 | Spirit of Chaos|
| 2 | Ruler of Equestria|
| 3 | Baker|

::right::

<div v-click>

```kotlin
class Pony(
    val id: Long?,
    val name: String,
    val occupation: Occupation,
)

class Occupation(
    val id: Long?,
    val name: String,
)
```

</div>

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Jointures - @ManyToMany

::left::

| id  | name         |
| --- | ------------ |
| 1   | Discord      |
| 2   | Rainbow Dash |
| 3   | Pinkie Pie   |

|id| Occupation |
|--| -- |
| 1 | Spirit of Chaos|
| 2 | Ruler of Equestria|
| 3 | Baker|

::right::

<div v-click>

```kotlin
class Pony(
    val id: Long?,
    val name: String,
    val occupations: List<Occupation>,
)

class Occupation(
    val id: Long?,
    val name: String,
    val ponies: List<Pony>,
)
```

</div>

<div v-click>

|pony_id| occupation_id |
|--| -- |
| 1 | 1 |
| 2 | 1 |
| 3 | 2 |

</div>

---
layout: full
class: text-left
---

## Cascades

La cascade est la propagation d'une modification aux enfants de l'entité.

Si l'objet A contient l'objet B,
lors d'un "update" de A en base, je peux vouloir
modifier/ajouter/supprimer l'objet B ou ignorer toutes les modifications de B

<br/>

<div v-click>

## Direction

Une relation peut être unidirectionnelle ie je ne peux aller que de l'objet A vers l'objet B

ou bi-directionnel ie je peux aller de A à B et de B à A.

</div>

---
layout: full
class: text-left
---

## Join-Column

```kotlin
@JoinColumn(referencedColumnName = "email")
```

L'annotation @JoinColumn permet de fournir à hibernate des informations sur la manière de lier les entités.

name: nom de la foreign key

referencedColumnName : le nom de la colonne de l'autre entité utilisé pour la jointure.

---
layout: full
class: text-left
---

## One-To-One unidirectionnel

````md magic-move
```kotlin{all|14}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val user: UserEntity,
    val phoneNumber: String,
)
```
```kotlin {14-15}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @OneToOne
    val user: UserEntity,
    val phoneNumber: String,
)
```
```kotlin {14-16|all}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @OneToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)
```
````

<!--
La clé étrangère est sur phone : la relation ne se lit que de Phone vers User
-->

---
layout: full
class: text-left
---

## One-To-One bi-directionnel

````md magic-move
```kotlin
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @OneToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)
```
```kotlin {7}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    var phone: PhoneEntity? = null,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @OneToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)
```
```kotlin {7-9}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    @OneToOne
    @JoinColumn(name = "phone_id")
    var phone: PhoneEntity? = null,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @OneToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)
```
```kotlin {7-8|all}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    @OneToOne(mappedBy = "user")
    var phone: PhoneEntity? = null,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @OneToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)
```
````

<!--
L'usage de var et du nullable permet de créer les objets puis les imbriquer

Avec un @JoinColumn de chaque côté, il y a deux clés étrangères : une par table

Le côté qui porte la clé étrangère est le côté propriétaire. L'autre côté dit mappedBy : il n'ajoute aucune colonne et relit celle du propriétaire

Le mapping devient bi-directionnel
-->

---
layout: full
class: text-left
---

## One-To-Many unidirectionnel

````md magic-move
```kotlin
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    val phones: MutableList<PhoneEntity> = mutableListOf(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val phoneNumber: String,
)
```
```kotlin {7}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    @OneToMany
    val phones: MutableList<PhoneEntity> = mutableListOf(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val phoneNumber: String,
)
```
```kotlin {7-8|all}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    @OneToMany
    @JoinColumn(name = "user_id") // la FK est sur phone
    val phones: MutableList<PhoneEntity> = mutableListOf(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val phoneNumber: String,
)
```
````

<!--
One-to-many unidirectionnel

MutableList : Hibernate remplace la collection, et on doit pouvoir y ajouter ou en retirer un élément
-->

---
layout: full
class: text-left
---

## Many-To-One

````md magic-move
```kotlin
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    @OneToMany
    @JoinColumn(name = "user_id") // la FK est sur phone
    val phones: MutableList<PhoneEntity> = mutableListOf(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val phoneNumber: String,
)
```
```kotlin {17-19}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    @OneToMany
    @JoinColumn(name = "user_id") // la FK est sur phone
    val phones: MutableList<PhoneEntity> = mutableListOf(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)
```
```kotlin {7}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    @OneToMany(mappedBy = "user")
    val phones: MutableList<PhoneEntity> = mutableListOf(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)
```
```kotlin {7|all}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val phones: MutableList<PhoneEntity> = mutableListOf(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)
```
````

<!--
ManyToOne pour le rendre bi-directionnel

Le many porte la clé étrangère : c'est lui le propriétaire, le côté OneToMany passe en mappedBy

La cascade propage l'enregistrement du user à ses phones
-->

---
layout: full
class: text-left
---

## Many-To-Many

````md magic-move
```kotlin
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    val groups: MutableList<GroupEntity> = mutableListOf(),
)

@Entity
@Table(name = "groups")
class GroupEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val users: MutableList<UserEntity> = mutableListOf(),
)
```
```kotlin {7-13}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_group",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "group_id")],
    )
    val groups: MutableList<GroupEntity> = mutableListOf(),
)

@Entity
@Table(name = "groups")
class GroupEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val users: MutableList<UserEntity> = mutableListOf(),
)
```
```kotlin {21-22|all}
@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    val name: String,
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_group",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "group_id")],
    )
    val groups: MutableList<GroupEntity> = mutableListOf(),
)

@Entity
@Table(name = "groups")
class GroupEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @ManyToMany(mappedBy = "groups")
    val users: MutableList<UserEntity> = mutableListOf(),
)
```
````

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

```kotlin{1|3-4|all}
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
