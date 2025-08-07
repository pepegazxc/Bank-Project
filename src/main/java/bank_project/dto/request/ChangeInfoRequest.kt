package bank_project.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ChangeInfoRequest (
    @field:Size(min = 6, max = 15)
    val postalCode: String?,
    @field:Size(min = 6, max = 9)
    val passport: String?,
    @field:Email
    val email: String?,
    @field:Size(min = 8, max = 15)
    val phoneNumber: String?,
    @field:Size(min = 8)
    val password: String?,

    @field:NotBlank
    val passwordForConfirm: String,
)