package org.defesasoft.bankservice.controller;

import org.defesasoft.bankservice.model.Bank;
import org.defesasoft.bankservice.service.BankService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/banks")
public class BankController {
    private final BankService service;

    public BankController(BankService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<Bank> getAll(){ return service.getAll(); }

    @GetMapping ("/{bankId}")
    public Mono<Bank> getById(@PathVariable Long bankId){
        return service.getById(bankId);
    }


}
