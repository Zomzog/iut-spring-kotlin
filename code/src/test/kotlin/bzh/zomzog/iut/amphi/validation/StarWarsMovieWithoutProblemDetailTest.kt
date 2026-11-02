package bzh.zomzog.iut.amphi.validation

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(StarWarsMovieController::class)
class StarWarsMovieWithoutProblemDetailTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `validated rejects episode 11 with a 400 and no body`() {
        mockMvc.get("/starwars/movies/validated?episode=11")
            .andExpect {
                status { isBadRequest() }
                content { string("") }
            }
    }
}
