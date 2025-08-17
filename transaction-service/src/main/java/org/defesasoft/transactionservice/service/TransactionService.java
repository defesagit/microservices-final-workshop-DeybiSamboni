package org.defesasoft.transactionservice.service;

import org.defesasoft.transactionservice.dto.GetAccountDTO;
import org.defesasoft.transactionservice.dto.TransferRequestDTO;
import org.defesasoft.transactionservice.dto.TransferResponseDTO;
import org.defesasoft.transactionservice.model.Transaction;
import org.defesasoft.transactionservice.repository.ITransactionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    private final ITransactionRepository repository;
    private final AccountService accountService;

    public TransactionService(ITransactionRepository repository, AccountService accountService) {
        this.repository = repository;
        this.accountService = accountService;
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

    // En TransactionService.java

    public Mono<ResponseEntity<TransferResponseDTO>> validateAndProcessTransfer(TransferRequestDTO payload) {
        Long fromAccountId = Long.valueOf(payload.getFromAccount());
        Long toAccountId   = Long.valueOf(payload.getToAccount());
        BigDecimal amount  = payload.getAmount();

        Mono<GetAccountDTO> fromAccountMono = accountService.getAccount(fromAccountId);
        Mono<GetAccountDTO> toAccountMono   = accountService.getAccount(toAccountId);

        return Mono.zip(fromAccountMono, toAccountMono)
                .flatMap(tuple -> {
                    GetAccountDTO fromAcc = tuple.getT1();
                    GetAccountDTO toAcc   = tuple.getT2();
                    boolean sameBank = fromAcc.getBankId().equals(toAcc.getBankId());

                    return processTransfer(payload.getFromAccount(), payload.getToAccount(), amount, sameBank)
                            .then(Mono.defer(() -> {
                                Transaction transaction = new Transaction();
                                transaction.setFromAccount(payload.getFromAccount());
                                transaction.setToAccount(payload.getToAccount());
                                transaction.setAmount(amount);
                                transaction.setType("TRANSFER");
                                transaction.setTimestamp(LocalDateTime.now());
                                return saveTransaction(transaction, sameBank);
                            }))
                            .map(saved -> {
                                TransferResponseDTO response = new TransferResponseDTO(
                                        "SUCCESS",
                                        "Transferencia realizada con éxito",
                                        saved
                                );
                                return ResponseEntity.status(HttpStatus.CREATED).body(response);
                            });
                })
                .onErrorResume(e ->
                        Mono.fromSupplier(() -> {
                            TransferResponseDTO errorResponse = new TransferResponseDTO(
                                    "FAILURE",
                                    "Error en transferencia: " + e.getMessage(),
                                    null
                            );
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                        })
                );
    }

    public Mono<Transaction> saveTransaction(Transaction transaction, boolean sameBank) {
        // Ejemplo de comisión si es transferencia interbancaria
        if (!sameBank) {
            BigDecimal fee = new BigDecimal("5.00");
            transaction.setAmount(transaction.getAmount().subtract(fee));
        }
        return repository.save(transaction);
    }
}
