package org.defesasoft.accountservice.service;

import org.defesasoft.accountservice.dto.GetBankDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class BankService {
    private final WebClient.Builder webClientBuilder;

    @Value("${bank-service.url}")
    private String bankServiceUrl;

    public BankService(final WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<GetBankDTO> getBank(Long bankId) {
        return webClientBuilder
                .build()
                .get()
                .uri(bankServiceUrl + "/{bankId}", bankId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Error fetching bank details: " + clientResponse.statusCode())))
                .bodyToMono(GetBankDTO.class);
    }

}
