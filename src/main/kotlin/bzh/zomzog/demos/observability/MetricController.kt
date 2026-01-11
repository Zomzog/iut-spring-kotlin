package bzh.zomzog.demos.observability

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/demo")
class MetricController(private val registry: MeterRegistry) {

    private val counter = registry.counter("demo.requests")

    @GetMapping("/hello")
    fun hello(): String {
        counter.increment()
        return "Hello from observability demo"
    }
}
