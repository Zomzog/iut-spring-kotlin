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
    val occupations: Occupation,
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
    val occupations: Occupation,
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

Une relation peut être uni-directionnel ie je ne peux aller que de l'objet A vers l'objet B

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

## One-To-One uni-directionnel

````md magic-move
```kotlin
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id // Doit être unique, peut aussi être un @Column(unique = true)
        val email: String,
        val number: String,
)
```
```kotlin {5}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToOne
        val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id // Doit être unique, peut aussi être un @Column(unique = true)
        val email: String,
        val number: String,
)
```
```kotlin {5-6}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToOne
        @JoinColumn(referencedColumnName = "email")
        val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id // Doit être unique, peut aussi être un @Column(unique = true)
        val email: String,
        val number: String,
)
```
```kotlin{5-6|all}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id // Doit être unique, peut aussi être un @Column(unique = true)
        val email: String,
        val number: String,
)
```
````

<!--
Reference par nom de colonnes, la plus simple mais uni-directionnel
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
        @Id val email: String,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id // Doit être unique, peut aussi être un @Column(unique = true)
        val email: String,
        val number: String,
)
```
```kotlin{15-17}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id // Doit être unique, peut aussi être un @Column(unique = true)
        val email: String,
        @OneToOne
        @JoinColumn(referencedColumnName = "email")
        val user: UserEntity,
        val number: String,
)
```
```kotlin{15-16|all}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id // Doit être unique, peut aussi être un @Column(unique = true)
        val email: String,
        @OneToOne(mappedBy = "phone")
        val user: UserEntity,
        val number: String,
)
```
```kotlin{13}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @OneToOne(mappedBy = "phone")
        val user: UserEntity,
        val number: String,
)
```
```kotlin{5-6|all}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "fk_email")
        var phone: PhoneEntity?,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @OneToOne(mappedBy = "phone")
        val user: UserEntity,
        val number: String,
)
```
````

<!--
L'usage de var et du nullable permet de créer les objets puis les imbriquer

Le mapping devient bi-directionnel
-->

---
layout: full
class: text-left
---

## One-To-Many uni-directionnel

````md magic-move
```kotlin
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        val phones: List<PhoneEntity> = emptyList(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        val email: String,
        val number: String,
)
```
```kotlin{7}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        val phones: List<PhoneEntity> = emptyList(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        val email: String,
        val number: String,
)
```
```kotlin{3-5|all}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phones: List<PhoneEntity> = emptyList(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        val email: String,
        val number: String,
)
```
````

<!--
One-to-many uni-directionnel
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
        @Id val email: String,
        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phones: List<PhoneEntity> = emptyList(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        val email: String,
        val number: String,
)
```
```kotlin{10-12}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phones: List<PhoneEntity> = emptyList(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @ManyToOne
        @JoinColumn(name="fk_email")
        val user: UserEntity?,
        val number: String,
)
```
```kotlin{3|all}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
        var phones: List<PhoneEntity> = emptyList(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @ManyToOne
        @JoinColumn(name="fk_email")
        val user: UserEntity?,
        val number: String,
)
```
````
<!--
ManyToOne pour le rendre bi-directionnel
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
        @Id val email: String,
        var phones: List<PhoneEntity> = emptyList(),
)
@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        val user: List<UserEntity>,
        val number: String,
)
```
```kotlin{5-9}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "user_phone",
                joinColumns = [JoinColumn(name = "email")],
                inverseJoinColumns = [JoinColumn(name = "id")])
        var phones: List<PhoneEntity> = emptyList(),
)
@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        val user: List<UserEntity>,
        val number: String,
)
```
```kotlin{16|all}
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "user_phone",
                joinColumns = [JoinColumn(name = "email")],
                inverseJoinColumns = [JoinColumn(name = "id")])
        var phones: List<PhoneEntity> = emptyList(),
)
@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @ManyToMany
        val user: List<UserEntity>,
        val number: String,
)
```
````

---
layout: cover
hideInToc: false
---

## TL;DR Jointures

---
layout: full
class: text-left
---

## OneToOne - uni-directionnel

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id // Doit être unique, peut aussi être un @Column(unique = true)
        val email: String,
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

## OneToOne - bi-directionnel

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id // Doit être unique, peut aussi être un @Column(unique = true)
        val email: String,
        @OneToOne(mappedBy = "phone")
        val user: UserEntity,
        val number: String,
)
```

---
layout: full
class: text-left
---

## OneToOne - bi-directionnel

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "fk_email")
        var phone: PhoneEntity?,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @OneToOne(mappedBy = "phone")
        val user: UserEntity,
        val number: String,
)
```

---
layout: full
class: text-left
---

## OneToMany - uni-directionnel

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToMany(cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "email")
        val phones: List<PhoneEntity> = emptyList(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        val email: String,
        val number: String,
)
```

---
layout: full
class: text-left
---

## Many-To-One

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
        var phones: List<PhoneEntity> = emptyList(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @ManyToOne
        @JoinColumn(name="fk_email")
        val user: UserEntity?,
        val number: String,
)
```

---
layout: full
class: text-left
---

## One-To-Many

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
        var phones: List<PhoneEntity> = emptyList(),
)

@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @ManyToOne
        @JoinColumn(name="fk_email")
        val user: UserEntity?,
        val number: String,
)
```

---
layout: full
class: text-left
---

## Many-To-Many

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
        @Id val email: String,
        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "user_phone",
                joinColumns = [JoinColumn(name = "email")],
                inverseJoinColumns = [JoinColumn(name = "id")])
        var phones: List<PhoneEntity> = emptyList(),
)
@Entity
@Table(name = "phone")
class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        @ManyToMany
        val user: List<UserEntity>,
        val number: String,
)
```

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
