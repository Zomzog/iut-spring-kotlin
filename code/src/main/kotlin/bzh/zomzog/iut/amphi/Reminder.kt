package bzh.zomzog.iut.amphi

import jakarta.persistence.Entity
import jakarta.persistence.Id
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@ConfigurationProperties(prefix = "my")
data class MyProperties(val enabled: Boolean)

@EnableConfigurationProperties(MyProperties::class)
@Configuration
class MyConfig2 {
    @Bean
    fun something(prop: MyProperties) = Something(prop)
}

@Service
class ServiceA(
    val something: Something,
    @Value("\${app.name}") val appName: String,
    val db: MyJpa,
)

@Component
class ServiceB {
    @Autowired
    lateinit var something: Something
}
interface MyJpa : JpaRepository<MyEntity, Long>

@Entity

class MyEntity(@Id val id:Long)

class Something(val myProp: MyProperties)