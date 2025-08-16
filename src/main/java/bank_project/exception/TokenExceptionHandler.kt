package bank_project.exception

import bank_project.exception.custom.TokenVerificationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class TokenExceptionHandler {

    @ExceptionHandler(TokenVerificationException::class)
    fun handleTokenVerificationException(ex: TokenVerificationException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.CONFLICT)
}