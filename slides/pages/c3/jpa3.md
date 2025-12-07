---
layout: TwoColumnsTitle
class: text-left
---

::title::

## One-to-One Uni-directionnel

::left::

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
    @Id val email: String,
    val name: String,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(referencedColumnName = "email")
    val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id val email: String,
    val phoneNumber: String,
)
```

::right::

```sql
```

<!--
La clé étrangère est sur la table users, pointant vers phone
Relation uni-directionnelle: User -> Phone
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

::left::

```kotlin
@Entity
@Table(name = "phone")
class PhoneEntity(
    @OneToOne
    @JoinColumn(name = "user_id")
    val number: String,
    val phoneNumber: String,
)

@Table(name = "users")
class UserEntity(
    @Id val id: Long,
    val name: String
)
```

::right::

```sql
```

<!--
La clé étrangère est sur la table phone, pointant vers users
Relation uni-directionnelle: Phone -> User
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## One-to-One bidirectionnel (clé étrangère sur User)

::left::

```kotlin
@Entity
class UserEntity(
    @Id val id: Long,
    val name: String,
    @JoinColumn(name = "phone_id")
    val phone: PhoneEntity,
)
@Entity
@Table(name = "phone")
class PhoneEntity(
    @OneToOne(mappedBy = "phone")
    val user: UserEntity,
    val phoneNumber: String,
)
```

::right::

```sql
```

<!-- 
Clé étrangère sur users, navigation bidirectionnelle
-->
---
layout: TwoColumnsTitle
class: text-left
---

::title::

## One-to-One bidirectionnel (clé étrangère sur Phone)

::left::

```kotlin
@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id val id: Long,
    @OneToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val number: String,
    val phoneNumber: String,
)

@Entity
@Table(name = "users")
class UserEntity(
    @Id val id: Long,
    val name: String,
    @OneToOne(mappedBy = "user")
    val phone: PhoneEntity,
)
```

::right::

```sql
```

<!--
Clé étrangère sur phone, navigation bidirectionnelle
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## One-to-One avec table de jointure (IDs séparés)

::left::

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
    @Id val id: Long,
    val name: String,
    @OneToOne
    @JoinTable(
        name = "user_phone",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "phone_id")]
    )
    val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id val id: Long,
    val number: String,
    val phoneNumber: String,
)
```

::right::

```sql
```

<!--
Table de jointure user_phone pour la relation One-to-One
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## One-to-Many (User -> Phones)

::left::

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
    @Id val id: Long,
    val name: String,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val phones: List<PhoneEntity>
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id val id: Long,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val number: String,
    val phoneNumber: String,
)
```

::right::

```sql
```

<!--
Un utilisateur peut avoir plusieurs téléphones, chaque téléphone appartient à un utilisateur.
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Many-to-One (Phones -> User)

::left::

```kotlin
@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id val id: Long,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val number: String,
    val phoneNumber: String,
)

@Entity
@Table(name = "users")
class UserEntity(
    @Id val id: Long,
    val name: String
)
```

::right::

```sql
```

<!--
Plusieurs téléphones peuvent référencer le même utilisateur.
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Many-to-Many (User <-> Groups)

::left::

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
    @Id val id: Long,
    val name: String,
    @ManyToMany
    @JoinTable(
        name = "user_group",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "group_id")]
    )
    val groups: List<GroupEntity>
)

@Entity
@Table(name = "groups")
class GroupEntity(
    @Id val id: Long,
    @ManyToMany(mappedBy = "groups")
    val users: List<UserEntity>
)
```

::right::

```sql
```

<!--
Un utilisateur peut appartenir à plusieurs groupes, un groupe peut contenir plusieurs utilisateurs.
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## One-to-Many explicite avec @JoinColumn

::left::

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
    @Id val id: Long,
    val name: String,
    @OneToMany
    @JoinColumn(name = "user_id") // la FK est sur phone
    val phones: List<PhoneEntity>
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id val id: Long,
    val number: String,
    val phoneNumber: String,
)
```

::right::

```sql
```

<!--
@OneToMany avec @JoinColumn : la FK est sur la table cible, pas besoin de mappedBy.
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## One-to-Many bidirectionnel avec mappedBy

::left::

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
    @Id val id: Long,
    val name: String,
    @OneToMany(mappedBy = "user")
    val phones: List<PhoneEntity>
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id val id: Long,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val number: String,
    val phoneNumber: String,
)
```

::right::

```sql
```

<!--
@OneToMany(mappedBy) : la FK est sur la table phone, navigation bidirectionnelle.
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Many-to-Many avec JoinTable et cascade

::left::

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
    @Id val id: Long,
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_group",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "group_id")]
    )
    val groups: List<GroupEntity>
)

@Entity
@Table(name = "groups")
class GroupEntity(
    @Id val id: Long,
    @ManyToMany(mappedBy = "groups")
    val users: List<UserEntity>
)
```

::right::

```sql
```

<!--
@ManyToMany avec JoinTable et cascade : suppression automatique des liens.
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## One-to-Many avec orphanRemoval et fetch

::left::

```kotlin
@Entity
@Table(name = "users")
class UserEntity(
    @Id val id: Long,
    val name: String,
    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    val phones: List<PhoneEntity>
)

@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id val id: Long,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val number: String,
)
```

::right::

```sql
```

<!--
@OneToMany avec orphanRemoval : suppression automatique des enfants orphelins.
-->

---
layout: TwoColumnsTitle
class: text-left
---

::title::

## Many-to-One explicite avec cascade

::left::

```kotlin
@Entity
@Table(name = "phone")
class PhoneEntity(
    @Id val id: Long,
    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val number: String,
    val phoneNumber: String,
)

@Entity
@Table(name = "users")
class UserEntity(
    @Id val id: Long,
    val name: String
)
```

::right::

```sql
```

<!--
@ManyToOne avec cascade : propagation de la persistance.
-->
