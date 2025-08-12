package exception

import exception.custom.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class EntityExceptionHandler {

    @ExceptionHandler(AccountsNotFoundException::class)
    fun handleAccountsNotFoundException(ex: AccountsNotFoundException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.NOT_FOUND)

    @ExceptionHandler(CardsNotFoundException::class)
    fun handleCardsNotFoundException(ex: CardsNotFoundException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.NOT_FOUND)

    @ExceptionHandler(GoalTemplatesNotFoundException::class)
    fun handleGoalNotFoundException(ex: GoalTemplatesNotFoundException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.NOT_FOUND)

    @ExceptionHandler(UserAccountNotFoundException::class)
    fun handleUserAccountNotFoundException(ex: UserAccountNotFoundException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.NOT_FOUND)

    @ExceptionHandler(UserCardNotFoundException::class)
    fun handleUserCardNotFoundException(ex: UserCardNotFoundException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.NOT_FOUND)

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.NOT_FOUND)

    @ExceptionHandler(UserOperationHistoryNotFoundException::class)
    fun handleUserOperationHistoryNotFoundException(ex: UserOperationHistoryNotFoundException) =
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.NOT_FOUND)

    @ExceptionHandler(IncorrectPasswordException::class)
    fun handleIncorrectPasswordException(ex: IncorrectPasswordException)=
        ResponseEntity(mapOf("message" to ex.message.orEmpty()), HttpStatus.UNAUTHORIZED)
}