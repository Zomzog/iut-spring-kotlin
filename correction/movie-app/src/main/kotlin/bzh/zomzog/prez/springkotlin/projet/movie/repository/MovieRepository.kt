package bzh.zomzog.prez.springkotlin.projet.movie.repository

import bzh.zomzog.prez.springkotlin.projet.movie.domain.Movie
import jakarta.persistence.*
import org.springframework.data.jpa.repository.JpaRepository

interface MovieRepository : JpaRepository<MovieEntity, Int>

@Table(name = "movies")
@Entity
class MovieEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Int?,
        val name: String,
        @Column(name = "the_month") val month: Int,
        @Column(name = "the_year")        val year: Int) {
    fun toDomain() = id?.let {
        Movie(it, name, "$year-$month")
    } ?: throw Exception("Impossible !")
}
