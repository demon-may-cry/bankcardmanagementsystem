package com.effectivemobile.cardmanagement.mapper;

import com.effectivemobile.cardmanagement.dto.CardDto;
import com.effectivemobile.cardmanagement.model.entity.Card;

/**
 * Простая ручная маппа между Entity и DTO.
 *
 * @author Дмитрий Ельцов
 **/
public class CardMapper {

    public static CardDto toDto(Card card) {
        return new CardDto(
                card.getId(),
                card.getMaskedNumber(),
                card.getExpirationDate(),
                card.getStatus().name(),
                card.getBalance()
        );
    }
}
