package bank_project.DTO

import jakarta.validation.constraints.NotBlank

data class LoginRequest (
    @field:NotBlank
    val userName: String,
    @field:NotBlank
    val password: String
)
