package bzh.zomzog.prez.springkotlin.controller

import assertk.assertThat
import assertk.assertions.isEqualTo
import bzh.zomzog.prez.springkotlin.controller.dto.UserDTO
import bzh.zomzog.prez.springkotlin.domain.User
import bzh.zomzog.prez.springkotlin.repository.UserRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

class UserControllerTest {

    @Nested
    @WebMvcTest(controllers = [UserController::class])
    inner class ListTests {

        @Autowired
        lateinit var mockMvc: MockMvc

        @MockkBean
        lateinit var userRepository: UserRepository

        @Test
        fun `list empty`() {
            // GIVEN
            every { userRepository.list() } returns listOf()

            // WHEN
            mockMvc.get("/api/users")
                    // THEN
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                        content { json("[]") }
                    }
        }

        @Test
        fun `list filtered`() {
            // GIVEN
            every { userRepository.list(17) } returns listOf(user("a@a.a"), user("b@b.b"))

            // WHEN
            mockMvc.get("/api/users?age=17")
                    // THEN
                    .andExpect {
                        status { isOk() }
                        content { contentType(MediaType.APPLICATION_JSON) }
                        content {
                            jsonPath("$.[0].email") { value("a@a.a") }
                            jsonPath("$.[1].email") { value("b@b.b") }
                        }
                    }
        }
    }

    @Nested
    @SpringBootTest
    inner class UpdateTests {

        @Autowired
        lateinit var userController: UserController

        @MockkBean
        lateinit var userRepository: UserRepository

        @Test
        fun `update valid`() {
            // GIVEN
            every { userRepository.update(any()) } returns Result.success(user())
            val update = UserDTO("email@email.com", "first", "last", 42)
            // WHEN
            val result = userController.update("email@email.com", update)
            // THEN
            assertThat(result).isEqualTo(ResponseEntity.ok(update))
        }

        @Test
        fun `update a non-existing user`() {
            // GIVEN
            every { userRepository.update(any()) } returns Result.failure(Exception("Nope"))
            val update = UserDTO("email@email.com", "first", "last", 42)
            // WHEN
            val result = userController.update("email@email.com", update)
            // THEN
            assertThat(result).isEqualTo(ResponseEntity.badRequest().body("Nope"))
        }

        @Test
        fun `update with two emails`() {
            // GIVEN
            val update = UserDTO("email@email.com", "first", "last", 42)
            // WHEN
            val result = userController.update("another@email.com", update)
            // THEN
            assertThat(result).isEqualTo(ResponseEntity.badRequest().body("Invalid email"))
        }
    }

    private fun user(email: String = "email@email.com") = User(email, "first", "last", 42)

}