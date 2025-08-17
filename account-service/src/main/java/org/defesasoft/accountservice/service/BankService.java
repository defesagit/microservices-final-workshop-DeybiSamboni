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

    /*public Mono<GetBankDTO> getBank(Long bankId) {
        return webClientBuilder
                .build()
                .get()
                .uri(bankServiceUrl + "/" + bankId)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Bank not Found")))
                .bodyToMono(GetBankDTO.class);
    }*/

    public Mono<GetBankDTO> getBank(Long bankId) {
        return webClientBuilder
                .build()
                .get()
                .uri(bankServiceUrl + "/" + bankId)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                        clientResponse -> Mono.error(new RuntimeException("Bank does not exist")))
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Error en la petición al bank-service")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Bank service error")))
                .bodyToMono(GetBankDTO.class);
    }

}
