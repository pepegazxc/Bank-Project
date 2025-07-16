package bank_project.DTO.RequestDto.TransferRequestDto

import jakarta.validation.constraints.Positive
import java.math.BigDecimal

data class BetweenAccountsCacheRequest (
    @field:Positive
    val toCardCache: BigDecimal?,
    @field:Positive
    val toAccountCache: BigDecimal?,
)