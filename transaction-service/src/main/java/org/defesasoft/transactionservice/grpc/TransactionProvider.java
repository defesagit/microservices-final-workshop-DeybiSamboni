package org.defesasoft.transactionservice.grpc;

import io.grpc.stub.StreamObserver;
import org.defesasoft.transactionservice.repository.ITransactionRepository;
import org.springframework.grpc.server.service.GrpcService;


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
                    if (transactions.isEmpty()) {
                        throw new RuntimeException("No se encontró historial para la cuenta: " + request.getToAccount());
                    }
                    TransactionListResponse.Builder listBuilder = TransactionListResponse.newBuilder();
                    for (var transaction : transactions) {
                        long fromAccount = transaction.getFromAccount() != null ? transaction.getFromAccount() : 0L;
                        TransactionResponse response = TransactionResponse.newBuilder()
                                .setTransactionId(transaction.getTransactionId())
                                .setFromAccount(fromAccount)
                                .setToAccount(transaction.getToAccount() != null ? transaction.getToAccount() : 0L)
                                .setType(transaction.getType() != null ? transaction.getType() : "")
                                .setAmount(transaction.getAmount() != null ? transaction.getAmount().doubleValue() : 0.0)
                                .setTimestamp(
                                        com.google.protobuf.Timestamp.newBuilder()
                                                .setSeconds(transaction.getTimestamp() != null ? transaction.getTimestamp().toEpochSecond(java.time.ZoneOffset.UTC) : 0)
                                                .setNanos(transaction.getTimestamp() != null ? transaction.getTimestamp().getNano() : 0)
                                                .build()
                                )
                                .build();
                        listBuilder.addTransactions(response);
                    }
                    return listBuilder.build();
                })
                .subscribe(
                        responseObserver::onNext,
                        error -> {
                            responseObserver.onError(
                                    io.grpc.Status.NOT_FOUND
                                            .withDescription(error.getMessage())
                                            .asRuntimeException()
                            );
                        },
                        responseObserver::onCompleted
                );
    }

}
