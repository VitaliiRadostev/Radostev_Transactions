package ru.sber.sber_tech.config.advisor;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleStateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.sber.sber_tech.exception.InsufficientFundsException;
import ru.sber.sber_tech.exception.TransferBetweenEqualsAccountException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * TransactionControllerAdvisor.
 *
 * @author Kiselev_Mikhail
 */
@Slf4j
@RestControllerAdvice("ru.sber.sber_tech.controller")
public class TransactionControllerAdvisor {

    private static final String RETRY_OPERATION_MESSAGE = "An error has occurred. Retry the operation";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientFundsException.class)
    public ErrorInfo handleInsufficientFundsException(final InsufficientFundsException ex, final HttpServletRequest request) {
        log.error(ex.getMessage(), ex);
        return new ErrorInfo(HttpStatus.BAD_REQUEST, ex, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(StaleStateException.class)
    public ErrorInfo handleStaleStateException(final StaleStateException ex, final HttpServletRequest request) {
        log.error(RETRY_OPERATION_MESSAGE, ex);
        return new ErrorInfo(HttpStatus.BAD_REQUEST, ex, request.getRequestURI(), RETRY_OPERATION_MESSAGE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorInfo handleEntityNotFoundException(final EntityNotFoundException ex, final HttpServletRequest request) {
        log.error(ex.getMessage(), ex);
        return new ErrorInfo(HttpStatus.BAD_REQUEST, ex, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(TransferBetweenEqualsAccountException.class)
    public ErrorInfo handleTransferBetweenEqualsAccountException(final TransferBetweenEqualsAccountException ex, final HttpServletRequest request) {
        log.error(ex.getMessage(), ex);
        return new ErrorInfo(HttpStatus.BAD_REQUEST, ex, request.getRequestURI());
    }

    @Data
    @Accessors(chain = true)
    static class ErrorInfo {
        @JsonFormat(
                pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
        )
        private Date timestamp;
        private int status;
        private String error;
        private String exception;
        private String message;
        private String path;

        ErrorInfo(HttpStatus status, Exception exception, String path) {
            errorInfoTemplate(status, exception, path)
                    .setMessage(exception.getMessage());
        }

        ErrorInfo(HttpStatus status, Exception exception, String path, String message) {
            errorInfoTemplate(status, exception, path)
                    .setMessage(message);
        }

        private ErrorInfo errorInfoTemplate(HttpStatus status, Exception exception, String path) {
            this
                    .setTimestamp(new Date())
                    .setException(exception.getClass().getName())
                    .setPath(path);

            if (status != null) {
                this.setStatus(status.value())
                        .setError(status.getReasonPhrase());
            }

            return this;
        }
    }
}