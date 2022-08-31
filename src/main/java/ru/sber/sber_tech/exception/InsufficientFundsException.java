package ru.sber.sber_tech.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class InsufficientFundsException extends RuntimeException {
    private String message;
}
