package bzh.zomzog.prez.springkotlin.repository

import bzh.zomzog.prez.springkotlin.domain.User
import bzh.zomzog.prez.springkotlin.repository.entity.MovieEntity
import bzh.zomzog.prez.springkotlin.repository.entity.UserEntity
import bzh.zomzog.prez.springkotlin.repository.entity.asEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import kotlin.jvm.optionals.getOrNull

@Repository
class UserDatabaseRepository(private val users: UserJpaRepository,
                             private val movies: MovieJpaRepository) {
    fun create(user: User): Result<User> = if (users.findById(user.email).isPresent) {
        Result.failure(Exception("User already in DB"))
    } else {
        val saved = users.save(user.asEntity())
        Result.success(saved.asUser())
    }

    fun list(age: Int?): List<User> {
        return if (age == null) {
            users.findAll()
        } else {
            users.findAllByAge(age)
        }.map { it.asUser() }
    }

    fun get(email: String): User? {
        return users.findById(email)
                .map { it.asUser() }
                .getOrNull()
    }

    fun update(user: User): Result<User> = if (users.findById(user.email).isPresent) {
        val saved = users.save(user.asEntity())
        Result.success(saved.asUser())
    } else {
        Result.failure(Exception("User not in DB"))
    }

    fun delete(email: String): User? {
        return users.findById(email)
                .also { users.deleteById(email) }
                .map { it.asUser() }
                .getOrNull()
    }

    fun addMovie(email: String, movieId: Int): User {
        return users.findById(email)
                .map { users.save(it.copy(favoriteMovies = it.favoriteMovies + MovieEntity(movieId, listOf(it)))) }
                .map { it.asUser() }
                .orElseThrow()
    }

    fun removeMovie(email: String, movieId: Int): User {
        return users.findById(email)
                .map { users.save(it.copy(favoriteMovies = it.favoriteMovies.filterNot { m -> m.movieId == movieId })) }
                .map { it.asUser() }
                .orElseThrow()
    }

    fun deleteMovie(movieId: Int): Unit =
            users.findAll()
                    .filter { it.favoriteMovies.map { it.movieId }.contains(movieId) }
                    .forEach {
                        users.save(it.copy(favoriteMovies = it.favoriteMovies.filterNot { m -> m.movieId == movieId }))
                    }
}

interface UserJpaRepository : JpaRepository<UserEntity, String> {
    fun findAllByAge(age: Int): List<UserEntity>
}

interface MovieJpaRepository : JpaRepository<MovieEntity, Int>


