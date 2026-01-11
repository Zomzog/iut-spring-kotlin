package bzh.zomzog.iut.amphi.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit.MINUTES

@Configuration
@EnableCaching
class MyCacheConfig {

    @Bean
    fun cacheManager(): CacheManager = CaffeineCacheManager()
        .apply {
            setCaffeine(
                Caffeine.newBuilder()
                    .maximumSize(500)
                    .expireAfterWrite(10, MINUTES)
            )
        }

    @Bean fun myKeyGenerator(): KeyGenerator = KeyGenerator { target, method, params ->
        method.name + params.joinToString("_") { it.toString() }
    }

}