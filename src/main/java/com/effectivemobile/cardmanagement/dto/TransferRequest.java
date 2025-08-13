package com.effectivemobile.cardmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * DTO для запроса перевода между картами.
 * @author Дмитрий Ельцов
 **/
@Schema(description = "Запрос на перевод средств между картами")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferRequest {

    @NotBlank(message = "Номер карты-отправителя обязателен")
    @Schema(description = "Маскированный номер карты-отправителя", example = "**** **** **** 1234")
    private String fromCard;

    @NotBlank(message = "Номер карты-получателя обязателен")
    @Schema(description = "Маскированный номер карты-получателя", example = "**** **** **** 5678")
    private String toCard;

    @DecimalMin(value = "0.01", message = "Сумма перевода должна быть больше 0")
    @Schema(description = "Сумма перевода", example = "250.00")
    private BigDecimal amount;
}
