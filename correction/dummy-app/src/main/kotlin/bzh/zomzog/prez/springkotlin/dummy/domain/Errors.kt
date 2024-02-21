package bzh.zomzog.prez.springkotlin.dummy.domain

sealed class Errors: Exception()

class AlreadyExistError(val name: String): Errors()