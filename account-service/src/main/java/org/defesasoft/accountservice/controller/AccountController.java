package org.defesasoft.accountservice.controller;

import org.defesasoft.accountservice.model.Account;
import org.defesasoft.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Flux<Account> getAllAccounts() {
        return accountService.getAll();
    }

    @GetMapping("/id/{id}")
    public Mono<Account> getAccountById(@PathVariable Long id) {
        return accountService.getById(id);
    }

    @GetMapping("/accountable/{accountNumber}")
    public Mono<Account> getAccountByNumberAccount(@PathVariable Long accountNumber) {
        return accountService.getByAccountNumber(accountNumber);
    }


    @PostMapping
    public Mono<ResponseEntity<Account>> create(@RequestBody Account account) {
        return accountService.create(account)
                .map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved));
    }



}