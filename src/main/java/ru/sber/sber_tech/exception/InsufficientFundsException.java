package ru.sber.sber_tech.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Exception при недостатке средств на счете.
 *
 * @author Kiselev_Mikhail
 */
@Getter
@AllArgsConstructor
public class InsufficientFundsException extends RuntimeException {
    private String message;
}
