package bzh.zomzog.demos.caching

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class CaffeineConfig {
    @Bean
    fun cacheManager(): CacheManager {
        val cm = CaffeineCacheManager("users")
        cm.setCaffeine(Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(10_000))
        return cm
    }
}
