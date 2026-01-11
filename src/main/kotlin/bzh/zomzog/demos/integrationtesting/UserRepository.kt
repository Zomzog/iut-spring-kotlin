package bzh.zomzog.demos.integrationtesting

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, String>
