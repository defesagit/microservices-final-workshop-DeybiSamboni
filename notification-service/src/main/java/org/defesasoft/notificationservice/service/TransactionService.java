package org.defesasoft.notificationservice.service;

import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    public void sendTransactionNotification(String message) {
        // Logic to send notification (e.g., email, SMS)
        System.out.println("Notification sent: " + message);
    }


}
