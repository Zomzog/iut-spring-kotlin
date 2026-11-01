package bzh.zomzog.iut.amphi.validation

import assertk.assertThat
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.method.annotation.HandlerMethodValidationException

@WebMvcTest(StarWarsMovieController::class)
@ActiveProfiles("problem")
class StarWarsMovieControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `no validation accepts any episode`() {
        mockMvc.get("/starwars/movies/no-validation?episode=11")
            .andExpect {
                status { isOk() }
                content { string("") }
            }
    }

    @Test
    fun `validated rejects episode 11 with a problem detail`() {
        val result = mockMvc.get("/starwars/movies/validated?episode=11")
            .andExpect {
                status { isBadRequest() }
                content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
            }
            .andDo { print() }
            .andReturn()

        assertThat(result.resolvedException)
            .isNotNull()
            .isInstanceOf(HandlerMethodValidationException::class)
    }

    @Test
    fun `validated accepts the first trilogy`() {
        mockMvc.get("/starwars/movies/validated?episode=5")
            .andExpect { status { isOk() } }
    }

    @Test
    fun `incoherent bounds reject every value`() {
        listOf(4, 5, 6).forEach { episode ->
            mockMvc.get("/starwars/movies/incoherentValidation?episode=$episode")
                .andExpect { status { isBadRequest() } }
        }
    }

    @Test
    fun `body without validation accepts anything`() {
        mockMvc.post("/starwars/movies/no-validation") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"episode": 11, "name": ""}"""
        }.andExpect { status { isOk() } }
    }

    @Test
    fun `validated body rejects episode 11 and blank name`() {
        val result = mockMvc.post("/starwars/movies/validated") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"episode": 11, "name": ""}"""
        }.andExpect {
            status { isBadRequest() }
            content { contentType(MediaType.APPLICATION_PROBLEM_JSON) }
        }.andDo { print() }
            .andReturn()

        assertThat(result.resolvedException)
            .isNotNull()
            .isInstanceOf(MethodArgumentNotValidException::class)
    }

    @Test
    fun `validated body accepts a valid movie`() {
        mockMvc.post("/starwars/movies/validated") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"episode": 4, "name": "A New Hope"}"""
        }.andExpect { status { isOk() } }
    }
}
