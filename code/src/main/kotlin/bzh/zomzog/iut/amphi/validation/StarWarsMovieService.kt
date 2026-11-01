package bzh.zomzog.iut.amphi.validation

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated

@Service
@Validated
class StarWarsMovieService {

    fun find(@Min(4) @Max(6) episode: Int) = "Episode $episode"
}

@Service
class UnvalidatedStarWarsMovieService {

    fun find(@Min(4) @Max(6) episode: Int) = "Episode $episode"
}
