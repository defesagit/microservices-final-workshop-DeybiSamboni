package org.defesasoft.accountservice.repository;

import org.defesasoft.accountservice.model.Account;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface IAccountRepository extends ReactiveCrudRepository<Account, Long> {
}
