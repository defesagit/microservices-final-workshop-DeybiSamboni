package org.defesasoft.accountservice.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.defesasoft.accountservice.dto.GetTransactionDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TransactionConsumer {
    @Value("${transaction.grpc.host}")
    private String grpcHost;
    @Value("${transaction.grpc.port}")
    private Integer grpcPort;
    private ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9091)
            .usePlaintext()
            .build();
    private TransactionServiceGrpc.TransactionServiceBlockingStub stub = TransactionServiceGrpc.newBlockingStub(channel);

    /*public Mono<GetTransactionDTO> getTransaction(Long toAccount) {
        try {
            TransactionRequest request = TransactionRequest.newBuilder().setToAccount(toAccount).build();
            TransactionResponse response = stub.getTransaction(request);
            GetTransactionDTO dto = new GetTransactionDTO(
                    response.getTransactionId(),
                    response.getFromAccount(),
                    response.getToAccount(),
                    response.getType(),
                    BigDecimal.valueOf(response.getAmount()),
                    LocalDateTime.ofEpochSecond(response.getTimestamp().getSeconds(), response.getTimestamp().getNanos(), java.time.ZoneOffset.UTC)
            );
            return Mono.just(dto);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("No se encontró transacción para la cuenta: " + toAccount + ". Error: " + e.getMessage()));
        }
    }*/
    public Mono<List<GetTransactionDTO>> getTransactions(Long toAccount) {
        try {
            TransactionRequest request = TransactionRequest.newBuilder().setToAccount(toAccount).build();
            TransactionListResponse response = stub.getTransactionsByAccount(request);
            List<GetTransactionDTO> dtos = response.getTransactionsList().stream()
                    .map(tr -> new GetTransactionDTO(
                            tr.getTransactionId(),
                            tr.getFromAccount(),
                            tr.getToAccount(),
                            tr.getType(),
                            BigDecimal.valueOf(tr.getAmount()),
                            LocalDateTime.ofEpochSecond(tr.getTimestamp().getSeconds(), tr.getTimestamp().getNanos(), java.time.ZoneOffset.UTC)
                    ))
                    .collect(Collectors.toList());
            return Mono.just(dtos);
        } catch (Exception e) {
            return Mono.error(new RuntimeException("No se encontró historial para la cuenta: " + toAccount + ". Error: " + e.getMessage()));
        }
    }
}