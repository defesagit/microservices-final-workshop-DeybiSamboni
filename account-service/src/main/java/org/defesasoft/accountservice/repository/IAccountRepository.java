package org.defesasoft.accountservice.repository;

import org.defesasoft.accountservice.model.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IAccountRepository extends ReactiveCrudRepository<Account, Long> {
    Mono<Boolean> existsByAccountNumber(String accountNumber);
    Mono<Account> findByAccountNumber(String accountNumber);
}
