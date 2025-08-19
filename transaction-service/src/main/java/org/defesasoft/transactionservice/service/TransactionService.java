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

    public Mono<Transaction> deposit(Long toAccount, BigDecimal amount) {
        Transaction deposit = new Transaction(null, null, toAccount, "DEPOSIT", amount, LocalDateTime.now());
        return repository.save(deposit);
    }

    public Mono<Transaction> withdraw(Long fromAccount, BigDecimal amount) {
        // Aquí deberías validar fondos suficientes antes de continuar
        Transaction withdrawal = new Transaction(null, fromAccount, null, "WITHDRAWAL", amount, LocalDateTime.now());
        return repository.save(withdrawal);
    }

    public Mono<Void> processTransfer(Long fromAccount, Long toAccount, BigDecimal amount, boolean sameBank) {
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

    /*public Mono<ResponseEntity<TransferResponseDTO>> validateAndProcessTransfer(TransferRequestDTO payload) {
        Long fromAccountNumber = Long.valueOf(payload.getFromAccount());
        Long toAccountNumber = Long.valueOf(payload.getToAccount());
        BigDecimal amount = payload.getAmount();

        Mono<GetAccountDTO> fromAccountMono = accountService.getAccount(fromAccountNumber);
        Mono<GetAccountDTO> toAccountMono = accountService.getAccount(toAccountNumber);

        return Mono.zip(fromAccountMono, toAccountMono)
                .flatMap(tuple -> {
                    GetAccountDTO fromAcc = tuple.getT1();
                    GetAccountDTO toAcc = tuple.getT2();
                    boolean sameBank = fromAcc.getBankId().equals(toAcc.getBankId());

                    if (fromAcc.getBalance().compareTo(amount) < 0) {
                        TransferResponseDTO errorResponse = new TransferResponseDTO(
                                "FAILURE",
                                "Insufficient funds",
                                null
                        );
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
                    }

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
                                        "Transfer completed successfully",
                                        saved
                                );
                                return ResponseEntity.status(HttpStatus.CREATED).body(response);
                            });
                })
                .onErrorResume(e ->
                        Mono.fromSupplier(() -> {
                            TransferResponseDTO errorResponse = new TransferResponseDTO(
                                    "FAILURE",
                                    "Transfer error: " + e.getMessage(),
                                    null
                            );
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
                        })
                );
    }*/

    public Mono<ResponseEntity<TransferResponseDTO>> validateAndProcessTransfer(TransferRequestDTO payload) {
        Long fromAccountNumber = payload.getFromAccount();
        Long toAccountNumber = payload.getToAccount();
        BigDecimal amount = payload.getAmount();

        Mono<GetAccountDTO> fromAccountMono = accountService.getAccount(fromAccountNumber);
        Mono<GetAccountDTO> toAccountMono = accountService.getAccount(toAccountNumber);

        return Mono.zip(fromAccountMono, toAccountMono)
                .flatMap(tuple -> {
                    GetAccountDTO fromAcc = tuple.getT1();
                    GetAccountDTO toAcc = tuple.getT2();
                    boolean sameBank = fromAcc.getBankId().equals(toAcc.getBankId());

                    if (fromAcc.getBalance().compareTo(amount) < 0) {
                        TransferResponseDTO errorResponse = new TransferResponseDTO(
                                "FAILURE",
                                "Insufficient funds",
                                null
                        );
                        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
                    }

                    BigDecimal newFromBalance = fromAcc.getBalance().subtract(amount);
                    BigDecimal newToBalance = toAcc.getBalance().add(amount);

                    // Actualiza ambos saldos antes de guardar la transacción
                    return accountService.updateAccountBalance(fromAccountNumber, newFromBalance)
                            .then(accountService.updateAccountBalance(toAccountNumber, newToBalance))
                            .then(processTransfer(fromAccountNumber, toAccountNumber, amount, sameBank))
                            .then(Mono.defer(() -> {
                                Transaction transaction = new Transaction();
                                transaction.setFromAccount(fromAccountNumber);
                                transaction.setToAccount(toAccountNumber);
                                transaction.setAmount(amount);
                                transaction.setType("TRANSFER");
                                transaction.setTimestamp(LocalDateTime.now());
                                return saveTransaction(transaction, sameBank);
                            }))
                            .map(saved -> {
                                TransferResponseDTO response = new TransferResponseDTO(
                                        "SUCCESS",
                                        "Transfer completed successfully",
                                        saved
                                );
                                return ResponseEntity.status(HttpStatus.CREATED).body(response);
                            });
                })
                .onErrorResume(e ->
                        Mono.fromSupplier(() -> {
                            TransferResponseDTO errorResponse = new TransferResponseDTO(
                                    "FAILURE",
                                    "Transfer error: " + e.getMessage(),
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

    public Flux<Transaction> getTransactionsByFromAccount(Long fromAccount) {
        return repository.findByFromAccount(fromAccount);
    }

    public Flux<Transaction> getTransactionsByToAccount(Long toAccount) {
        return repository.findByToAccount(toAccount);
    }

    public Flux<Transaction> getTransactionsByAccount(Long accountNumber) {
        return repository.findByFromAccountOrToAccount(accountNumber, accountNumber);
    }
}
