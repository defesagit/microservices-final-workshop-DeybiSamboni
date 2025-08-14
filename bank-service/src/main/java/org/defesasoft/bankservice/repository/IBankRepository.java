package org.defesasoft.bankservice.repository;

import org.defesasoft.bankservice.model.Bank;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IBankRepository extends ReactiveCrudRepository<Bank, Long> {
    Mono<Bank> findByName(String name);
}
