package com.effectivemobile.cardmanagement.service;

import com.effectivemobile.cardmanagement.model.entity.Card;
import com.effectivemobile.cardmanagement.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Дмитрий Ельцов
 **/
public interface CardService {
    Card createcard(String rawCardNumber, String holder, User owner);

    Page<Card> getUserCards(User user, Pageable pageable);

    Card getCardById(Long id);

    void deleteCard(Long id);
}
