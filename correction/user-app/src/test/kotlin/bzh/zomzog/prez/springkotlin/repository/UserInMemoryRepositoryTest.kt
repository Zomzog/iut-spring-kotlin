package bzh.zomzog.prez.springkotlin.repository

import org.junit.jupiter.api.BeforeEach

class UserInMemoryRepositoryTest: UserDatabaseTest() {

    @BeforeEach
    fun setUp() {
        repository = UserInMemoryRepository()
    }
}