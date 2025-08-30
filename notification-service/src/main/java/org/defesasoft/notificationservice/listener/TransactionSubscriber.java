package org.defesasoft.notificationservice.listener;

import org.defesasoft.notificationservice.model.Transaction;
import org.defesasoft.notificationservice.service.TransactionService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionSubscriber {
    private final TransactionService transactionService;

    public TransactionSubscriber(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    @RabbitListener(queues = "transaction-queue")
    public void recibeTransactionMessage(Transaction transaction) {
        System.out.println("Received transaction message: " + transaction);
        transactionService.sendTransactionNotification("Confirmacion de transferencia" + " Se ha realizado una transferencia de " + transaction.getAmount() + " a la cuenta " + transaction.getToAccount());
    }

}
