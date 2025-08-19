package org.defesasoft.transactionservice.grpc;

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

    /*@Override
    public void getTransaction(TransactionRequest request, StreamObserver<TransactionResponse> responseObserver) {
        repository.findByToAccount(request.getToAccount())
                .switchIfEmpty(Mono.error(new RuntimeException("Account not found")))
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

    }*/

    @Override
    public void getTransactionsByAccount(TransactionRequest request, StreamObserver<TransactionListResponse> responseObserver) {
        repository.findAllByToAccount(request.getToAccount())
                .collectList()
                .map(transactions -> {
                    TransactionListResponse.Builder listBuilder = TransactionListResponse.newBuilder();
                    for (var transaction : transactions) {
                        TransactionResponse response = TransactionResponse.newBuilder()
                                .setTransactionId(transaction.getTransactionId())
                                .setFromAccount(transaction.getFromAccount())
                                .setToAccount(transaction.getToAccount())
                                .setType(transaction.getType())
                                .setAmount(transaction.getAmount().doubleValue())
                                .setTimestamp(
                                        com.google.protobuf.Timestamp.newBuilder()
                                                .setSeconds(transaction.getTimestamp().toEpochSecond(java.time.ZoneOffset.UTC))
                                                .setNanos(transaction.getTimestamp().getNano())
                                                .build()
                                )
                                .build();
                        listBuilder.addTransactions(response);
                    }
                    return listBuilder.build();
                })
                .subscribe(
                        responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted
                );
    }

}
