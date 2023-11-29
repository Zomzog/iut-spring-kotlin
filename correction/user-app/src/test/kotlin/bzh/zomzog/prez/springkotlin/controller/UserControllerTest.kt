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
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.ResponseEntity

@SpringBootTest
class UserControllerTest {
    @MockkBean
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userController: UserController

    @Nested
    inner class UpdateTests {
        @Test
        fun `update valid`() {
            // GIVEN
            every { userRepository.update(any()) } returns Result.success(User("email@email.com", "first", "last", 42))
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

}