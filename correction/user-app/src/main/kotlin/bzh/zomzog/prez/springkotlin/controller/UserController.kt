package bzh.zomzog.prez.springkotlin.controller

import bzh.zomzog.prez.springkotlin.errors.UserNotFoundError
import bzh.zomzog.prez.springkotlin.controller.dto.UserDTO
import bzh.zomzog.prez.springkotlin.controller.dto.asUserDTO
import bzh.zomzog.prez.springkotlin.repository.UserRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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

    @Operation(summary = "Create user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "User created",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = UserDTO::class)
            )]),
        ApiResponse(responseCode = "409", description = "User already exist",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
    @PostMapping("/api/users")
    fun create(@RequestBody @Valid user: UserDTO): ResponseEntity<UserDTO> =
        userRepository.create(user.asUser()).fold(
            { success -> ResponseEntity.status(HttpStatus.CREATED).body(success.asUserDTO()) },
            { failure -> ResponseEntity.status(HttpStatus.CONFLICT).build() })

    @Operation(summary = "List users")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "List users",
            content = [Content(mediaType = "application/json",
                array = ArraySchema(
                    schema = Schema(implementation = UserDTO::class))
            )])])
    @GetMapping("/api/users")
    fun list(@RequestParam(required = false) @Min(15) age: Int?) =
        userRepository.list(age)
            .map { it.asUserDTO() }
            .let {
                ResponseEntity.ok(it)
            }

    @Operation(summary = "Get user by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "The user",
            content = [
                Content(mediaType = "application/json",
                    schema = Schema(implementation = UserDTO::class))]),
        ApiResponse(responseCode = "404", description = "User not found")
    ])
    @GetMapping("/api/users/{email}")
    fun findOne(@PathVariable @Email email: String): ResponseEntity<UserDTO> {
        val user = userRepository.get(email)
        return if (user != null) {
            ResponseEntity.ok(user.asUserDTO())
        } else {
            throw UserNotFoundError(email)
        }
    }

    @Operation(summary = "Update a user by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "User updated",
            content = [Content(mediaType = "application/json",
                schema = Schema(implementation = UserDTO::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])])
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

    @Operation(summary = "Delete user by email")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "User deleted"),
        ApiResponse(responseCode = "400", description = "User not found",
            content = [Content(mediaType = "application/json", schema = Schema(implementation = String::class))])
    ])
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
