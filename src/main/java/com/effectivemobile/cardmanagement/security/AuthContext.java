package com.effectivemobile.cardmanagement.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Помощник для получения текущего пользователя.
 *
 * @author Дмитрий Ельцов
 **/
public class AuthContext {

    public static String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null ? null : auth.getName();
    }
}
