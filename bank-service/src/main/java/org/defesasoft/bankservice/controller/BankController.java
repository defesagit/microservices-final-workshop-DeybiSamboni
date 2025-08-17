package org.defesasoft.bankservice.controller;

import org.defesasoft.bankservice.model.Bank;
import org.defesasoft.bankservice.service.BankService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/banks")
public class BankController {
    private final BankService service;

    public BankController(BankService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<Bank> getAll(){
        return service.getAll();
    }

    @GetMapping ("/{bankId}")
    public Mono<Bank> getById(@PathVariable Long bankId){
        return service.getById(bankId);
    }

    @PostMapping
    public Mono<Bank> createBank(@RequestBody Bank bank){
        if (bank.getBankId() != null) {
            return Mono.error(new IllegalArgumentException("Bank ID should not be provided for creation"));
        }
        if (bank.getName() == null || bank.getName().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Bank name is required"));
        }
        return service.createBank(bank);
    }



}
