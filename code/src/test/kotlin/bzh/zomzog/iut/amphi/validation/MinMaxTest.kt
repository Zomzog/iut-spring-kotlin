package bzh.zomzog.iut.amphi.validation

import bzh.zomzog.iut.amphi.validation.custom.CustomDemoController
import bzh.zomzog.iut.amphi.validation.meta.MetaDemoController
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(controllers = [MetaDemoController::class, CustomDemoController::class])
class MinMaxTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Test
    fun `meta annotation accepts a value in 0 to 10`() {
        mockMvc.get("/meta?i=5").andExpect { status { isOk() } }
    }

    @Test
    fun `meta annotation rejects a value above 10`() {
        mockMvc.get("/meta?i=11").andExpect { status { isBadRequest() } }
    }

    @Test
    fun `custom annotation accepts a value in 0 to 10`() {
        mockMvc.get("/custom?i=5").andExpect { status { isOk() } }
    }

    @Test
    fun `custom annotation rejects a value above 10`() {
        mockMvc.get("/custom?i=11").andExpect { status { isBadRequest() } }
    }
}
