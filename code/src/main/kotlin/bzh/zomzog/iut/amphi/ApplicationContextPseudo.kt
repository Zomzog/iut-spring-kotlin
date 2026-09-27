package bzh.zomzog.iut.amphi

import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.context.annotation.Scope
import org.springframework.web.context.annotation.RequestScope

class ApplicationContextPseudo {
}
class ApplicationContext {
    val beans: List< Bean> = mutableListOf()

    fun <T: Bean> getBean(klass: Class<T>) : T =
        beans.first { it.javaClass.isInstance(klass) } as T

   @RequestScope
    fun pony() {
        BeanDefinition.SCOPE_PROTOTYPE

    }
}

open class Bean