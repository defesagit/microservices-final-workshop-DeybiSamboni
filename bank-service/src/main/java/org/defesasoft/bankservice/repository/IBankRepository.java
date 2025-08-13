package org.defesasoft.bankservice.repository;

import org.defesasoft.bankservice.model.Bank;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IBankRepository extends ReactiveCrudRepository<Bank, Long> {
}
