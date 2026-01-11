package bzh.zomzog.demos.messaging

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class Consumer {
    @KafkaListener(topics = ["demo-topic"], groupId = "demo-group")
    fun listen(message: String) {
        println("Consumed: $message")
    }
}
