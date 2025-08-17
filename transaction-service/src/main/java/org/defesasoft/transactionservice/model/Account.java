package org.defesasoft.transactionservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("accounts")
public class Account {
    @Id
    @Column("account_id")
    private Long id;
    @Column("account_number")
    private Long accountNumber;
    @Column("holder")
    private String holder;
    @Column("bank_id")
    private Long bankId;
    @Column("created_at")
    private LocalDateTime createdAt;
    @Column("balance")
    private BigDecimal balance;

    public Account() {
    }

    public Account(Long id, Long accountNumber, String holder, Long bankId, LocalDateTime createdAt, BigDecimal balance) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.holder = holder;
        this.bankId = bankId;
        this.createdAt = createdAt;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
