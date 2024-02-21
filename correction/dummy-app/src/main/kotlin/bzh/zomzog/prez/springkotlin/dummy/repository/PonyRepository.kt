package bzh.zomzog.prez.springkotlin.dummy.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface PonyRepository : CoroutineCrudRepository<PonyEntity, Int> {
    suspend fun findByName(name: String): PonyEntity?
}