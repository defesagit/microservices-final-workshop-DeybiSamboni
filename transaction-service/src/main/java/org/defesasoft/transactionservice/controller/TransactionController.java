package org.defesasoft.transactionservice.controller;

import org.defesasoft.transactionservice.dto.GetAccountDTO;
import org.defesasoft.transactionservice.model.Transaction;
import org.defesasoft.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService service;
    private final WebClient webClient;

    public TransactionController(TransactionService service, @Value("${account.service.url}") String accountServiceUrl) {
        this.service = service;
        this.webClient = WebClient.builder().baseUrl(accountServiceUrl).build();
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
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> transfer(@RequestBody Map<String, Object> payload) {
        String fromAccount = (String) payload.get("fromAccount");
        String toAccount = (String) payload.get("toAccount");
        BigDecimal amount = new BigDecimal(payload.get("amount").toString());

        // Consultar cuentas origen y destino en account-service
        Mono<GetAccountDTO> fromAccountMono = webClient.get()
                .uri("/accounts/number/{number}", fromAccount)
                .retrieve()
                .bodyToMono(GetAccountDTO.class);

        Mono<GetAccountDTO> toAccountMono = webClient.get()
                .uri("/accounts/number/{number}", toAccount)
                .retrieve()
                .bodyToMono(GetAccountDTO.class);

        return Mono.zip(fromAccountMono, toAccountMono)
                .flatMap(tuple -> {
                    GetAccountDTO fromAcc = tuple.getT1();
                    GetAccountDTO toAcc = tuple.getT2();
                    boolean sameBank = fromAcc.getBankId().equals(toAcc.getBankId());
                    // Aquí podrías consultar el saldo real de la cuenta origen si tienes ese endpoint
                    return service.processTransfer(fromAccount, toAccount, amount, sameBank);
                });
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
