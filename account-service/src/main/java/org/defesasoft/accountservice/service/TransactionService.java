package org.defesasoft.accountservice.service;

import org.defesasoft.accountservice.dto.GetTransactionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class TransactionService {
    private final WebClient.Builder webClientBuilder;

    public TransactionService(final WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Value("${transaction-service.url}")
    private String transactionServiceUrl;

    public Mono<GetTransactionDTO> getTransaction(Long toAccount) {
        return webClientBuilder
                .build()
                .get()
                .uri(transactionServiceUrl + "/to/" + toAccount)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        clientResponse -> Mono.error(new RuntimeException("Transaction does not exist")))
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Error in request transaction-service")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Transaction service error")))
                .bodyToMono(GetTransactionDTO.class);
    }

}
