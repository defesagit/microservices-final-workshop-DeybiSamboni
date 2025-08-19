package org.defesasoft.transactionservice.repository;

import org.defesasoft.transactionservice.model.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface ITransactionRepository extends ReactiveCrudRepository<Transaction, Long> {
    Flux<Transaction> findByFromAccount(Long fromAccount);
    Flux<Transaction> findByToAccount(Long toAccount);
    Flux<Transaction> findByFromAccountOrToAccount(Long accountNumber1, Long accountNumber2);
    Flux<Transaction> findAllByToAccount(Long toAccount);
}
