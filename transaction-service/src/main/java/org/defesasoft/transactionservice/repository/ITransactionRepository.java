package org.defesasoft.transactionservice.repository;

import org.defesasoft.transactionservice.model.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ITransactionRepository extends ReactiveCrudRepository<Transaction, Long> {
}
