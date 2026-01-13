package bzh.zomzog.iut.amphi

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TelemetryController(val demo: SpanDemoService) {
    @GetMapping("/demo")
    fun demo() = demo.process()
}
