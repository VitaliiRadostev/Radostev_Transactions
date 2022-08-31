package ru.sber.sber_tech.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Exception при попытке перевода в рамках одного счета.
 *
 * @author Kiselev_Mikhail
 */
@Getter
@AllArgsConstructor
public class TransferBetweenEqualsAccountException extends RuntimeException {
    private String message;
}
