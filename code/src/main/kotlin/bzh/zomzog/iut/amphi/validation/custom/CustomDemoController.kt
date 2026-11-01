package bzh.zomzog.iut.amphi.validation.custom

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomDemoController {

    @GetMapping("/custom")
    fun list(@RequestParam @MinMax(0, 10) i: Int) = i
}
