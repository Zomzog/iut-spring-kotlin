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

abstract class ProblemDetailErrorTest {

    @MockkBean
    protected lateinit var demoService: DemoService
    @Autowired
    protected lateinit var mockMvc: MockMvc

    protected fun postInvalidBody() = mockMvc.post("/api/v1/demo") {
        contentType = MediaType.APPLICATION_JSON
        content = """{"name":""}"""
    }
}

@WebMvcTest(DemoController::class)
@ActiveProfiles("problem-error")
class ProblemDetailAdviceTest : ProblemDetailErrorTest() {

    @Test
    fun `unknown id gives a 404 problem detail`() {
        every { demoService.findById(42) } throws DemoNotFoundException()

        mockMvc.get("/api/v1/demo/42")
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
                jsonPath("$.detail") { value("Demo not found") }
            }
    }

    @Test
    fun `invalid body stays a 400 problem detail without any override`() {
        postInvalidBody().andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
        }
    }
}

@WebMvcTest(DemoController::class)
@ActiveProfiles("problem")
class ProblemDetailPropertyTest : ProblemDetailErrorTest() {

    @Test
    fun `property alone gives a 400 problem detail`() {
        postInvalidBody().andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
        }
    }
}

/**
 * Si la propriété agissait, son advice répondrait en premier : on aurait du `problem+json`, pas ce texte.
 */
@WebMvcTest(DemoController::class)
@ActiveProfiles("problem", "error")
class ProblemDetailPropertyIgnoredTest : ProblemDetailErrorTest() {

    @Test
    fun `property has no effect when an advice extends ResponseEntityExceptionHandler`() {
        postInvalidBody().andExpect {
            status { isBadRequest() }
            content { string("Your arg is invalid") }
        }
    }
}
