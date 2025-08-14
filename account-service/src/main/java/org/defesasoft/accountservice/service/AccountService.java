package org.defesasoft.accountservice.service;

import org.defesasoft.accountservice.model.Account;
import org.defesasoft.accountservice.repository.IAccountRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    private final IAccountRepository accountRepository;
    public AccountService(IAccountRepository repository) {
        this.accountRepository = repository;
    }
    public Flux<Account> getAll() {
        return accountRepository.findAll();
    }
    public Mono<Account> getById(Long accountId) {
        return accountRepository
                .findById(accountId)
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found")));
    }
    public Mono<Account> create(Account account) {
        return accountRepository.save(account);
    }
}