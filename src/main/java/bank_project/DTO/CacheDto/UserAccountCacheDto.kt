package bank_project.DTO.CacheDto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

data class UserAccountCacheDto @JsonCreator constructor(
    @JsonProperty("accountBalance") val accountBalance: BigDecimal?,
    @JsonProperty("customGoal") val customGoal: String?,
    @JsonProperty("cipherAccountNumber") val cipherAccountNumber: String?,
) : Serializable