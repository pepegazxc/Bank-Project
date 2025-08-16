package bank_project.exception

import bank_project.exception.custom.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class TransferMoneyExceptionHandler {

    @ExceptionHandler(AmountTransferException::class)
    fun handleAmountTransferException(ex: AmountTransferException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.CONFLICT)

    @ExceptionHandler(InsufficientBalanceException::class)
    fun handleInsufficientBalanceException(ex: InsufficientBalanceException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.CONFLICT)

    @ExceptionHandler(TransferMoneyException::class)
    fun handleTransferMoneyException(ex: TransferMoneyException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.CONFLICT)

    @ExceptionHandler(RecipientNotFoundException::class)
    fun handleRecipientNotFoundException(ex: RecipientNotFoundException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.NOT_FOUND)
}