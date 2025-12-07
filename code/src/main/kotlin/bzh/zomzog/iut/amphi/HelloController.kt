package bzh.zomzog.iut.amphi

import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*

@RestController
class HelloController {

//    @RequestMapping(method = [RequestMethod.GET], path = ["/hello"])
//    fun getCall(@RequestParam name: String) = "Hello $name"

    @GetMapping("/hello/{name}")
    fun path(@PathVariable name: String) = "Hello $name"

    @PostMapping("/hello")
    fun body(@RequestBody name: String) = "Body $name"

    @GetMapping("/hello")
    @Transactional(transactionManager = "transactionManager",
                   timeout = 30,
                   propagation = Propagation.REQUIRED,
                   isolation = Isolation.DEFAULT)
    fun header(@RequestHeader name: String) = "Header $name"

    @PutMapping("/hello")
    fun putCall() = "Hello World"

    @DeleteMapping("/hello")
    fun deleteCall() = "Hello World"
}
