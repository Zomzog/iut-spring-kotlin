package bzh.zomzog.iut.amphi.validation

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class UnconstrainedStarWarsMovieDto(
    val episode: Int,
    val name: String
)

data class StarWarsMovieDto(
    @field:Min(4)
    @field:Max(6)
    val episode: Int,
    @field:NotBlank
    val name: String
)
