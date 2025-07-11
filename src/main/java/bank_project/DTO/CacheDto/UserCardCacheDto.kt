package bank_project.DTO.CacheDto

import bank_project.Entity.CardsEntity
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable
import java.math.BigDecimal

data class UserCardCacheDto @JsonCreator constructor(
    @JsonProperty("cardNumber") val cardNumber: String?,
    @JsonProperty("cardThreeNumbers") val cardThreeNumbers: String?,
    @JsonProperty("cardExpirationDate") val cardExpirationDate: String?,
    @JsonProperty("CardBalance") val cardBalance: BigDecimal?,
    @JsonProperty("cashback") val cashback: BigDecimal?,
    @JsonProperty("cardType") val cardTypeId: CardsEntity?,
    @JsonProperty("active") val isActive: Boolean?,
) : Serializable