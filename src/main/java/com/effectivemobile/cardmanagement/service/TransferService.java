package com.effectivemobile.cardmanagement.service;

import com.effectivemobile.cardmanagement.dto.TransferRequest;
import com.effectivemobile.cardmanagement.model.entity.Card;

/**
 * Сервис для перевода средств между картами пользователя.
 * @author Дмитрий Ельцов
 **/
public interface TransferService {

    /**
     * Перевод между своими картами с проверками
     */
    void transfer(TransferRequest request);

    /**
     * Поиск карты по номеру (сырым или маске) с расшифровкой
     */
    Card findCardByMaskedOrRaw(String number);
}
