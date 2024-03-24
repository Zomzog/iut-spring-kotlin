package bzh.zomzog.prez.springkotlin.domain

data class User(val email: String, val firstName: String, val lastName: String, val age: Int, val favoriteMovies: List<Movie> = listOf())

data class Movie(val id: Int, val name: String, val releaseDate: String)