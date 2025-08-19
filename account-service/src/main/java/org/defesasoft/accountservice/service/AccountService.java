package org.defesasoft.accountservice.service;

import org.defesasoft.accountservice.dto.AccountWithTransactionsDTO;
import org.defesasoft.accountservice.dto.GetTransactionDTO;
import org.defesasoft.accountservice.exception.AccountAlreadyExistsException;
import org.defesasoft.accountservice.grpc.TransactionConsumer;
import org.defesasoft.accountservice.grpc.TransactionResponse;

import org.defesasoft.accountservice.model.Account;
import org.defesasoft.accountservice.repository.IAccountRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
public class AccountService {
    private final IAccountRepository accountRepository;
    private final BankService bankService;
    private final TransactionConsumer transactionConsumer;
    private final String msgAccountNotFound = "Account not found";

    public AccountService(IAccountRepository repository, BankService bankServicerepo, TransactionConsumer transactionConsumer) {
        this.accountRepository = repository;
        this.bankService = bankServicerepo;
        this.transactionConsumer = transactionConsumer;
    }

    public Flux<Account> getAll() {
        return accountRepository.findAll();
    }

    public Mono<Account> getById (Long accountId) {
        return accountRepository
                .findById(accountId)
                .switchIfEmpty(Mono.error(new RuntimeException(msgAccountNotFound)));
    }

    public Mono<Account> getByAccountNumber (Long accountNumber) {
        return accountRepository
                .findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new RuntimeException(msgAccountNotFound)));
    }

    public Mono<Account> create(Account account) {
        return accountRepository.existsByAccountNumber(account.getAccountNumber())
                .flatMap(exists -> {
                    if (Boolean.TRUE.equals(exists)) {
                        return Mono.error(new AccountAlreadyExistsException(account.getAccountNumber()));
                    }
                    return bankService.getBank(account.getBankId())
                            .switchIfEmpty(Mono.error(new RuntimeException("Bank does not exist")))
                            .then(Mono.defer(() -> {
                                account.setCreatedAt(LocalDateTime.now());
                                return accountRepository.save(account);
                            }));
                });
    }

    public Mono<Account> updateBalanceByAccountNumber(Long accountNumber, BigDecimal newBalance) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new RuntimeException(msgAccountNotFound)))
                .flatMap(account -> {
                    account.setBalance(newBalance);
                    return accountRepository.save(account);
                });
    }

    public Mono<AccountWithTransactionsDTO> getByAccountNumberWithHistory(Long accountNumber) {
        return accountRepository
                .findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new RuntimeException(msgAccountNotFound)))
                .flatMap(account ->
                        transactionConsumer.getTransactions(accountNumber)
                                .map(transactions -> new AccountWithTransactionsDTO(
                                        account,
                                        transactions // aquí va la lista directamente
                                ))
                );
    }

}