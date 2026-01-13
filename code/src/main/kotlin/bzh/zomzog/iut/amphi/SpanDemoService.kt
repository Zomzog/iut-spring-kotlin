package bzh.zomzog.iut.amphi

import io.opentelemetry.api.trace.Tracer
import org.springframework.stereotype.Service

@Service
class SpanDemoService(val tracer: Tracer) {
    fun process(): String {
        val span = tracer.spanBuilder("process-business").startSpan()
        return try {
            span.setAttribute("demo.attr", "ok")
            "done"
        } finally {
            span.end()
        }
    }
}
