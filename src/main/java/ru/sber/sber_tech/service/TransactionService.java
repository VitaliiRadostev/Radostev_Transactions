package ru.sber.sber_tech.service;

import ru.sber.sber_tech.domain.Account;
import ru.sber.sber_tech.dto.TransferDto;

import java.math.BigDecimal;

/**
 * TransactionService.
 *
 * @author Kiselev_Mikhail
 */
public interface TransactionService {

    Account depositIntoAccount(Long accountId, BigDecimal depositAmount);

    Account withdrawFromAccount(Long accountId, BigDecimal withdrawAmount);

    Account transfer(TransferDto transferDto);
}
