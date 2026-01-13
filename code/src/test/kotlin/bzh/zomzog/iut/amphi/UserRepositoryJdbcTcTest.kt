package bzh.zomzog.iut.amphi

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import javax.sql.DataSource

@ActiveProfiles("tc-jdbc")
@DataJpaTest
class UserRepositoryJdbcTcTest @Autowired constructor(
    val dataSource: DataSource,
) {

    @Test
    fun `jdbc tc should start postgres and init script created users table`() {
        dataSource.connection.use { conn ->
            val stmt = conn.prepareStatement("SELECT count(*) FROM users")
            val rs = stmt.executeQuery()
            rs.next()
            // if we can query the table, the jdbc:tc init script ran
            val count = rs.getInt(1)
            assert(count >= 0)
        }
    }
}
