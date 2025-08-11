package com.effectivemobile.cardmanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * DTO для запроса перевода между картами.
 * @author Дмитрий Ельцов
 **/
@Data
public class TransferRequest {

    @NotBlank(message = "Номер карты-отправителя обязателен")
    private String fromCard;

    @NotBlank(message = "Номер карты-получателя обязателен")
    private String toCard;

    @DecimalMin(value = "0.01", message = "Сумма перевода должна быть больше 0")
    private BigDecimal amount;
}
