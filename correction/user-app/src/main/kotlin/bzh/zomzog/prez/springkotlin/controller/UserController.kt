package bzh.zomzog.prez.springkotlin.controller

import bzh.zomzog.prez.springkotlin.controller.dto.AddMovieDTO
import bzh.zomzog.prez.springkotlin.controller.dto.UserCreationDTO
import bzh.zomzog.prez.springkotlin.controller.dto.UserDTO
import bzh.zomzog.prez.springkotlin.controller.dto.asDTO
import bzh.zomzog.prez.springkotlin.errors.UserNotFoundError
import bzh.zomzog.prez.springkotlin.repository.UserDatabaseRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Validated
class UserController(val userRepository: UserDatabaseRepository) {

    companion object {
        const val ADMIN = """This API-key must be "admin-key" or respond 401"""
    }
    @Operation(summary = "Create a new user")
    @PostMapping("/api/v1/users")
    fun create(@Parameter(description = ADMIN , `in` = HEADER) @RequestHeader(required = true) apiKey: String, @RequestBody @Valid user: UserCreationDTO): ResponseEntity<UserDTO> =

            userRepository.create(user.asUser()).fold(
                    { success -> ResponseEntity.status(HttpStatus.CREATED).body(success.asDTO()) },
                    { failure -> ResponseEntity.status(HttpStatus.CONFLICT).build() })

    @GetMapping("/api/v1/users")
    fun list(@Parameter(description = ADMIN , `in` = HEADER)@RequestHeader(required = true) apiKey: String,  @RequestParam(required = false) @Min(15) age: Int?) =
            userRepository.list(age)
                    .map { it.asDTO() }
                    .let {
                        ResponseEntity.ok(it)
                    }

    @GetMapping("/api/v1/users/{email}")
    fun findOne(@PathVariable @Email email: String): ResponseEntity<UserDTO> {
        val user = userRepository.get(email)
        return if (user != null) {
            ResponseEntity.ok(user.asDTO())
        } else {
            throw UserNotFoundError(email)
        }
    }

    @PutMapping("/api/v1/users/{email}")
    fun update(@Parameter(description = ADMIN , `in` = HEADER)@RequestHeader(required = true) apiKey: String,  @PathVariable @Email email: String, @RequestBody @Valid user: UserDTO): ResponseEntity<Any> =
            if (email != user.email) {
                ResponseEntity.badRequest().body("Invalid email")
            } else {
                userRepository.update(user.asUser()).fold(
                        { success -> ResponseEntity.ok(success.asDTO()) },
                        { failure -> ResponseEntity.badRequest().body(failure.message) }
                )
            }

    @DeleteMapping("/api/v1/users/{email}")
    fun delete(@Parameter(description = ADMIN , `in` = HEADER) @RequestHeader(required = true) apiKey: String, @PathVariable @Email email: String): ResponseEntity<Any> {
        val deleted = userRepository.delete(email)
        return if (deleted == null) {
            ResponseEntity.badRequest().body("User not found")
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/api/v1/users/{email}/favoriteMovies")
    fun addFavoriteMovie(@PathVariable @Email email: String, @RequestBody addMovieDTO: AddMovieDTO) =
        userRepository.addMovie(email, addMovieDTO.id)
                .let { ResponseEntity.ok(it) }

    @DeleteMapping("/api/v1/users/{email}/favoriteMovies/{movieId}")
    fun removeFavoriteMovie(@PathVariable @Email email: String, @PathVariable @Min(0) movieId: Int) =
            userRepository.removeMovie(email, movieId)
                    .let { ResponseEntity.ok(it) }

    @DeleteMapping("/api/v1/movies/{movieId}")
    fun deleteMovie(@Parameter(description = ADMIN , `in` = HEADER) @RequestHeader(required = true) apiKey: String, @PathVariable @Min(0) movieId: Int) = userRepository.deleteMovie(movieId)
            .let {ResponseEntity.ok(it)}

}
