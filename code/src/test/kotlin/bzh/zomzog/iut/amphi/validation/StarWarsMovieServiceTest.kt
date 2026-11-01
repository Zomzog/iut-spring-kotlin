package bzh.zomzog.iut.amphi.validation

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import jakarta.validation.ConstraintViolationException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.validation.autoconfigure.ValidationAutoConfiguration

@SpringBootTest(classes = [StarWarsMovieService::class, UnvalidatedStarWarsMovieService::class])
@ImportAutoConfiguration(ValidationAutoConfiguration::class)
class StarWarsMovieServiceTest {

    @Autowired
    lateinit var validated: StarWarsMovieService

    @Autowired
    lateinit var unvalidated: UnvalidatedStarWarsMovieService

    @Test
    fun `validated service rejects episode 11`() {
        assertFailure { validated.find(11) }
            .isInstanceOf(ConstraintViolationException::class)
    }

    @Test
    fun `validated service accepts episode 5`() {
        assertThat(validated.find(5)).isEqualTo("Episode 5")
    }

    @Test
    fun `service without validated silently ignores the constraints`() {
        assertThat(unvalidated.find(11)).isEqualTo("Episode 11")
    }
}
