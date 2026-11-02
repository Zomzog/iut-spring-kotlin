package bzh.zomzog.iut.amphi.config

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.core.env.Environment
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

/**
 * src/test/resources/application.yml remplace celui de src/main/resources : le fichier entier, sans fusion.
 */
@SpringJUnitConfig(initializers = [ConfigDataApplicationContextInitializer::class])
class TestResourcesReplaceMainTest {

    @Autowired
    private lateinit var environment: Environment

    @Test
    fun `property in both files comes from the test file`() {
        assertThat(environment.getProperty("app.name")).isEqualTo("test-app")
    }

    @Test
    fun `property only in the main file is not defined`() {
        assertThat(environment.getProperty("spring.application.name")).isNull()
    }
}
