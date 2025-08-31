package org.defesasoft.autservice.repository;

import org.defesasoft.autservice.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IUserRepository extends ReactiveCrudRepository<User , Long> {
    // Custom query method to find a user by username
    Mono<User> findByUsername(String username);
}
