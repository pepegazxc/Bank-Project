package bank_project.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size


data class RegistrationRequest (
    @field:NotBlank
    @field:Size(min = 1, max = 50)
    val name: String,
    @field:NotBlank
    @field:Size(min = 1, max = 50)
    val surname: String,
    @field:Size(min = 1, max = 50)
    val patronymic: String? = null,
    @field:NotBlank
    @field:Size(min = 1, max = 50)
    val userName: String,
    @field:NotBlank
    @field:Size(min = 8, max = 15)
    val phoneNumber: String,
    @field:NotBlank
    @field:Email
    val email: String,
    @field:Size(min = 6, max = 9)
    val passport: String? = null,
    @field:NotBlank
    @field:Size(min = 8)
    val password: String,
    @field:NotBlank
    @field:Size(min = 6, max = 15)
    val postalCode: String
)