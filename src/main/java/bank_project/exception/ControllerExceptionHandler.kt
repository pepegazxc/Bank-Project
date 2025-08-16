package bank_project.exception

import bank_project.exception.custom.ControllerException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(ControllerException::class)
    fun handleControllerException(ex: ControllerException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.CONFLICT)
}