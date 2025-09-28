package bzh.zomzog.iut.amphi.config

import bzh.zomzog.iut.amphi.services.AService
import bzh.zomzog.iut.amphi.services.Database
import bzh.zomzog.iut.amphi.services.Other
import bzh.zomzog.iut.amphi.services.PostgresDb
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
@ComponentScan("bzh.zomzog.another")
class MyConfig {
    @Bean
    @Primary
    fun myDb() = PostgresDb("primary")

    @Bean("secondary")
    fun my2ndDb() = PostgresDb("secondary")

    @Bean
    fun aService(@Qualifier("secondary") db: Database) = AService(db) // primary db
}

//@Configuration
//class MyConfig(val db: Database) {
//    @Bean
//    fun aService() = AService(db)
//
//    @Bean
//    fun another() = Other(db)
//}
//
//@Configuration
//class MyDatabaseConfig() {
//    @Bean
//    fun myDb() = PostgresDb()
//}
/*
@Configuration
class MyConfig {
    @Bean
    fun aService(db: Database) = AService(db)

    @Bean
    fun another(db: Database) = Other(db)
}

@Configuration
class MyDatabaseConfig() {
    @Bean
    fun myDb() = PostgresDb()
}
*/
/*
@Configuration
class MyConfig {
    @Bean
    fun myDb() = PostgresDb()

    @Bean
    fun aService() = AService(myDb())

    @Bean
    fun another() = Other(myDb())
}
*/
