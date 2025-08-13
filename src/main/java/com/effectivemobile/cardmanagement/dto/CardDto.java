package com.effectivemobile.cardmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO для передачи данных карты между слоями приложения.
 * Используется в API вместо самой Entity, чтобы скрыть внутреннюю реализацию.
 * @author Дмитрий Ельцов
 **/
@Schema(description = "Данные банковской карты")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardDto {

    @Schema(description = "Идентификатор карты", example = "1")
    private Long id;

    @Schema(description = "Маскированный номер карты", example = "**** **** **** 1234")
    private String maskedNumber;

    @NotNull
    @Schema(description = "Дата окончания действия карты", example = "2027-12-31")
    private LocalDate expirationDate;

    @NotNull
    @Schema(description = "Статус карты", example = "ACTIVE")
    private String status;

    @NotNull
    @DecimalMin(value = "0.0")
    @Schema(description = "Баланс карты", example = "1500.75")
    private BigDecimal balance;
}
