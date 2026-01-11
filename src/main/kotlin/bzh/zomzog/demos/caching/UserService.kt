package bzh.zomzog.demos.caching

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class UserService {
    @Cacheable("users")
    fun findById(id: String): String {
        Thread.sleep(200) // simulate slow call
        return "user-$id"
    }
}
