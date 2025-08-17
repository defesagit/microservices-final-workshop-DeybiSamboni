package org.defesasoft.accountservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;



@Table(name = "accounts")
public class Account {

    @Id
    private Long id;
    private String accountNumber;
    private String holder;
    private Integer bankId;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Account() {
    }
    public Account(Long id, String accountNumber, String holder, Integer bankId, LocalDateTime createdAt) {
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

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}