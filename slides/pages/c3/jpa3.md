---
layout: TwoColumns
class: text-left
---

::left::

### One-to-One Uni-directionnel

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
		@Id val email: String,
		val number: String,
)
```

::right::

### Structure de la base

```sql
CREATE TABLE users (
	email VARCHAR PRIMARY KEY,
	phone_email VARCHAR,
	FOREIGN KEY (phone_email) REFERENCES phone(email)
);

CREATE TABLE phone (
	email VARCHAR PRIMARY KEY,
	number VARCHAR
);
```

<!--
La clé étrangère est sur la table users, pointant vers phone
Relation uni-directionnelle: User -> Phone
-->
