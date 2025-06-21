package bank_project.DTO.CacheDto

data class UserCacheDto (
    val name: String,
    val surname: String,
    val patronymic: String?,
    val userName: String,
    val phoneNumber: String,
    val email: String,
    val passport: String?,
    val token: String,
    val postalCode: String
)