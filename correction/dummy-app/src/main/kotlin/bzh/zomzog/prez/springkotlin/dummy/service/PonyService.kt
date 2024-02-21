package bzh.zomzog.prez.springkotlin.dummy.service

import bzh.zomzog.prez.springkotlin.dummy.domain.AlreadyExistError
import bzh.zomzog.prez.springkotlin.dummy.domain.Pony
import bzh.zomzog.prez.springkotlin.dummy.repository.PonyEntity
import bzh.zomzog.prez.springkotlin.dummy.repository.PonyRepository
import bzh.zomzog.prez.springkotlin.dummy.repository.asEntity
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service


@Service
class PonyService(val ponyRepository: PonyRepository) {

    suspend fun createPony(pony: Pony): Pony {
        val before = ponyRepository.findByName(pony.name)
        if (before == null) {
            return ponyRepository.save(pony.asEntity())
                    .toDomain()

        } else {
            throw AlreadyExistError(pony.name)
        }
    }

    suspend fun listPonies() = ponyRepository.findAll().map { it.toDomain() }

    suspend fun finfById(id: Int) = ponyRepository.findById(id)?.toDomain()

    suspend fun deleteById(id: Int) = ponyRepository.deleteById(id)

    suspend fun updateById(id: Int, pony: Pony) =
            ponyRepository.save(PonyEntity(id, pony.name, pony.type)).toDomain()
}