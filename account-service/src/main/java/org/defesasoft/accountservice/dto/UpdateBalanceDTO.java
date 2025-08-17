package org.defesasoft.accountservice.dto;

import java.math.BigDecimal;

public class UpdateBalanceDTO {
    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
