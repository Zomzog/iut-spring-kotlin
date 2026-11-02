package bzh.zomzog.iut.amphi.jpa

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import bzh.zomzog.iut.amphi.UserEntity
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Sort
import org.springframework.data.core.PropertyReferenceException
import org.springframework.data.repository.query.parser.PartTree

/**
 * Le nom d'une méthode de repository est analysé par PartTree : le même analyseur que celui du démarrage du contexte.
 */
class DerivedQueryNameTest {

    @Test
    fun `OrderBy without By is read as a property`() {
        assertFailure { PartTree("findAllOrderByIdDesc", UserEntity::class.java) }
            .isInstanceOf(PropertyReferenceException::class)
    }

    @Test
    fun `findAllByOrderBy sorts`() {
        val tree = PartTree("findAllByOrderByIdDesc", UserEntity::class.java)

        assertThat(tree.sort).isEqualTo(Sort.by(Sort.Direction.DESC, "id"))
    }
}
