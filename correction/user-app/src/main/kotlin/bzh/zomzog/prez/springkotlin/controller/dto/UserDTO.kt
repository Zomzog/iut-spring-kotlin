package bzh.zomzog.prez.springkotlin.controller.dto

import bzh.zomzog.prez.springkotlin.domain.Movie
import bzh.zomzog.prez.springkotlin.domain.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size


data class UserCreationDTO(
        @field:Email val email: String,
        @field:Size(min = 1, max = 30) val firstName: String,
        @field:Size(min = 1, max = 30) val lastName: String,
        @field:Min(15) @field:Max(120) val age: Int,
) {
    fun asUser() = User(email, firstName, lastName, age)
}

data class UserDTO(
        @field:Email val email: String,
        @field:Size(min = 1, max = 30) val firstName: String,
        @field:Size(min = 1, max = 30) val lastName: String,
        @field:Min(15) @field:Max(120) val age: Int,
        val favoriteMovies: List<MovieDTO> = listOf()
) {
    fun asUser() = User(email, firstName, lastName, age, favoriteMovies.map { it.asMovie() })
}

fun User.asDTO() = UserDTO(email, firstName, lastName, age, favoriteMovies.map { it.asDTO() })

data class MovieDTO(val id: Int, val name: String, val releaseDate: String) {
    fun asMovie() = Movie(id, name, releaseDate)
}

fun Movie.asDTO() = MovieDTO(id, name, releaseDate)

data class AddMovieDTO(val id: Int)