package org.defesasoft.accountservice.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    private final String accountNumber;

    public AccountAlreadyExistsException(String accountNumber) {
        super("The account with number " + accountNumber + " already exists");
        this.accountNumber = accountNumber;
    }

    public AccountAlreadyExistsException(String accountNumber, String message) {
        super(message);
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}

