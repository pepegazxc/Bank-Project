package bank_project.dto.request

data class AccountRequest (
    val goal: String?,
    val accountType: String,
    val customGoal: String?
)