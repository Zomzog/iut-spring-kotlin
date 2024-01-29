package bzh.zomzog.prez.springkotlin.controller

import bzh.zomzog.prez.springkotlin.errors.UserNotFoundError
import bzh.zomzog.prez.springkotlin.controller.dto.UserDTO
import bzh.zomzog.prez.springkotlin.controller.dto.asUserDTO
import bzh.zomzog.prez.springkotlin.repository.UserRepository
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

@RestController
@Validated
class UserController(val userRepository: UserRepository) {

    @PostMapping("/api/users")
    fun create(@RequestBody @Valid user: UserDTO): ResponseEntity<UserDTO> =
        userRepository.create(user.asUser()).fold(
            { success -> ResponseEntity.status(HttpStatus.CREATED).body(success.asUserDTO()) },
            { failure -> ResponseEntity.status(HttpStatus.CONFLICT).build() })


    @GetMapping("/api/users")
    fun list(@RequestParam(required = false) @Min(15) age: Int?) =
        userRepository.list(age)
            .map { it.asUserDTO() }
            .let {
                ResponseEntity.ok(it)
            }

    @GetMapping("/api/users/{email}")
    fun findOne(@PathVariable @Email email: String): ResponseEntity<UserDTO> {
        val user = userRepository.get(email)
        return if (user != null) {
            ResponseEntity.ok(user.asUserDTO())
        } else {
            throw UserNotFoundError(email)
        }
    }

    @PutMapping("/api/users/{email}")
    fun update(@PathVariable @Email email: String, @RequestBody @Valid user: UserDTO): ResponseEntity<Any> =
        if (email != user.email) {
            ResponseEntity.badRequest().body("Invalid email")
        } else {
            userRepository.update(user.asUser()).fold(
                { success -> ResponseEntity.ok(success.asUserDTO()) },
                { failure -> ResponseEntity.badRequest().body(failure.message) }
            )
        }

    @DeleteMapping("/api/users/{email}")
    fun delete(@PathVariable @Email email: String): ResponseEntity<Any> {
        val deleted = userRepository.delete(email)
        return if (deleted == null) {
            ResponseEntity.badRequest().body("User not found")
        } else {
            ResponseEntity.noContent().build()
        }
    }
}
