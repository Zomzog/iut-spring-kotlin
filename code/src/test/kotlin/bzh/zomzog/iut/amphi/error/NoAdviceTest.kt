package bzh.zomzog.iut.amphi.error

import assertk.assertFailure
import assertk.assertions.isInstanceOf
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import jakarta.servlet.ServletException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

/**
 * Aucun profil actif, donc aucun advice : rien ne traduit l'exception.
 */
@WebMvcTest(DemoController::class)
class NoAdviceTest {

    @MockkBean
    private lateinit var demoService: DemoService
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `without advice the exception escapes from MockMvc`() {
        every { demoService.findById(42) } throws DemoNotFoundException()

        assertFailure { mockMvc.get("/api/v1/demo/42") }
            .isInstanceOf<ServletException>()
    }
}
