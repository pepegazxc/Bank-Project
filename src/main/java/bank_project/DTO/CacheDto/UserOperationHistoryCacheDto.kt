package bank_project.DTO.CacheDto

import bank_project.Entity.UserContactEntity
import bank_project.Entity.UserOperationHistoryEntity.OperationType
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal
import java.time.LocalDateTime

data class UserOperationHistoryCacheDto @JsonCreator constructor (
    @JsonProperty("contactId") val contactId: UserContactEntity?,
    @JsonProperty("operationType") val operationType: OperationType?,
    @JsonProperty("amount") val amount: BigDecimal?,
    @JsonProperty("time") val time: LocalDateTime?
)