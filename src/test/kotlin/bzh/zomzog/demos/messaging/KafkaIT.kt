package bzh.zomzog.demos.messaging

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

@SpringBootTest
class KafkaIT {
    companion object {
        val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.4.0"))
            .apply { start() }
    }

    @Autowired
    lateinit var template: KafkaTemplate<String, String>

    @Test
    fun `send message`() {
        template.send("demo-topic", "hello")
        // consumer will print consumed message; test ensures no exception
    }
}
