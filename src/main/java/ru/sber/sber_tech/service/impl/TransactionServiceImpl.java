package ru.sber.sber_tech.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sber.sber_tech.aop.annotation.LogMethodCall;
import ru.sber.sber_tech.domain.Account;
import ru.sber.sber_tech.dto.TransferDto;
import ru.sber.sber_tech.exception.InsufficientFundsException;
import ru.sber.sber_tech.exception.TransferBetweenEqualsAccountException;
import ru.sber.sber_tech.repository.AccountRepository;
import ru.sber.sber_tech.service.TransactionService;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Objects;


@Service
@Transactional
@LogMethodCall
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;

    @Override
    public Account depositIntoAccount(Long accountId, BigDecimal depositAmount) {
        Account targetAccount = getAccountByIdWithOptimisticLock(accountId);

        return targetAccount.setAmount(
                targetAccount.getAmount().add(depositAmount)
        );
    }

    @Override
    public Account withdrawFromAccount(Long accountId, BigDecimal withdrawAmount) {
        Account targetAccount = getAccountByIdWithOptimisticLock(accountId);

        if (targetAccount.getAmount().compareTo(withdrawAmount) < 0) {
            throw new InsufficientFundsException("Insufficient funds: Check your available balance and try again");
        }

        return targetAccount.setAmount(
                targetAccount.getAmount().subtract(withdrawAmount)
        );
    }

    @Override
    public Account transfer(TransferDto transferDto) {
        if (Objects.equals(transferDto.getSourceAccountId(), transferDto.getTargetAccountId())) {
            throw new TransferBetweenEqualsAccountException("You are trying to transfer to this account");
        }

        Account sourceAccountAfterWithdrawOnTransfer = withdrawFromAccount(
                transferDto.getSourceAccountId(),
                transferDto.getAmount()
        );
        depositIntoAccount(transferDto.getTargetAccountId(), transferDto.getAmount());

        return sourceAccountAfterWithdrawOnTransfer;
    }

    private Account getAccountByIdWithOptimisticLock(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format("Account with id = %s not found", accountId))
                );
    }
}