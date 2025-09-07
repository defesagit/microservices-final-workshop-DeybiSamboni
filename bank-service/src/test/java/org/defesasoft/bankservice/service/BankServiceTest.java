package org.defesasoft.bankservice.service;

import org.defesasoft.bankservice.model.Bank;
import org.defesasoft.bankservice.repository.IBankRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

class BankServiceTest {
    private IBankRepository iBankRepository;
    private BankService bankService;

    public BankServiceTest() {
        iBankRepository = Mockito.mock(IBankRepository.class);
        bankService = Mockito.mock(BankService.class);
    }

    @Test
    void createBank() {
        Bank bank = new Bank();
        bank.setName("BancoTest");

        Mockito.when(iBankRepository.findByName("BancoTest")).thenReturn(Mono.empty());
        Mockito.when(iBankRepository.save(bank)).thenReturn(Mono.just(bank));


    }

}