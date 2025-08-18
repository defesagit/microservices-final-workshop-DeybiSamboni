package org.defesasoft.accountservice.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TransactionConsumer {
    @Value("${transaction.grpc.host}")
    private String grpcHost;
    @Value("${transaction.grpc.port}")
    private Integer grpcPort;
    private ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
            .usePlaintext()
            .build();
    private TransactionServiceGrpc.TransactionServiceBlockingStub stub = TransactionServiceGrpc.newBlockingStub(channel);
    TransactionResponse getTransaction(Long transactionId) {
        TransactionRequest request = TransactionRequest.newBuilder()
                .setTransactionId(transactionId)
                .build();
        return stub.getTransaction(request);
    }
}
