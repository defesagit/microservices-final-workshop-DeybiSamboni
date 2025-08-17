package org.defesasoft.transactionservice.service;

import org.defesasoft.transactionservice.dto.GetAccountDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class AccountService {
    private final WebClient.Builder webClientBuilder;

    @Value("${account-service.url}")
    private String accountServiceUrl;

    public AccountService(final WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<GetAccountDTO> getAccount (Long accountNumber) {
        return webClientBuilder
                .build()
                .get()
                .uri(accountServiceUrl + "/" + accountNumber)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        clientResponse -> Mono.error(new RuntimeException("Account does not exist")))
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Error in request to account-service")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Account service error")))
                .bodyToMono(GetAccountDTO.class);
    }

    public Mono<Void> updateAccountBalance(Long accountNumber, BigDecimal newBalance) {
        return webClientBuilder
                .build()
                .patch()
                .uri(accountServiceUrl + "/" + accountNumber + "/balance")
                .bodyValue(Map.of("balance", newBalance))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Error updating balance")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Account service error")))
                .toBodilessEntity()
                .then();
    }

}
