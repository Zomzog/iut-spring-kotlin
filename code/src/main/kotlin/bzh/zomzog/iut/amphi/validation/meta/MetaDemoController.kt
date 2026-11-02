package bzh.zomzog.iut.amphi.validation.meta

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MetaDemoController {

    @GetMapping("/meta")
    fun list(@RequestParam @MinMax i: Int) = i
}
