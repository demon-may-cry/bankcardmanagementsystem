package com.effectivemobile.cardmanagement.service.impl;

import com.effectivemobile.cardmanagement.model.entity.Card;
import com.effectivemobile.cardmanagement.model.entity.CardStatus;
import com.effectivemobile.cardmanagement.model.entity.User;
import com.effectivemobile.cardmanagement.repository.CardRepository;
import com.effectivemobile.cardmanagement.service.CardService;
import com.effectivemobile.cardmanagement.util.CardNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Дмитрий Ельцов
 **/
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private static final String CARD_ALREADY_EXISTS = "The card already exists";
    private static final String CARD_NOT_FOUND = "Card not found";

    private final CardRepository cardRepository;

    @Override
    public Card createcard(String rawCardNumber, String holder, User owner) {
        String encrypted = CardNumberUtil.encrypt(rawCardNumber);
        if (cardRepository.existsByEncryptedNumber(encrypted)) {
            throw new IllegalArgumentException(CARD_ALREADY_EXISTS);
        }

        Card card = new Card();
        card.setEncryptedNumber(encrypted);
        card.setMaskedNumber(CardNumberUtil.mask(rawCardNumber));
        card.setCardHolder(holder);
        card.setExpirationDate(LocalDate.now().plusYears(5));
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.ZERO);
        card.setUser(owner);

        return cardRepository.save(card);
    }

    @Override
    public Page<Card> getUserCards(User user, Pageable pageable) {
        return cardRepository.findByUser(user, pageable);
    }

    @Override
    public Card getCardById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(CARD_NOT_FOUND));
    }

    @Override
    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }
}
