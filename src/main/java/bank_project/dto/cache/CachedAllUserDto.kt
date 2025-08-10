package bank_project.dto.cache

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class CachedAllUserDto @JsonCreator constructor(
    @JsonProperty("user") val user: CachedUserDto?,
    @JsonProperty("card") val card: CachedUserCardDto?,
    @JsonProperty("account") val account: CachedUserAccountDto?
) : Serializable