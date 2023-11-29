package bzh.zomzog.prez.springkotlin.repository

import assertk.assertThat
import assertk.assertions.*
import bzh.zomzog.prez.springkotlin.domain.User
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

abstract class UserDatabaseTest {

    lateinit var repository: UserRepository

    @Nested
    inner class CreationTest {
        @Test
        fun `create once is ok`() {
            // GIVEN
            val user = defaultUser()
            // WHEN
            val result = repository.create(user)
            // THEN
            assertThat(result).isSuccess()
                .isEqualTo(user)
        }

        @Test
        fun `create twice with same email is an error`() {
            // GIVEN
            val user = defaultUser()
            val user2 = defaultUser(firstName = "another")
            repository.create(user)
            // WHEN
            val result = repository.create(user2)
            // THEN
            assertThat(result).isFailure()
        }

        @Test
        fun `create twice with different email is ok`() {
            // GIVEN
            val user = defaultUser()
            val user2 = defaultUser(email = "another@email.pony")
            repository.create(user)
            // WHEN
            val result = repository.create(user2)
            // THEN
            assertThat(result).isSuccess()
                .isEqualTo(user2)
        }
    }

    @Nested
    inner class ListTests {
        @Test
        fun `list all users`() {
            // GIVEN
            val user = defaultUser()
            val user2 = defaultUser(email = "another@email.pony")
            val user3 = defaultUser(email = "anOld@email.pony", age = 1337)
            repository.create(user)
            repository.create(user2)
            repository.create(user3)
            // WHEN
            val result = repository.list()
            // THEN
            assertThat(result).containsExactlyInAnyOrder(user, user2, user3)
        }

        @Test
        fun `list users filtered by age`() {
            // GIVEN
            val user = defaultUser()
            val user2 = defaultUser(email = "another@email.pony")
            val user3 = defaultUser(email = "anOld@email.pony", age = 1337)
            repository.create(user)
            repository.create(user2)
            repository.create(user3)
            // WHEN
            val result = repository.list(1337)
            // THEN
            assertThat(result).containsExactly(user3)
        }
    }

    @Nested
    inner class GetTest {
        @Test
        fun `find existing one`() {
            // GIVEN
            val user = defaultUser()
            repository.create(user)
            // WHEN
            val result = repository.get(user.email)
            // THEN
            assertThat(result).isEqualTo(user)
        }

        @Test
        fun `find one without users`() {
            // GIVEN
            // WHEN
            val result = repository.get("anEmail")
            // THEN
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class UpdatedTest {
        @Test
        fun `update an existing user`() {
            // GIVEN
            val user = defaultUser()
            repository.create(user)
            val update = defaultUser(firstName = "newFirst", lastName = "newLast", age = 99)
            // WHEN
            val result = repository.update(update)
            // THEN
            assertThat(result).isSuccess().isEqualTo(update)
        }

        @Test
        fun `update non-existing user`() {
            // GIVEN
            val update = defaultUser(firstName = "newFirst", lastName = "newLast", age = 99)
            // WHEN
            val result = repository.update(update)
            // THEN
            assertThat(result).isFailure()

        }
    }

    @Nested
    inner class DeleteTests {
        @Test
        fun `delete an existing user`() {
            // GIVEN
            val user = defaultUser()
            repository.create(user)
            // WHEN
            val result = repository.delete(user.email)
            // THEN
            assertThat(result).isEqualTo(user)
        }

        @Test
        fun `update non-existing user`() {
            // GIVEN
            // WHEN
            val result = repository.delete("email")
            // THEN
            assertThat(result).isNull()

        }
    }

    private fun defaultUser(
        email: String = "j@d.com",
        firstName: String = "first",
        lastName: String = "last",
        age: Int = 42
    ) = User(email, firstName, lastName, age)
}