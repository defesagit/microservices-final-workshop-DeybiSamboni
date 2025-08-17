package org.defesasoft.accountservice.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    private final Long accountNumber;

    public AccountAlreadyExistsException(Long accountNumber) {
        super("The account with number " + accountNumber + " already exists");
        this.accountNumber = accountNumber;
    }

    public AccountAlreadyExistsException(Long accountNumber, String message) {
        super(message);
        this.accountNumber = accountNumber;
    }

    public Long getAccountNumber() {
        return accountNumber;
    }
}

