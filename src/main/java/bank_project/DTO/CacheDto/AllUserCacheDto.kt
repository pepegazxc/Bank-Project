package bank_project.DTO.CacheDto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class AllUserCacheDto @JsonCreator constructor(
    @JsonProperty("user") val user: UserCacheDto,
    @JsonProperty("card") val card: UserCardCacheDto,
    @JsonProperty("account") val account: UserAccountCacheDto
) : Serializable