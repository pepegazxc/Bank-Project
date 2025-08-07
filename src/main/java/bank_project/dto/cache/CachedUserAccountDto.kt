package bank_project.dto.cache

import bank_project.entity.GoalTemplates
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

data class CachedUserAccountDto @JsonCreator constructor(
    @JsonProperty("accountBalance") val accountBalance: BigDecimal?,
    @JsonProperty("goalId") val accountName: GoalTemplates?,
    @JsonProperty("customGoal") val customGoal: String?,
    @JsonProperty("cipherAccountNumber") val cipherAccountNumber: String?,
) : Serializable