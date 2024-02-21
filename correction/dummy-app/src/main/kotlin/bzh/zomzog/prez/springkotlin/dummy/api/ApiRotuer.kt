package bzh.zomzog.prez.springkotlin.dummy.api

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class ApiRotuer {
    @Bean
    fun router(ponyHandler: PonyHandler) = coRouter {
        "/api".nest {
            "/ponies".nest {
                POST("", ponyHandler::createPony)
                GET("", ponyHandler::listPonies)
                "/{id}".nest {
                    GET("", ponyHandler::findById)
                    PUT("", ponyHandler::updateById)
                    DELETE("", ponyHandler::deleteById)
                }
            }
        }
    }
}