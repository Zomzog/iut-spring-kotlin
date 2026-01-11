package bzh.zomzog.demos.reactive

import kotlinx.coroutines.delay
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ReactiveController {
    @GetMapping("/reactive/hello")
    suspend fun hello(): String {
        delay(50)
        return "Hello from WebFlux + coroutines"
    }
}
