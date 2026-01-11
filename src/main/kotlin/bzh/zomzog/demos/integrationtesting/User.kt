package bzh.zomzog.demos.integrationtesting

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class User(
    @Id
    val id: String,
    val name: String
)
