package bank_project.DTO.RequestDto

data class AccountRequest (
    val goal: String?,
    val accountType: String,
    val customGoal: String?
)