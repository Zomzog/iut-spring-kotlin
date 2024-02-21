package bzh.zomzog.prez.springkotlin.dummy.config

import org.h2.tools.Server
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.ContextClosedEvent
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import java.sql.SQLException

@Configuration
class H2Config {
    private lateinit var webServer: Server

    private lateinit var tcpServer: Server

    @EventListener(ContextRefreshedEvent::class)
    @Throws(SQLException::class)
    fun start() {
        this.webServer = Server.createWebServer("-webPort", "8082", "-tcpAllowOthers").start()
        this.tcpServer = Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start()
    }

    @EventListener(ContextClosedEvent::class)
    fun stop() {
        tcpServer.stop()
        webServer.stop()
    }
}