package bzh.zomzog.iut.amphi

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(val cachedService: CachedService) {

    @GetMapping("/caches/random")
    fun random(@RequestParam i: Int) = "${cachedService.randomOnlyOnce(i)} - ${cachedService.randomOnlyOnce2(i)}"
}