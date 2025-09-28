package bzh.zomzog.iut.amphi

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<AmphiApplication>().with(TestcontainersConfiguration::class).run(*args)
}
