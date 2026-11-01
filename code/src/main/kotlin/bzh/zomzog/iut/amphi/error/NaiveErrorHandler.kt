package bzh.zomzog.iut.amphi.error

import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Advice sans `ResponseEntityExceptionHandler` : son fallback capte aussi les exceptions de Spring MVC
 * (ici `HandlerMethodValidationException`), qui deviennent des 500. Voir `HttpErrorHandler` pour la version correcte.
 */
@Profile("naive-error")
@RestControllerAdvice
class NaiveErrorHandler {

    @ExceptionHandler(Exception::class)
    fun fallback(e: Exception) =
        ResponseEntity.internalServerError().body("Unhandled error")
}
