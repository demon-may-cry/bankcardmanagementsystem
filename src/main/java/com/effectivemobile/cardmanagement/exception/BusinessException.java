package com.effectivemobile.cardmanagement.exception;

import lombok.Getter;

/**
 * Бизнес-исключение с кодом ошибки и сообщением
 * @author Дмитрий Ельцов
 **/
@Getter
public class BusinessException extends RuntimeException{

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
