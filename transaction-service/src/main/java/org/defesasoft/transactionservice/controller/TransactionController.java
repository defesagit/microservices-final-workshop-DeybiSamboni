package org.defesasoft.transactionservice.controller;

import org.defesasoft.transactionservice.dto.TransferRequestDTO;
import org.defesasoft.transactionservice.dto.TransferResponseDTO;
import org.defesasoft.transactionservice.dto.DepositRequestDTO;
import org.defesasoft.transactionservice.model.Transaction;
import org.defesasoft.transactionservice.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.Map;


@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
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

    /*@PostMapping("/deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Transaction> deposit(@RequestBody Map<Long, Object> payload) {
        Long toAccount = (Long) payload.get("toAccount");
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        return service.deposit(toAccount, amount);
    }*/

    @PostMapping("/deposit")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Transaction> deposit(@RequestBody DepositRequestDTO payload) {
        return service.deposit(payload.getToAccount(), payload.getAmount());
    }

    @PostMapping("/withdraw")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Transaction> withdraw(@RequestBody Map<Long, Object> payload) {
        Long fromAccount = (Long) payload.get("fromAccount");
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());
        return service.withdraw(fromAccount, amount);
    }

    @GetMapping("/from/{accountNumber}")
    public Flux<Transaction> getByFromAccount(@PathVariable Long accountNumber) {
        return service.getTransactionsByFromAccount(accountNumber);
    }

    @GetMapping("/to/{accountNumber}")
    public Flux<Transaction> getByToAccount(@PathVariable Long accountNumber) {
        return service.getTransactionsByToAccount(accountNumber);
    }

    @GetMapping("/account/{accountNumber}")
    public Flux<Transaction> getByAccount(@PathVariable Long accountNumber) {
        return service.getTransactionsByAccount(accountNumber);
    }

}
