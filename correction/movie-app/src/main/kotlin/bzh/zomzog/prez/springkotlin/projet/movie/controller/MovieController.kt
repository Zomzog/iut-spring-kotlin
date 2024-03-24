package bzh.zomzog.prez.springkotlin.projet.movie.controller

import bzh.zomzog.prez.springkotlin.projet.movie.domain.NewMovie
import bzh.zomzog.prez.springkotlin.projet.movie.repository.MovieEntity
import bzh.zomzog.prez.springkotlin.projet.movie.repository.MovieRepository
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.util.retry.Retry
import java.time.Duration

@RestController
class MovieController(val movieRepository: MovieRepository) {

    @GetMapping("/api/v1/movies/{id}")
    fun findById(@PathVariable id: Int) =
            movieRepository.findById(id).map { it.toDomain() }
                    ?.let { ResponseEntity.ok(it) }
                    ?: ResponseEntity.notFound()


    @GetMapping("/api/v1/movies")
    fun findAll() {
        val wc = WebClient.builder().baseUrl("http://localhost:9997")
                .defaultHeaders {
                    it.add("name", "value")
                }
                .build()
        Mono.error<String>(Exception())
        Mono.just("Data")
        val pony = wc.get()
                .uri { uriBuilder: UriBuilder ->
                    uriBuilder.path("/api/ponies")
                            .build()
                }.retrieve()
                .onStatus(
                        { status: HttpStatusCode -> status.isError },
                        { response -> Mono.error(ResponseStatusException(response.statusCode(), "the error")) }
                )
                .bodyToMono<String>()
                .retryWhen(Retry.backoff(5, Duration.ofSeconds(1))
                        .filter {
                            when (it) {
                                is ResponseStatusException -> it.statusCode.is5xxServerError
                                else -> false
                            }
                        })
                .block()
       return movieRepository.findAll().map { it.toDomain() }
                .let { ResponseEntity.ok(it) }
    }
    @PostMapping("/api/v1/movies")
    fun create(@RequestBody newMovie: NewMovie) =
            movieRepository.save(MovieEntity(null, newMovie.name, newMovie.month, newMovie.year))
                    .let { ResponseEntity.status(HttpStatus.CREATED).body(it) }

    @PutMapping("/api/v1/movies/{id}")
    fun update(@PathVariable id: Int, @RequestBody newMovie: NewMovie) =
            movieRepository.save(MovieEntity(id, newMovie.name, newMovie.month, newMovie.year))
                    .let { ResponseEntity.ok(it) }

    @DeleteMapping("/api/v1/movies/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Unit> {
        movieRepository.deleteById(id)
        return ResponseEntity.ok().build()
    }
}