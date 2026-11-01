package bzh.zomzog.iut.amphi.error

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Sans ce profil, `@WebMvcTest` appliquerait cet advice à tous les contrôleurs, dont ceux de `validation`.
 * Le profil n'apparaît pas sur les slides.
 */
@Profile("error")
@RestControllerAdvice
class HttpErrorHandler : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(
        e: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        return ResponseEntity.badRequest().body("Your arg is invalid")
    }

    @ExceptionHandler(DemoNotFoundException::class)
    fun handleDemoNotFound(e: DemoNotFoundException) =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body("Demo not found")
}
