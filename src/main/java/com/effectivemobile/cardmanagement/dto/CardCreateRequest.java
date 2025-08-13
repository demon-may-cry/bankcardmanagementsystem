package com.effectivemobile.cardmanagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Запрос на создание карты.
 *
 * @author Дмитрий Ельцов
 **/
@Getter
@Setter
public class CardCreateRequest {

    @NotBlank
    @Schema(description = "Сырой номер карты", example = "4111111111111111")
    private String cardNumber;

    @NotBlank
    @Schema(description = "Имя владельца", example = "IVAN IVANOV")
    private String cardHolder;
}
