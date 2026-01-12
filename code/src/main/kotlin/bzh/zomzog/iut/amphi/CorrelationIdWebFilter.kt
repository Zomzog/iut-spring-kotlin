package bzh.zomzog.iut.amphi

import org.slf4j.MDC
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.util.UUID

@Component
class CorrelationIdWebFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        val traceparent = request.headers.getFirst("traceparent")
        val xrid = request.headers.getFirst("X-Request-Id") ?: UUID.randomUUID().toString()
        // Put in MDC for logging (note: Reactor context approach better in reactive pipelines)
        MDC.put("X-Request-Id", xrid)
        return chain.filter(exchange)
            .doFinally { _ -> MDC.remove("X-Request-Id") }
    }
}
