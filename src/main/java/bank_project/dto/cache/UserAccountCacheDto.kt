package bank_project.dto.cache

import bank_project.entity.GoalTemplatesEntity
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

data class UserAccountCacheDto @JsonCreator constructor(
    @JsonProperty("accountBalance") val accountBalance: BigDecimal?,
    @JsonProperty("goalId") val accountName: GoalTemplatesEntity?,
    @JsonProperty("customGoal") val customGoal: String?,
    @JsonProperty("cipherAccountNumber") val cipherAccountNumber: String?,
) : Serializable