package bzh.zomzog.iut.amphi

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@ActiveProfiles("tc-service")
@DataJpaTest
@Testcontainers
class UserRepositoryServiceConnectionTes {

    companion object {
        @Container
        @ServiceConnection
        val postgres = PostgreSQLContainer("postgres:18-alpine").apply {
            withDatabaseName("testdb")
            withUsername("test")
            withPassword("test")
        }
    }

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `service connection should provide database and repository works`() {
        val user = UserEntity(id = 2L, name = "Bob")
        userRepository.save(user)

        val found = userRepository.findById(user.id.toString())
        assert(found.isPresent)
    }
}
