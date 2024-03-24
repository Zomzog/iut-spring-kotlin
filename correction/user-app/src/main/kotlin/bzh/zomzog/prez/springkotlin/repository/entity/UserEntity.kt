package bzh.zomzog.prez.springkotlin.repository.entity

import bzh.zomzog.prez.springkotlin.domain.Movie
import bzh.zomzog.prez.springkotlin.domain.User
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(
        @Id val email: String,
        val firstName: String,
        val lastName: String,
        val age: Int,
        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "user_movies",
                joinColumns = [JoinColumn(name = "email")],
                inverseJoinColumns = [JoinColumn(name = "movieId")])
        var favoriteMovies: List<MovieEntity> = emptyList(),
) {













    fun asUser() = User(this.email, this.firstName, this.lastName, this.age, this.favoriteMovies.map { Movie(it.movieId!!, it.movieId.toString(), it.movieId.toString()) })
}

@Entity
@Table(name = "movies")
data class MovieEntity(
        @Id val movieId: Int?,
        @ManyToMany
        val user: List<UserEntity>,
) {
}

fun User.asEntity(): UserEntity {
    val movies: MutableList<MovieEntity> = mutableListOf()
    val user = UserEntity(this.email, this.firstName, this.lastName, this.age, movies)
    favoriteMovies.forEach { movies.add(MovieEntity(it.id, listOf(user))) }
    return user
}

