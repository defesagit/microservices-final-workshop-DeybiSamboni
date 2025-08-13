package org.defesasoft.bankservice.service;

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

    public Mono<Bank> getById(Long BankId){
        return repository
                .findById(BankId)
                .switchIfEmpty( Mono.error(new RuntimeException("Bank not found")));
    }
}
