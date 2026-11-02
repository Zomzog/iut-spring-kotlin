package bzh.zomzog.iut.amphi.validation

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.tomcat.autoconfigure.servlet.TomcatServletWebServerAutoConfiguration
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.autoconfigure.DispatcherServletAutoConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureWebMvc
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

/**
 * MockMvc ne fait pas le dispatch vers /error : pour voir le body "legacy" de Spring
 * (BasicErrorController), il faut un vrai serveur.
 */
@SpringBootTest(classes = [StarWarsMovieServerTest.Config::class], webEnvironment = RANDOM_PORT)
class StarWarsMovieServerTest {

    @TestConfiguration
    @AutoConfigureWebMvc
    @ImportAutoConfiguration(
        TomcatServletWebServerAutoConfiguration::class,
        DispatcherServletAutoConfiguration::class,
        JacksonAutoConfiguration::class,
    )
    @Import(StarWarsMovieController::class, LegacyStarWarsMovieController::class)
    class Config

    @Value("\${local.server.port}")
    var port: Int = 0

    private fun get(path: String): HttpResponse<String> =
        HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI("http://localhost:$port$path")).GET().build(),
            HttpResponse.BodyHandlers.ofString()
        )

    @Test
    fun `validated gives a 400 with the legacy spring body`() {
        val response = get("/starwars/movies/validated?episode=11")

        println(response.body())
        assertThat(response.statusCode()).isEqualTo(400)
        assertThat(response.body()).contains("\"error\":\"Bad Request\"")
    }

    @Test
    fun `legacy validated controller gives a 500`() {
        val response = get("/starwars/movies/legacy?episode=11")

        println(response.body())
        assertThat(response.statusCode()).isEqualTo(500)
    }
}
