package org.defesasoft.transactionservice.dto;

public class GetAccountDTO {
    private Long id;
    private String accountNumber;
    private String holder;
    private Long bankId;

    public GetAccountDTO() {
    }
    public GetAccountDTO(Long id, String accountNumber, String holder, Long bankId) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.holder = holder;
        this.bankId = bankId;
    }


    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getHolder() { return holder; }
    public void setHolder(String holder) { this.holder = holder; }
    public Long getBankId() { return bankId; }
    public void setBankId(Long bankId) { this.bankId = bankId; }
}
