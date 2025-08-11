package bank_project.entity.interfaces

import java.math.BigDecimal

interface BalanceHolder {
    fun getBalance() : BigDecimal
    fun setBalance(balance: BigDecimal)
}