package bzh.zomzog.prez.springkotlin.repository

import bzh.zomzog.prez.springkotlin.domain.User
import org.springframework.stereotype.Repository

//@Repository
class UserInMemoryRepository : UserRepository {

    private val map = mutableMapOf<String, User>()

    override fun create(user: User): Result<User> {
        val previous = map.putIfAbsent(user.email, user)
        return if (previous == null) {
            Result.success(user)
        } else {
            Result.failure(Exception("User already exit"))
        }
    }

    override fun list(age: Int?) = if (age == null) {
        map.values.toList()
    } else {
        map.values.filter { it.age == age }
    }

    override fun get(email: String) = map[email]

    override fun update(user: User): Result<User> {
        val updated = map.replace(user.email, user)
        return if (updated == null) {
            Result.failure(Exception("User doesn't exit"))
        } else {
            Result.success(user)
        }
    }

    override fun delete(email: String) = map.remove(email)
}