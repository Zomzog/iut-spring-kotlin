package bzh.zomzog.iut.amphi

import org.springframework.web.client.RestTemplate

class Pony {
    fun yolo() {
        RestTemplate().getForObject("http://example.com", String::class.java)
    }
}