package com.effectivemobile.cardmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Логин/пароль + ответ с токеном.
 *
 * @author Дмитрий Ельцов
 **/
public class AuthDto {
    @Getter
    @Setter
    public static class LoginRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class TokenResponse {
        private String token;
    }
}
