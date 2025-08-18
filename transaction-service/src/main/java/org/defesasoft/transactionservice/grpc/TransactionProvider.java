package org.defesasoft.transactionservice.grpc;

import org.defesasoft.transactionservice.grpc.TransactionRequest;
import org.defesasoft.transactionservice.grpc.TransactionResponse;
import org.defesasoft.transactionservice.grpc.TransactionServiceGrpc;
import com.google.protobuf.Timestamp;
import java.time.ZoneOffset;
import io.grpc.stub.StreamObserver;
import org.defesasoft.transactionservice.repository.ITransactionRepository;
import org.springframework.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;


@GrpcService
public class TransactionProvider extends TransactionServiceGrpc.TransactionServiceImplBase {
    private final ITransactionRepository repository;

    public TransactionProvider(ITransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getTransaction(TransactionRequest request, StreamObserver<TransactionResponse> responseObserver) {
        repository.findById(request.getTransactionId())
                .switchIfEmpty(Mono.error(new RuntimeException("Transaction not found")))
                .subscribe(transaction -> {

                    double amount = transaction.getAmount().doubleValue();


                    Timestamp timestamp = Timestamp.newBuilder()
                            .setSeconds(transaction.getTimestamp().toEpochSecond(ZoneOffset.UTC))
                            .setNanos(transaction.getTimestamp().getNano())
                            .build();

                    TransactionResponse response = TransactionResponse
                            .newBuilder()
                            .setTransactionId(transaction.getTransactionId())
                            .setFromAccount(transaction.getFromAccount())
                            .setToAccount(transaction.getToAccount())
                            .setType(transaction.getType())
                            .setAmount(amount)
                            .setTimestamp(timestamp)
                            .build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }, responseObserver::onError).dispose();

    }
}
