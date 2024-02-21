package bzh.zomzog.prez.springkotlin.dummy.api.dto

import bzh.zomzog.prez.springkotlin.dummy.domain.Pony
import bzh.zomzog.prez.springkotlin.dummy.domain.PonyType
import java.util.UUID

data class PonyDTO(val id: Int?, val name: String, val type: PonyType) {
    fun toDomain() = Pony(id,name, type)
}

fun Pony.asDTO() = PonyDTO(id, name, type)