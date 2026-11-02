package bzh.zomzog.iut.amphi.validation.meta

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.reflect.KClass

@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
@Retention(RUNTIME)
@Constraint(validatedBy = [])
@Min(value = 0)
@Max(value = 10)
annotation class MinMax(
    val message: String = "Value must be between min and max",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)
