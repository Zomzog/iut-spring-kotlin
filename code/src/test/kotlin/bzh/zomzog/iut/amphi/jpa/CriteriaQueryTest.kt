package bzh.zomzog.iut.amphi.jpa

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.containsExactlyInAnyOrder
import bzh.zomzog.iut.amphi.UserEntity
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import jakarta.persistence.criteria.Path
import jakarta.persistence.criteria.Root
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest

/**
 * Le corps de la requête Criteria de `jpa.md`, sur [UserEntity].
 *
 * Le générique de `Path` est effacé à l'exécution : ce test prouve que le code de la slide tourne, pas que
 * `Path<String>` est le bon type.
 */
@DataJpaTest
class CriteriaQueryTest {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    private val alice = UserEntity(id = 1L, name = "Alice")
    private val bob = UserEntity(id = 2L, name = "Bob")

    @BeforeEach
    fun persistUsers() {
        entityManager.persist(alice)
        entityManager.persist(bob)
    }

    private fun criteria(name: String?): List<UserEntity> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val queryBuilder = criteriaBuilder.createQuery(UserEntity::class.java)
        val root: Root<UserEntity> = queryBuilder.from(UserEntity::class.java)
        var query = queryBuilder.select(root)
        if (name != null) {
            val nameField: Path<String> = root.get("name")
            query = query.where(criteriaBuilder.equal(nameField, name))
        }
        return entityManager.createQuery(query).resultList
    }

    @Test
    fun `a name filters on the column`() {
        assertThat(criteria("Alice")).containsExactly(alice)
    }

    @Test
    fun `no name returns everything`() {
        assertThat(criteria(null)).containsExactlyInAnyOrder(alice, bob)
    }
}
