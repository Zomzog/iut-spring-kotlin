package bzh.zomzog.prez.springkotlin.dummy.repository

import bzh.zomzog.prez.springkotlin.dummy.domain.Pony
import bzh.zomzog.prez.springkotlin.dummy.domain.PonyType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("PONIES")
data class PonyEntity(
        @Id val id: Int?,
        val name: String,
        val type: PonyType
) {
    fun toDomain() = Pony(id, name, type)
}

fun Pony.asEntity() = PonyEntity(id, name, type)