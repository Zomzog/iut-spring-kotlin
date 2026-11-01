package bzh.zomzog.iut.amphi.error

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@WebMvcTest(DemoController::class)
@ActiveProfiles("error")
class DemoControllerTest {

    @MockkBean
    private lateinit var demoService: DemoService
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `invalid body gives 400`() {
        mockMvc.post("/api/v1/demo") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"name":""}"""
        }.andExpect {
            status { isBadRequest() }
            content { string("Your arg is invalid") }
        }
    }

    @Test
    fun `unknown id gives 404`() {
        every { demoService.findById(42) } throws DemoNotFoundException()

        mockMvc.get("/api/v1/demo/42")
            .andExpect {
                status { isNotFound() }
                content { string("Demo not found") }
            }
    }
}
