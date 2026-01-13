package bzh.zomzog.iut.amphi

import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class CachedService {

    @Cacheable("random")
    fun randomOnlyOnce(max: Int): Int {
        return Random.nextInt(until = max + 1)
    }

    @Cacheable("random2", keyGenerator = "myKeyGenerator")
    fun randomOnlyOnce2(max: Int): Int {
        return Random.nextInt(until = max + 1)
    }
}