package org.defesasoft.transactionservice.controller;

import org.defesasoft.transactionservice.dto.GetAccountDTO;
import org.defesasoft.transactionservice.dto.TransferRequestDTO;
import org.defesasoft.transactionservice.dto.TransferResponseDTO;
import org.defesasoft.transactionservice.model.Transaction;
import org.defesasoft.transactionservice.service.AccountService;
import org.defesasoft.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService service;
    private final AccountService accountService;

    public TransactionController(TransactionService service, AccountService accountService) {
        this.service = service;
        this.accountService = accountService;
    }

    @GetMapping
    public Flux<Transaction> getAll(){
        return  service.getAllTransactions();
    }

    @GetMapping("/{transactionId}")
    public Mono<Transaction> getById(@PathVariable Long transactionId) {
        return service.getTransactionById(transactionId);
    }

    @PostMapping("/transfer")
    public Mono<ResponseEntity<TransferResponseDTO>> transfer(@RequestBody TransferRequestDTO payload) {
        return service.validateAndProcessTransfer(payload);
    }




    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Transaction> deposit(@RequestBody Map<String, Object> payload) {
        String toAccount = (String) payload.get("toAccount");
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        return service.deposit(toAccount, amount);
    }

    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Transaction> withdraw(@RequestBody Map<String, Object> payload) {
        String fromAccount = (String) payload.get("fromAccount");
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        return service.withdraw(fromAccount, amount);
    }
}
