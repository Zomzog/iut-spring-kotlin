package bzh.zomzog.iut.amphi.error

import org.springframework.stereotype.Service

class DemoNotFoundException : RuntimeException()

@Service
class DemoService {

    fun findById(id: Long): DemoDto = throw DemoNotFoundException()

    fun save(dto: DemoDto) = dto
}
