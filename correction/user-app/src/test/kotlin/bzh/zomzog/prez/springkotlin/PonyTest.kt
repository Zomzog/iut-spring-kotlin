package bzh.zomzog.prez.springkotlin

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
@Da
class PonyTest {

    @BeforeAll
    fun beforeAll() = println("before all")

    @BeforeEach
    fun beforeEach() = println("before each")

    @Test
    fun test1() = println("test1")

    @Test
    fun test2() = println("test2")

    @AfterEach
    fun afterEach() = println("after each")

    @AfterAll
    fun afterAll() = println("after all")
}