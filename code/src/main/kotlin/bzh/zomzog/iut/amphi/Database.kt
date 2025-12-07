package bzh.zomzog.iut.amphi

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

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




interface UserRepository : JpaRepository<UserEntity, String>