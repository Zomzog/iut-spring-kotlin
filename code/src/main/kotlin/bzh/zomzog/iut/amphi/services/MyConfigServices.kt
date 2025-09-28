package bzh.zomzog.iut.amphi.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

class AService(db: Database) {
    init {
        println("AService created with db=$db")
    }
}

class Other(db: Database) {
    init {
        println("Other created with db=$db")
    }
}

interface Database

class PostgresDb(private val name: String) : Database {
    init {
        println("PostgresDb created")
    }

    override fun toString(): String {
        return "PostgresDb(name='$name')"
    }
}

@Service("encoreUnNom")
class MyService {
    @Autowired
    lateinit var db: Database

    init {
        println("MyService created with db")
    }
}