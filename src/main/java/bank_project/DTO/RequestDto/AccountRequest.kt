package bank_project.DTO.RequestDto

data class AccountRequest (
    val goal: String? = null,
    val accountType: String?,
    val customGoal: String? = null
)