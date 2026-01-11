package bzh.zomzog.demos.messaging

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaConfig {
    @Bean
    fun topic(): NewTopic = NewTopic("demo-topic", 1, 1)
}
