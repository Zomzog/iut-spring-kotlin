package bzh.zomzog.iut.amphi

import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.body

class Pony {
    fun yolo(pony: PonyDto) {
        val client = RestClient.builder()
            .baseUrl("http://localhost:8080")
            .build()
        val result = client.post()
            .uri("/api/v1/ponies")
            .body(pony)
            .retrieve()
            .onStatus({ it.isError },
                { req, res -> throw MyException("${req.uri} -> ${res.statusCode}") })
            .body<PonyDto>()
    }
}

data class PonyDto(val yo:String)

class MyException(msg:String): Exception(msg)