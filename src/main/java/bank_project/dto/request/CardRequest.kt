package bank_project.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CardRequest (
    @field:Size(min = 1, max = 50)
    @field:NotBlank
    val name: String,
    @field:Size(min = 1, max = 50)
    @field:NotBlank
    val surname: String,
    val patronymic: String? = null,
    @field:Size(min = 8, max = 15)
    @field:NotBlank
    val phoneNumber: String,
    @field:Size(min = 6, max = 9)
    @field:NotBlank
    val passport: String,
    @field:NotBlank
    @field:Email
    val email: String,
    @field:NotBlank
    val address: String,
    @field:NotBlank
    val cardType: String
)