package bzh.zomzog.prez.springkotlin.dummy.domain

data class Pony(val id: Int?, val name: String, val type: PonyType)

enum class PonyType {
    EARTH,
    PEGASIS,
    UNICORN
}
