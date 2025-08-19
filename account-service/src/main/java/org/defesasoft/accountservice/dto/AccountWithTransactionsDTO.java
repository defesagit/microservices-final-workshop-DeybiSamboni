package org.defesasoft.accountservice.dto;

import org.defesasoft.accountservice.model.Account;

import java.util.List;

public class AccountWithTransactionsDTO {
    private Account account;
    private List<GetTransactionDTO> transactions;

    public AccountWithTransactionsDTO() {
    }

    public AccountWithTransactionsDTO(Account account, List<GetTransactionDTO> transactions) {
        this.account = account;
        this.transactions = transactions;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<GetTransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<GetTransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
