package bank_project.dto.cache

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

data class UserCacheDto @JsonCreator constructor (
    @JsonProperty("name") val name: String?,
    @JsonProperty("surname") val surname: String?,
    @JsonProperty("patronymic") val patronymic: String?,
    @JsonProperty("userName") val userName: String?,
    @JsonProperty("phoneNumber") val phoneNumber: String?,
    @JsonProperty("email") val email: String?,
    @JsonProperty("passport") val passport: String?,
    @JsonProperty("token") val token: String?,
    @JsonProperty("postalCode") val postalCode: String?,

) : Serializable