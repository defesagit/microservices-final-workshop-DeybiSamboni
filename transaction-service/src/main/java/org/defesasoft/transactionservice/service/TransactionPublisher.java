package org.defesasoft.transactionservice.service;

import org.defesasoft.transactionservice.dto.TransferRequestDTO;
import org.defesasoft.transactionservice.model.Transaction;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionPublisher {
    private final RabbitTemplate rabbitTemplate;

    public TransactionPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishTransactionCreatedEvent(Transaction transaction) {
        rabbitTemplate.convertAndSend("transaction-exchange", "transaction-routing-key", transaction);
        System.out.println("Published transaction created");
    }

}
