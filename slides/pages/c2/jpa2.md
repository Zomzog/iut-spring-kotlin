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
| <span v-mark.circle.red="1">1</span> | Discord      | DRACONEQUUS |
| <span v-mark.box.green="2">2</span> | Rainbow Dash | PEGASUS     |
| <span v-mark.circle.blue="3">3</span> | Pinkie Pie   | EARTH       |

<br>

| PonyId | Occupation |
| -- | -- |
| <span v-mark.circle.red="1">1</span> | Spirit of Chaos|
| <span v-mark.box.green="2">2</span> | Ruler of Equestria|
| <span v-mark.circle.blue="3">3</span> | Baker|

::right::

<div v-click.at="4">

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
| <span v-mark.circle.red="1">1</span> | Discord      |
| 2   | Rainbow Dash |
| <span v-mark.circle.blue="2">3</span> | Pinkie Pie   |

<br>

|id| PonyId | Occupation |
|--| -- | -- |
| 1 | <span v-mark.circle.red="1">1</span> | Spirit of Chaos|
| 2 | <span v-mark.circle.red="1">1</span> | Ruler of Equestria|
| 3 | <span v-mark.circle.blue="2">3</span> | Baker|

::right::

<div v-click.at="3">

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
| 1   | Discord      | <span v-mark.circle.red="1">1</span> |
| 2   | Rainbow Dash | <span v-mark.circle.red="1">1</span> |
| 3   | Pinkie Pie   | <span v-mark.circle.blue="2">3</span> |

<br>

|id| Occupation |
|--| -- |
| <span v-mark.circle.red="1">1</span> | Spirit of Chaos|
| 2 | Ruler of Equestria|
| <span v-mark.circle.blue="2">3</span> | Baker|

::right::

<div v-click.at="3">

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
glowSeed: 1144
---

::title::

## Jointures - @ManyToMany

::left::

| id  | name         |
| --- | ------------ |
| <span v-mark.circle.red="2">1</span> | Discord      |
| <span v-mark.box.green="3">2</span> | Rainbow Dash |
| <span v-mark.circle.blue="4">3</span> | Pinkie Pie   |

<br>

|id| Occupation |
|--| -- |
| <span v-mark.circle.red="2"><span v-mark.box.green="{ at: 3, padding: 8 }">1</span></span> | Spirit of Chaos|
| <span v-mark.circle.red="2">2</span> | Ruler of Equestria|
| <span v-mark.circle.blue="4">3</span> | Baker|

::right::

<div v-click.at="1">

|pony_id| occupation_id |
|--| -- |
| <span v-mark.circle.red="2">1</span> | <span v-mark.circle.red="2">1</span> |
| <span v-mark.circle.red="2">1</span> | <span v-mark.circle.red="2">2</span> |
| <span v-mark.box.green="3">2</span> | <span v-mark.box.green="3">1</span> |
| <span v-mark.circle.blue="4">3</span> | <span v-mark.circle.blue="4">3</span> |

</div>

<div v-click.at="5">

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

<style>
td, th { padding-top: 0.25rem; padding-bottom: 0.25rem; }
</style>

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
layout: TwoColumnsTitle
class: text-left
---

::title::

## One-To-One unidirectionnel

::left::

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

::right::

<div v-click class="db-tables">

users

| id | name |
| --- | --- |
| 1 | Discord |
| 2 | Rainbow Dash |
| 3 | Pinkie Pie |

phone

| id | **user_id** | phone_number |
| --- | --- | --- |
| 1 | 1 | 0601020304 |
| 2 | 2 | 0602030405 |
| 3 | 3 | 0603040506 |

</div>

<!--
La clé étrangère est sur phone : la relation ne se lit que de Phone vers User

Deux tables : user_id est sur phone, et il ne se répète pas
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## One-To-One bi-directionnel

::left::

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

::right::

<div v-click class="db-tables">

users

| id | name |
| --- | --- |
| 1 | Discord |
| 2 | Rainbow Dash |
| 3 | Pinkie Pie |

phone

| id | **user_id** | phone_number |
| --- | --- | --- |
| 1 | 1 | 0601020304 |
| 2 | 2 | 0602030405 |
| 3 | 3 | 0603040506 |

</div>

<!--
L'usage de var et du nullable permet de créer les objets puis les imbriquer

Avec un @JoinColumn de chaque côté, il y a deux clés étrangères : une par table

Le côté qui porte la clé étrangère est le côté propriétaire. L'autre côté dit mappedBy : il n'ajoute aucune colonne et relit celle du propriétaire

Le mapping devient bi-directionnel

La base est la même qu'en unidirectionnel : le bi-directionnel ne change que le code
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## One-To-Many unidirectionnel

::left::

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

::right::

<div v-click class="db-tables">

users

| id | name |
| --- | --- |
| 1 | Discord |
| 2 | Rainbow Dash |
| 3 | Pinkie Pie |

phone

| id | **user_id** | phone_number |
| --- | --- | --- |
| 1 | 1 | 0601020304 |
| 2 | 1 | 0602030405 |
| 3 | 3 | 0603040506 |

</div>

<!--
One-to-many unidirectionnel

MutableList : Hibernate remplace la collection, et on doit pouvoir y ajouter ou en retirer un élément

Deux tables : user_id est sur phone, même si PhoneEntity ne connaît pas User, et il se répète
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Many-To-One

::left::

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

::right::

<div v-click class="db-tables">

users

| id | name |
| --- | --- |
| 1 | Discord |
| 2 | Rainbow Dash |
| 3 | Pinkie Pie |

phone

| id | **user_id** | phone_number |
| --- | --- | --- |
| 1 | 1 | 0601020304 |
| 2 | 1 | 0602030405 |
| 3 | 3 | 0603040506 |

</div>

<!--
ManyToOne pour le rendre bi-directionnel

Le many porte la clé étrangère : c'est lui le propriétaire, le côté OneToMany passe en mappedBy

La cascade propage l'enregistrement du user à ses phones

La base est la même qu'en One-To-Many unidirectionnel
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Many-To-Many

::left::

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
    @ManyToMany(cascade = [CascadeType.PERSIST])
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
    @ManyToMany(cascade = [CascadeType.PERSIST])
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

::right::

<div v-click class="db-tables">

users

| id | name |
| --- | --- |
| 1 | Discord |
| 2 | Rainbow Dash |
| 3 | Pinkie Pie |

user_group

| **user_id** | **group_id** |
| --- | --- |
| 1 | 1 |
| 2 | 1 |
| 3 | 1 |
| 3 | 2 |

groups

| id |
| --- |
| 1 |
| 2 |

</div>

<!--
ManyToMany, il faut une table de jointure

Trois tables : user_id et group_id se répètent tous les deux dans user_group
-->

---
layout: full
class: text-left
---

## Cascades

| `CascadeType` | Se propage sur |
| --- | --- |
| `PERSIST` | `save()` d'une nouvelle entité (id null) |
| `MERGE` | `save()` d'une entité existante (id renseigné) |
| `REMOVE` | `delete()` |
| `ALL` | tous les types |

<br/>

`REFRESH` et `DETACH` existent, inclus dans `ALL`.

<br/>

<div v-click>

`ALL` inclut `REMOVE` : à garder pour les enfants qui appartiennent au parent.

</div>

<style>
:not(pre) > code {
  background: rgba(255, 255, 255, 0.12);
  color: inherit;
  padding: 0 0.3em;
  border-radius: 0.25em;
  font-size: 0.85em;
}
</style>

<!--
Un phone appartient à son user : ALL convient

Un groupe est partagé : avec ALL, supprimer un user tente de supprimer ses groupes, et la base refuse dès qu'un autre user y est lié. D'où PERSIST sur le Many-To-Many

Avec PERSIST seul sur phones, save() d'un user existant n'enregistre pas ses nouveaux phones : il insère une ligne vide, sans numéro ni user, et sans erreur
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## orphanRemoval

::left::

```kotlin
@OneToMany(mappedBy = "user", orphanRemoval = true)
val phones: MutableList<PhoneEntity> = mutableListOf(),
```

```kotlin
user.phones.removeIf { it.id == phoneId }
```

Retirer un phone de la liste le supprime de la base.

::right::

<div v-click class="db-tables">

| Sur `phones` | retirer de la liste | supprimer le user |
| --- | --- | --- |
| rien | rien | erreur FK |
| `cascade = [REMOVE]` | rien | supprimés |
| `orphanRemoval = true` | supprimé | supprimés |

</div>

<!--
orphanRemoval : un phone retiré de la liste est supprimé de la base

REMOVE ne réagit qu'à la suppression du user : un phone retiré de la liste reste en base

Sans rien, on ne peut pas supprimer un user qui a des phones : la clé étrangère l'en empêche
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
