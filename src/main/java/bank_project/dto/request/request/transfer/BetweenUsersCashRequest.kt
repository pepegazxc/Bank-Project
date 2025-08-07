package bank_project.dto.request.request.transfer

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class BetweenUsersCashRequest (
    @field:Size(min = 8, max = 15)
    val phoneNumber: String?,
    @field:Size(min = 16)
    val cardNumber: String?,
    @field:NotNull
    @field:Positive
    val value: BigDecimal
)
