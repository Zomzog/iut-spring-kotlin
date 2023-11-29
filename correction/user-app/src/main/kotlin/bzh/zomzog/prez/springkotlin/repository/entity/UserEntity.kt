package bzh.zomzog.prez.springkotlin.repository.entity

import bzh.zomzog.prez.springkotlin.domain.User
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class UserEntity(
    @Id val email: String,
    val firstName: String,
    val lastName: String,
    val age: Int,
) {
    fun asUser() = User(this.email, this.firstName, this.lastName, this.age)
}
fun User.asEntity() = UserEntity(this.email, this.firstName, this.lastName, this.age)
