package bzh.zomzog.iut.amphi.validation

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/starwars/movies")
class StarWarsMovieController {

    @GetMapping("/no-validation")
    fun noValidation(@RequestParam episode: Int) {
    }

    @GetMapping("/validated")
    fun validated(@RequestParam @Min(4) @Max(6) episode: Int) {
    }

    @GetMapping("/incoherentValidation")
    fun incoherentValidation(@RequestParam @Min(6) @Max(4) episode: Int) {
    }

    @PostMapping("/no-validation")
    fun noValidationBody(@RequestBody dto: UnconstrainedStarWarsMovieDto) {
    }

    @PostMapping("/validated")
    fun validatedBody(@RequestBody @Valid dto: StarWarsMovieDto) {
    }
}
