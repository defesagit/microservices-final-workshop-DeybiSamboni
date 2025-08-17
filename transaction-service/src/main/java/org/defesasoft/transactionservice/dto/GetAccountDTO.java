package org.defesasoft.transactionservice.dto;

import java.math.BigDecimal;

public class GetAccountDTO {
    private Long id;
    private Long accountNumber;
    private String holder;
    private Long bankId;
    private BigDecimal balance;

    public GetAccountDTO() {
    }
    public GetAccountDTO(Long id, Long accountNumber, String holder, Long bankId, BigDecimal balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.holder = holder;
        this.bankId = bankId;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
