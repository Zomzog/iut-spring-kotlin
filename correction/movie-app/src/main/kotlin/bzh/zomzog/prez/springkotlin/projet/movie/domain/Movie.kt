package bzh.zomzog.prez.springkotlin.projet.movie.domain

data class Movie(val id: Int, val name: String, val releaseDate: String)
data class NewMovie(val name: String, val month: Int, val year: Int)
