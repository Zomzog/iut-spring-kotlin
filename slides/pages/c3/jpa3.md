---
layout: cover
hideInToc: false
---

# JPA

---
layout: cover
hideInToc: false
---

## One-to-One

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
data class UserEntity(
    @Id val email: String,
    val name: String,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(referencedColumnName = "email")
    val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
data class PhoneEntity(
    @Id val email: String,
    val phoneNumber: String,
)
```

::right::

```sql
CREATE TABLE users (
 email varchar(255) NOT NULL,
 "name" varchar(255) NULL,
 phone_email varchar(255) NULL,
 CONSTRAINT users_phone_email_key UNIQUE (phone_email),
 CONSTRAINT users_pkey PRIMARY KEY (email),
 CONSTRAINT fk1qj2mfat2o9nn5mp097e50otl 
    FOREIGN KEY (phone_email) REFERENCES phone(email)
);

CREATE TABLE phone (
 email varchar(255) NOT NULL,
 phone_number varchar(255) NULL,
 CONSTRAINT phone_pkey PRIMARY KEY (email)
);
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
@Table(name = "users")
data class UserEntity(
    @Id val id: Long,
    val name: String,
)

@Entity
@Table(name = "phone")
data class PhoneEntity(
    @Id val id: Long,
    @OneToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)
```

::right::

```sql
CREATE TABLE users (
 id int8 NOT NULL,
 "name" varchar(255) NULL,
 CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE phone (
 id int8 NOT NULL,
 user_id int8 NULL,
 phone_number varchar(255) NULL,
 CONSTRAINT phone_pkey PRIMARY KEY (id),
 CONSTRAINT phone_user_id_key UNIQUE (user_id),
 CONSTRAINT fkik7a2etdorybvoolvchfcvgkx 
        FOREIGN KEY (user_id) REFERENCES users(id)
);
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
@Table(name = "users")
data class UserEntity(
    @Id val id: Long,
    val name: String,
    @JoinColumn(name = "phone_id")
    val phone: PhoneEntity,
)
@Entity
@Table(name = "phone")
data class PhoneEntity(
    @Id val id: Long,
    @OneToOne(mappedBy = "phone")
    val user: UserEntity,
    val phoneNumber: String,
)
```

::right::

```sql
CREATE TABLE users (
 id int8 NOT NULL,
 phone_id int8 NULL,
 "name" varchar(255) NULL,
 CONSTRAINT users_phone_id_key UNIQUE (phone_id),
 CONSTRAINT users_pkey PRIMARY KEY (id),
 CONSTRAINT fklq0cckks2hmmyc1mk9g30h44 
  FOREIGN KEY (phone_id) REFERENCES phone(id)
);

CREATE TABLE phone (
 id int8 NOT NULL,
 phone_number varchar(255) NULL,
 CONSTRAINT phone_pkey PRIMARY KEY (id)
);
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
data class PhoneEntity(
    @Id val id: Long,
    @OneToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)

@Entity
@Table(name = "users")
data class UserEntity(
    @Id val id: Long,
    val name: String,
    @OneToOne(mappedBy = "user")
    val phone: PhoneEntity,
)
```

::right::

```sql
CREATE TABLE users (
 id int8 NOT NULL,
 "name" varchar(255) NULL,
 CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE phone (
 id int8 NOT NULL,
 user_id int8 NULL,
 phone_number varchar(255) NULL,
 CONSTRAINT phone_pkey PRIMARY KEY (id),
 CONSTRAINT phone_user_id_key UNIQUE (user_id),
 CONSTRAINT fkik7a2etdorybvoolvchfcvgkx 
   FOREIGN KEY (user_id) REFERENCES users(id)
);
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
data class UserEntity(
    @Id val id: Long,
    val name: String,
    @OneToOne
    @JoinTable(
        name = "user_phone",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "phone_id")],
    )
    val phone: PhoneEntity,
)

@Entity
@Table(name = "phone")
data class PhoneEntity(
    @Id val id: Long,
    val phoneNumber: String,
)
```

::right::

```sql
CREATE TABLE users (
 id int8 NOT NULL,
 "name" varchar(255) NULL,
 CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE phone (
 id int8 NOT NULL,
 phone_number varchar(255) NULL,
 CONSTRAINT phone_pkey PRIMARY KEY (id)
);

CREATE TABLE user_phone (
 phone_id int8 NULL,
 user_id int8 NOT NULL,
 CONSTRAINT user_phone_phone_id_key UNIQUE (phone_id),
 CONSTRAINT user_phone_pkey PRIMARY KEY (user_id),
 CONSTRAINT fk85cnan0dinwj6imy3gkhgikq 
  FOREIGN KEY (phone_id) REFERENCES phone(id),
 CONSTRAINT fklxcdtyyvlfok8uka5tax0u1sm 
  FOREIGN KEY (user_id) REFERENCES users(id)
);
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
data class UserEntity(
    @Id val id: Long,
    val name: String,
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val phones: List<PhoneEntity>,
)

@Entity
@Table(name = "phone")
data class PhoneEntity(
    @Id val id: Long,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)
```

::right::

```sql
CREATE TABLE users (
 id int8 NOT NULL,
 "name" varchar(255) NULL,
 CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE phone (
 id int8 NOT NULL,
 user_id int8 NULL,
 phone_number varchar(255) NULL,
 CONSTRAINT phone_pkey PRIMARY KEY (id),
 CONSTRAINT fkik7a2etdorybvoolvchfcvgkx 
   FOREIGN KEY (user_id) REFERENCES users(id)
);
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
data class PhoneEntity(
    @Id val id: Long,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)

@Entity
@Table(name = "users")
data class UserEntity(
    @Id val id: Long,
    val name: String,
)
```

::right::

```sql
CREATE TABLE users (
 id int8 NOT NULL,
 "name" varchar(255) NULL,
 CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE phone (
 id int8 NOT NULL,
 user_id int8 NULL,
 phone_number varchar(255) NULL,
 CONSTRAINT phone_pkey PRIMARY KEY (id),
 CONSTRAINT fkik7a2etdorybvoolvchfcvgkx 
  FOREIGN KEY (user_id) REFERENCES users(id)
);
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
data class UserEntity(
    @Id val id: Long,
    val name: String,
    @ManyToMany
    @JoinTable(
        name = "user_group",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "group_id")],
    )
    val groups: List<GroupEntity>,
)

@Entity
@Table(name = "groups")
data class GroupEntity(
    @Id val id: Long,
    @ManyToMany(mappedBy = "groups")
    val users: List<UserEntity>,
)
```

::right::

```sql
CREATE TABLE "groups" (
 id int8 NOT NULL,
 CONSTRAINT groups_pkey PRIMARY KEY (id)
);

CREATE TABLE users (
 id int8 NOT NULL,
 "name" varchar(255) NULL,
 CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE user_group (
 group_id int8 NOT NULL,
 user_id int8 NOT NULL,
 CONSTRAINT fk7k9ade3lqbo483u9vuryxmm34 
  FOREIGN KEY (user_id) REFERENCES users(id),
 CONSTRAINT fkbegtgnl3oq004958pisko4fu4 
  FOREIGN KEY (group_id) REFERENCES "groups"(id)
);
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
data class UserEntity(
    @Id val id: Long,
    val name: String,
    @OneToMany
    @JoinColumn(name = "user_id") // la FK est sur phone
    val phones: List<PhoneEntity>,
)

@Entity
@Table(name = "phone")
data class PhoneEntity(
    @Id val id: Long,
    val phoneNumber: String,
)
```

::right::

```sql
CREATE TABLE users (
 id int8 NOT NULL,
 "name" varchar(255) NULL,
 CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE phone (
 id int8 NOT NULL,
 user_id int8 NULL,
 phone_number varchar(255) NULL,
 CONSTRAINT phone_pkey PRIMARY KEY (id),
 CONSTRAINT fkik7a2etdorybvoolvchfcvgkx
  FOREIGN KEY (user_id) REFERENCES users(id)
);
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
data class UserEntity(
    @Id val id: Long,
    val name: String,
    @OneToMany(mappedBy = "user")
    val phones: List<PhoneEntity>,
)

@Entity
@Table(name = "phone")
data class PhoneEntity(
    @Id val id: Long,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)
```

::right::

```sql
CREATE TABLE users (
 id int8 NOT NULL,
 "name" varchar(255) NULL,
 CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE phone (
 id int8 NOT NULL,
 user_id int8 NULL,
 phone_number varchar(255) NULL,
 CONSTRAINT phone_pkey PRIMARY KEY (id),
 CONSTRAINT fkik7a2etdorybvoolvchfcvgkx 
  FOREIGN KEY (user_id) REFERENCES users(id)
);
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
data class UserEntity(
    @Id val id: Long,
    @ManyToMany(cascade = [CascadeType.ALL])
    @JoinTable(
        name = "user_group",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "group_id")],
    )
    val groups: List<GroupEntity>,
)

@Entity
@Table(name = "groups")
data class GroupEntity(
    @Id val id: Long,
    @ManyToMany(mappedBy = "groups")
    val users: List<UserEntity>,
)
```

::right::

```sql
CREATE TABLE "groups" (
 id int8 NOT NULL,
 CONSTRAINT groups_pkey PRIMARY KEY (id)
);

CREATE TABLE users (
 id int8 NOT NULL,
 CONSTRAINT users_pkey PRIMARY KEY (id)
);


CREATE TABLE user_group (
 group_id int8 NOT NULL,
 user_id int8 NOT NULL,
 CONSTRAINT fk7k9ade3lqbo483u9vuryxmm34 
  FOREIGN KEY (user_id) REFERENCES users(id),
 CONSTRAINT fkbegtgnl3oq004958pisko4fu4 
  FOREIGN KEY (group_id) REFERENCES "groups"(id)
);
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
data class UserEntity(
    @Id val id: Long,
    val name: String,
    @OneToMany(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    val phones: List<PhoneEntity>,
)

@Entity
@Table(name = "phone")
data class PhoneEntity(
    @Id val id: Long,
    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)
```

::right::

```sql
CREATE TABLE users (
 id int8 NOT NULL,
 "name" varchar(255) NULL,
 CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE phone (
 id int8 NOT NULL,
 user_id int8 NULL,
 phone_number varchar(255) NULL,
 CONSTRAINT phone_pkey PRIMARY KEY (id),
 CONSTRAINT fkik7a2etdorybvoolvchfcvgkx 
  FOREIGN KEY (user_id) REFERENCES users(id)
);
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
data class PhoneEntity(
    @Id val id: Long,
    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_id")
    val user: UserEntity,
    val phoneNumber: String,
)

@Entity
@Table(name = "users")
data class UserEntity(
    @Id val id: Long,
    val name: String,
)
```

::right::

```sql
CREATE TABLE users (
 id int8 NOT NULL,
 "name" varchar(255) NULL,
 CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE phone (
 id int8 NOT NULL,
 user_id int8 NULL,
 phone_number varchar(255) NULL,
 CONSTRAINT phone_pkey PRIMARY KEY (id),
 CONSTRAINT fkik7a2etdorybvoolvchfcvgkx 
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

<!--
@ManyToOne avec cascade : propagation de la persistance.
-->
