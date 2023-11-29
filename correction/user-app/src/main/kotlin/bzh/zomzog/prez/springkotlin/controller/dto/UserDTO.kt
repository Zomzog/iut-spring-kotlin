package bzh.zomzog.prez.springkotlin.controller.dto

import bzh.zomzog.prez.springkotlin.domain.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class UserDTO(
    @field:Email val email: String,
    @field:Size(min = 1, max = 30) val firstName: String,
    @field:Size(min = 1, max = 30) val lastName: String,
    @field:Min(15) @field:Max(120) val age: Int
) {

    fun asUser() = User(email, firstName, lastName, age)
}

fun User.asUserDTO() = UserDTO(this.email, this.firstName, this.lastName, this.age)
