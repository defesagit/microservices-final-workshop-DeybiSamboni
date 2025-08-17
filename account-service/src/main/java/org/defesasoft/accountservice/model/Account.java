package org.defesasoft.accountservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("accounts")
public class Account {
    @Id
    @Column("account_id")
    private Long id;
    @Column("account_number")
    private String accountNumber;
    @Column("holder")
    private String holder;
    @Column("bank_id")
    private Long bankId;
    @Column("created_at")
    private LocalDateTime createdAt;

    public Account() {
    }

    public Account(Long id, String accountNumber, String holder, Long bankId, LocalDateTime createdAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.holder = holder;
        this.bankId = bankId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
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
}