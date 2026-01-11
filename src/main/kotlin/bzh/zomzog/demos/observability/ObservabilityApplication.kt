package bzh.zomzog.demos.observability

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ObservabilityApplication

fun main(args: Array<String>) {
    runApplication<ObservabilityApplication>(*args)
}
