package bzh.zomzog.iut.amphi.error

import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * Version du TL;DR, où elle s'appelle `HttpErrorHandler` : rien n'est surchargé,
 * toutes les erreurs sortent donc en `ProblemDetail`.
 */
@Profile("problem-error")
@RestControllerAdvice
class ProblemDetailErrorHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(DemoNotFoundException::class)
    fun handleDemoNotFound(e: DemoNotFoundException) =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Demo not found")
}
