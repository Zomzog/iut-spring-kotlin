package bzh.zomzog.iut.amphi.error

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/demo")
class DemoController(private val demoService: DemoService) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) = demoService.findById(id)

    @PostMapping
    fun create(@RequestBody @Valid dto: DemoDto) = demoService.save(dto)
}
