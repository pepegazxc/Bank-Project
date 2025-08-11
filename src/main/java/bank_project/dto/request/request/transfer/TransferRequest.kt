package bank_project.dto.request.request.transfer

import java.math.BigDecimal

interface TransferRequest {
    fun getTransferRequest(): BigDecimal
}