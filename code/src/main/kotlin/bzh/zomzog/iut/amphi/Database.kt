package bzh.zomzog.iut.amphi

import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

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

interface UserRepository : JpaRepository<UserEntity, String>