package org.defesasoft.accountservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;



@Table(name = "accounts")
public class Account {

    @Id
    private Long id;
    private String account_number;
    private String holder;
    private String bankCode;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Account() {
    }

    public Account(Long id, String number, String holder, String bankCode, LocalDateTime createdAt) {
        this.id = id;
        this.account_number = number;
        this.holder = holder;
        this.bankCode = bankCode;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getHolder() {
        return holder;
    }

    public void setHolder(String holder) {
        this.holder = holder;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}