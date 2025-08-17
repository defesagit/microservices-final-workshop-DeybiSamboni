package org.defesasoft.transactionservice.service;

import org.defesasoft.transactionservice.model.Transaction;
import org.defesasoft.transactionservice.repository.ITransactionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    private final ITransactionRepository repository;

    public TransactionService(ITransactionRepository repository) {
        this.repository = repository;
    }

    public Flux<Transaction> getAllTransactions() {
        return repository.findAll();
    }

    public Mono<Transaction> getTransactionById(Long transactionId) {
        return repository.findById(transactionId);
    }

    public Mono<Transaction> deposit(String toAccount, BigDecimal amount) {
        Transaction deposit = new Transaction(null, null, toAccount, "DEPOSIT", amount, LocalDateTime.now());
        return repository.save(deposit);
    }

    public Mono<Transaction> withdraw(String fromAccount, BigDecimal amount) {
        // Aquí deberías validar fondos suficientes antes de continuar
        Transaction withdrawal = new Transaction(null, fromAccount, null, "WITHDRAWAL", amount, LocalDateTime.now());
        return repository.save(withdrawal);
    }

    public Mono<Void> processTransfer(String fromAccount, String toAccount, BigDecimal amount, boolean sameBank) {
        // Validar fondos suficientes (simulado)
        return withdraw(fromAccount, amount)
            .flatMap(withdrawal -> {
                if (sameBank) {
                    return deposit(toAccount, amount).then();
                } else {
                    // Aquí deberías enviar a una cola de mensajería la transacción de depósito
                    return Mono.empty();
                }
            });
    }
}
