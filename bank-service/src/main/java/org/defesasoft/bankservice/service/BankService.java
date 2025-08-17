package org.defesasoft.bankservice.service;

import org.defesasoft.bankservice.exception.BankNotFoundException;
import org.defesasoft.bankservice.model.Bank;
import org.defesasoft.bankservice.repository.IBankRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BankService {
    private final IBankRepository repository;
    public BankService(IBankRepository repository) {
        this.repository = repository;
    }

    public Flux<Bank> getAll(){ return repository.findAll(); }

    /*public Mono<Bank> getById(Long bankId){
        return repository
                .findById(bankId)
                .switchIfEmpty( Mono.error(new RuntimeException("Bank not found")));
    }*/
    public Mono<Bank> getById(Long bankId){
        return repository
                .findById(bankId)
                .switchIfEmpty(Mono.error(new BankNotFoundException("Bank does not exist with ID: " + bankId)));
    }

    public Mono<Bank> createBank(Bank bank) {
        return repository.findByName(bank.getName())
                .flatMap(existingBank -> Mono.<Bank>error(new IllegalArgumentException("Bank with name '" + bank.getName() + "' already exists")))
                .switchIfEmpty(repository.save(bank));
    }




}
