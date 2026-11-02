package bzh.zomzog.iut.amphi.error

import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

/**
 * Une contrainte sur un paramètre lève HandlerMethodValidationException (pas MethodArgumentNotValidException).
 */
abstract class ParamValidationErrorTest {

    @MockkBean
    protected lateinit var demoService: DemoService
    @Autowired
    protected lateinit var mockMvc: MockMvc
}

@WebMvcTest(DemoController::class)
@ActiveProfiles("naive-error")
class NaiveFallbackSwallowsValidationTest : ParamValidationErrorTest() {

    @Test
    fun `fallback on Exception turns a param validation into a 500`() {
        mockMvc.get("/api/v1/demo?limit=0")
            .andExpect { status { isInternalServerError() } }
    }
}

@WebMvcTest(DemoController::class)
@ActiveProfiles("error")
class BaseClassKeepsValidationTest : ParamValidationErrorTest() {

    @Test
    fun `ResponseEntityExceptionHandler keeps the 400 for a param validation`() {
        mockMvc.get("/api/v1/demo?limit=0")
            .andExpect { status { isBadRequest() } }
    }
}
