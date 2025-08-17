package org.defesasoft.accountservice.exception;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String accountNumber) {
        super("La cuenta con número " + accountNumber + " ya existe");
    }
}

