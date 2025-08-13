package bank_project.exception

import exception.custom.EmptyDtoException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class DtoExceptionHandler {

    @ExceptionHandler(EmptyDtoException::class)
    fun handleEmptyDtoException(ex: EmptyDtoException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.NOT_FOUND)
}