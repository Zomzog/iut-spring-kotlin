package bzh.zomzog.iut.amphi.validation

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/starwars/movies/legacy")
class LegacyStarWarsMovieController {

    @GetMapping
    fun legacy(@RequestParam @Min(4) @Max(6) episode: Int) {
    }
}
