package org.defesasoft.transactionservice.dto;

import org.defesasoft.transactionservice.model.Transaction;

public class TransferResponseDTO {
    private String status;       // "SUCCESS" o "FAILURE"
    private String message;      // Mensaje descriptivo
    private Transaction transaction; // Detalle de la transacción (si aplica)

    public TransferResponseDTO() {
    }

    public TransferResponseDTO(String status, String message, Transaction transaction) {
        this.status = status;
        this.message = message;
        this.transaction = transaction;
    }

    // Getters y Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
