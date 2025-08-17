package org.defesasoft.transactionservice.service;

import org.defesasoft.transactionservice.dto.GetAccountDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AccountService {
    private final WebClient.Builder webClientBuilder;

    @Value("${account-service.url}")
    private String accountServiceUrl;

    public AccountService(final WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<GetAccountDTO> getAccount (Long accountId) {
        return webClientBuilder
                .build()
                .get()
                .uri(accountServiceUrl + "/" + accountId)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        clientResponse -> Mono.error(new RuntimeException("Account does not exist")))
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Error in request to account-service")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Account service error")))
                .bodyToMono(GetAccountDTO.class);
    }

}
