package ru.sber.sber_tech.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sber.sber_tech.domain.Account;
import ru.sber.sber_tech.dto.TransferDto;
import ru.sber.sber_tech.service.TransactionService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.math.BigDecimal;


@Validated
@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private static final String ACCOUNT_ID = "accountId";
    private static final String MUST_POSITIVE_VALUE = "The value must be positive";

    private final TransactionService transactionService;

    @PatchMapping("/depositIntoAccount")
    public ResponseEntity<Account> depositIntoAccount(
            @RequestParam(ACCOUNT_ID) Long accountId,
            @RequestParam("depositAmount") @Min(value = 1, message = MUST_POSITIVE_VALUE) BigDecimal depositAmount
    ) {
        return ResponseEntity.ok(
                transactionService.depositIntoAccount(accountId, depositAmount)
        );
    }

    @PatchMapping("/withdrawFromAccount")
    public ResponseEntity<Account> withdrawFromAccount(
            @RequestParam(ACCOUNT_ID) Long accountId,
            @RequestParam("withdrawAmount") @Min(value = 1, message = MUST_POSITIVE_VALUE) BigDecimal withdrawAmount
    ) {
        return ResponseEntity.ok(
                transactionService.withdrawFromAccount(accountId, withdrawAmount)
        );
    }

    @PatchMapping("/transfer")
    public ResponseEntity<Account> transfer(
            @Valid @RequestBody TransferDto transferDto
    ) {
        return ResponseEntity.ok(
                transactionService.transfer(
                        transferDto
                )
        );
    }
}