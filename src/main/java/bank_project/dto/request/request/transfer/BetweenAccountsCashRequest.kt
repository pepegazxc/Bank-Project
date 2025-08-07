package bank_project.dto.request.request.transfer

import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class BetweenAccountsCashRequest (
    @field:Positive
    val toCardCache: BigDecimal?,
    @field:Positive
    val toAccountCache: BigDecimal?,
)