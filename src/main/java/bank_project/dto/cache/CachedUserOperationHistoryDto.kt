package bank_project.dto.cache

import bank_project.entity.UserContact
import bank_project.entity.UserOperationHistory.OperationType
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime

data class CachedUserOperationHistoryDto @JsonCreator constructor (
    @JsonProperty("contactId") val contactId: UserContact?,
    @JsonProperty("operationType") val operationType: OperationType?,
    @JsonProperty("amount") val amount: BigDecimal?,
    @JsonProperty("time") val time: LocalDateTime?
)