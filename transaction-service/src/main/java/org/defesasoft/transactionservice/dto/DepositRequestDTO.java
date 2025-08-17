package org.defesasoft.transactionservice.dto;

import java.math.BigDecimal;

public class DepositRequestDTO {
    private Long toAccount;
    private BigDecimal amount;

    public DepositRequestDTO() {}

    public Long getToAccount() { return toAccount; }
    public void setToAccount(Long toAccount) { this.toAccount = toAccount; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
