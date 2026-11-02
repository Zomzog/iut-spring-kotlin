package bzh.zomzog.iut.relations

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

/*
 * Le code des slides de relations, presque mot pour mot : un object par slide de c2/jpa3.md.
 * Les fins de magic-move de c2/jpa2.md sont les mêmes que ces slides.
 *
 * Hors de bzh.zomzog.iut.amphi : ce package est scanné par les @DataJpaTest, et ces @Entity portent
 * les mêmes noms que celles de Database.kt.
 */

/** One-to-One Uni-directionnel (clé étrangère sur User). */
object OneToOneUniFkOnUser {
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
        // Doit être unique : @Id ou @Column(unique = true)
        @Id val email: String,
        val phoneNumber: String,
    )

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}

/** La même slide avec le commentaire appliqué : la jointure vise une colonne unique, pas la clé primaire. */
object OneToOneUniFkOnUserUniqueColumn {
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
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        @Column(unique = true)
        val email: String,
        val phoneNumber: String,
    )

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}

/** One-to-One Uni-directionnel (clé étrangère sur Phone), fin du pas à pas One-To-One unidirectionnel. */
object OneToOneUniFkOnPhone {
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

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}

/** Étape du pas à pas One-To-One bi-directionnel : une clé étrangère de chaque côté, sans mappedBy. */
object OneToOneBiTwoForeignKeys {
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

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}

/** One-to-One bidirectionnel (clé étrangère sur User). */
object OneToOneBiFkOnUser {
    @Entity
    @Table(name = "users")
    class UserEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        val name: String,
        @OneToOne
        @JoinColumn(name = "phone_id")
        val phone: PhoneEntity,
    )

    @Entity
    @Table(name = "phone")
    class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        val phoneNumber: String,
        @OneToOne(mappedBy = "phone")
        var user: UserEntity? = null,
    )

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}

/** One-to-One bidirectionnel (clé étrangère sur Phone), fin du pas à pas One-To-One bi-directionnel. */
object OneToOneBiFkOnPhone {
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

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}

/** One-to-One avec table de jointure (IDs séparés). */
object OneToOneJoinTable {
    @Entity
    @Table(name = "users")
    class UserEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
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
    class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        val phoneNumber: String,
    )

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}

/** One-to-Many (User -> Phones), fin du pas à pas Many-To-One. */
object OneToManyCascade {
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

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}

/** Many-to-One (Phones -> User). */
object ManyToOneUni {
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

    @Entity
    @Table(name = "users")
    class UserEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        val name: String,
    )

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}

/** Many-to-Many (User <-> Groups), fin du pas à pas Many-To-Many. */
object ManyToManyBi {
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

    val entities = arrayOf(UserEntity::class, GroupEntity::class)
}

/** Le Many-to-Many sans mappedBy, tel qu'il était sur les slides. */
object ManyToManyWithoutMappedBy {
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
        @ManyToMany
        val users: MutableList<UserEntity> = mutableListOf(),
    )

    val entities = arrayOf(UserEntity::class, GroupEntity::class)
}

/** One-to-Many explicite avec @JoinColumn, fin du pas à pas One-To-Many unidirectionnel. */
object OneToManyJoinColumn {
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

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}

/** One-to-Many bidirectionnel avec mappedBy. */
object OneToManyBi {
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

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}

/** One-to-Many avec orphanRemoval et fetch. */
object OneToManyOrphanRemoval {
    @Entity
    @Table(name = "users")
    class UserEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        val name: String,
        @OneToMany(
            mappedBy = "user",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
        )
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

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}

/** Many-to-One explicite avec cascade. */
object ManyToOneCascade {
    @Entity
    @Table(name = "phone")
    class PhoneEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        @ManyToOne(cascade = [CascadeType.PERSIST])
        @JoinColumn(name = "user_id")
        val user: UserEntity,
        val phoneNumber: String,
    )

    @Entity
    @Table(name = "users")
    class UserEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        val name: String,
    )

    val entities = arrayOf(UserEntity::class, PhoneEntity::class)
}
