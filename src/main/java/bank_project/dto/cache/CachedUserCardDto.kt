package bank_project.dto.cache

import bank_project.entity.Cards
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

data class CachedUserCardDto @JsonCreator constructor(
    @JsonProperty("cardNumber") val cardNumber: String?,
    @JsonProperty("cardThreeNumbers") val cardThreeNumbers: String?,
    @JsonProperty("cardExpirationDate") val cardExpirationDate: String?,
    @JsonProperty("CardBalance") val cardBalance: BigDecimal?,
    @JsonProperty("cashback") val cashback: BigDecimal?,
    @JsonProperty("cardType") val cardTypeId: Cards?,
    @JsonProperty("active") val isActive: Boolean?,
) : Serializable