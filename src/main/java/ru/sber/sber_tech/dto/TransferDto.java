package ru.sber.sber_tech.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@Accessors(chain = true)
public class TransferDto {

    @Min(value = 1L, message = "The value must be positive")
    private BigDecimal amount;

    private Long sourceAccountId;

    private Long targetAccountId;
}
