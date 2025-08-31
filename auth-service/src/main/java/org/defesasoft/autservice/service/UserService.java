package org.defesasoft.autservice.service;

import org.defesasoft.autservice.dto.RegisterRequest;
import org.defesasoft.autservice.model.User;
import org.defesasoft.autservice.repository.IUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final IUserRepository repository;
    private final PasswordEncoder enconder;

    public UserService(IUserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.enconder = passwordEncoder;
    }

    public Mono<User> register(RegisterRequest request) {
        // Encode the password before saving
        return repository.findByUsername(request.username())
                .switchIfEmpty(repository.save(new User(null, request.username(), enconder.encode(request.password()), request.role())))
                .flatMap(existing -> Mono.error(new RuntimeException("Username already exists")));

    }
}
