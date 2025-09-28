package bzh.zomzog.iut.amphi

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
    fun header(@RequestHeader name: String) = "Header $name"

    @PutMapping("/hello")
    fun putCall() = "Hello World"

    @DeleteMapping("/hello")
    fun deleteCall() = "Hello World"
}
