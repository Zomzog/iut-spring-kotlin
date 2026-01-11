package bzh.zomzog.demos.messaging

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class Producer(private val kafka: KafkaTemplate<String, String>) {
    fun send(message: String) {
        kafka.send("demo-topic", message)
    }
}
