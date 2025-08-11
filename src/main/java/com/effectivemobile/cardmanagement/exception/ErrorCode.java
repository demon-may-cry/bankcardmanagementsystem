package com.effectivemobile.cardmanagement.exception;

/**
 * Коды ошибок для передачи на фронтенд
 * @author Дмитрий Ельцов
 **/
public enum ErrorCode {
    CARD_NOT_FOUND,
    INSUFFICIENT_FUNDS,
    CARD_BLOCKED,
    CARD_EXPIRED,
    SAME_CARD_TRANSFER_NOT_ALLOWED,
    VALIDATION_ERROR
}
