package bzh.zomzog.prez.springkotlin.errors

sealed class FunctionalErrors(message: String = "", cause: Exception? = null) :
    Exception(message, cause)

class UserNotFoundError(email: String) : FunctionalErrors(message = "User $email not found")

