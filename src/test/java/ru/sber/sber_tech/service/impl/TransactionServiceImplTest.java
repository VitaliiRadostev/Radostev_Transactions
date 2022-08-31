package ru.sber.sber_tech.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import ru.sber.sber_tech.domain.Account;
import ru.sber.sber_tech.dto.TransferDto;
import ru.sber.sber_tech.exception.InsufficientFundsException;
import ru.sber.sber_tech.exception.TransferBetweenEqualsAccountException;
import ru.sber.sber_tech.repository.AccountRepository;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    private static final Long ID_EXISTS_ACCOUNT_1 = 1L;
    private static final Long ID_EXISTS_ACCOUNT_2 = 2L;
    private static final Long ID_NOT_EXISTS_ACCOUNT = 0L;
    private static final BigDecimal INIT_TEST_AMOUNT = BigDecimal.valueOf(30_000);

    @Spy
    @InjectMocks
    TransactionServiceImpl transactionService;
    @Mock
    AccountRepository accountRepository;

    @Before
    public void setUp() {
        when(accountRepository.findById(ID_EXISTS_ACCOUNT_1))
                .thenReturn(Optional.of(new Account(ID_EXISTS_ACCOUNT_1, INIT_TEST_AMOUNT)));
        when(accountRepository.findById(ID_EXISTS_ACCOUNT_2))
                .thenReturn(Optional.of(new Account(ID_EXISTS_ACCOUNT_2, INIT_TEST_AMOUNT)));
        when(accountRepository.findById(ID_NOT_EXISTS_ACCOUNT))
                .thenReturn(Optional.empty());
    }

    @Test
    public void depositIntoAccount_whenAccountExists_whenSuccessComplete() {
        BigDecimal amountForDeposit = BigDecimal.valueOf(3_000);
        Account targetAccountAfterDeposit = transactionService.depositIntoAccount(ID_EXISTS_ACCOUNT_1, amountForDeposit);
        assertEquals(INIT_TEST_AMOUNT.add(amountForDeposit), targetAccountAfterDeposit.getAmount());
    }

    @Test(expected = EntityNotFoundException.class)
    public void depositIntoAccount_whenAccountNotExists_whenThrowEntityNotFoundException() {
        BigDecimal amountForDeposit = BigDecimal.valueOf(3_000);
        transactionService.depositIntoAccount(ID_NOT_EXISTS_ACCOUNT, amountForDeposit);
    }

    @Test(expected = InsufficientFundsException.class)
    public void withdrawFromAccount_whenInsufficientFundsOnAccount_whenThrowInsufficientFundsException() {
        BigDecimal amountForWithdraw = BigDecimal.valueOf(40_000);
        transactionService.withdrawFromAccount(ID_EXISTS_ACCOUNT_1, amountForWithdraw);
    }

    @Test
    public void withdrawFromAccount_whenSufficientFundsOnAccount_whenSuccessComplete() {
        BigDecimal amountForWithdraw = BigDecimal.valueOf(25_000);
        Account targetAccountAfterWithdraw = transactionService.withdrawFromAccount(ID_EXISTS_ACCOUNT_1, amountForWithdraw);
        assertEquals(INIT_TEST_AMOUNT.subtract(amountForWithdraw), targetAccountAfterWithdraw.getAmount());
    }

    @Test(expected = TransferBetweenEqualsAccountException.class)
    public void transfer_whenTransferBetweenEqualsAccount_whenThrowTransferBetweenEqualsAccountException() {
        BigDecimal amountForTransfer = BigDecimal.valueOf(25_000L);
        transactionService.transfer(
                new TransferDto(
                        amountForTransfer,
                        ID_EXISTS_ACCOUNT_1,
                        ID_EXISTS_ACCOUNT_1
                )
        );
    }

    @Test
    public void transfer_whenSuccessPath_whenComplete() {
        BigDecimal amountForTransfer = BigDecimal.valueOf(25_000);
        Account sourceAccountAfterTransfer = transactionService.transfer(
                new TransferDto(
                        amountForTransfer,
                        ID_EXISTS_ACCOUNT_1,
                        ID_EXISTS_ACCOUNT_2
                )
        );

        assertEquals(INIT_TEST_AMOUNT.subtract(amountForTransfer), sourceAccountAfterTransfer.getAmount());
        verify(transactionService, times(1))
                .depositIntoAccount(ID_EXISTS_ACCOUNT_2, amountForTransfer);
    }
}