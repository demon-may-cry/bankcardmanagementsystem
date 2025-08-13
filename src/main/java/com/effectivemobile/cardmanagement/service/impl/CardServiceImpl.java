package com.effectivemobile.cardmanagement.service.impl;

import com.effectivemobile.cardmanagement.dto.CardCreateRequest;
import com.effectivemobile.cardmanagement.dto.CardDto;
import com.effectivemobile.cardmanagement.exception.BusinessException;
import com.effectivemobile.cardmanagement.exception.ErrorCode;
import com.effectivemobile.cardmanagement.mapper.CardMapper;
import com.effectivemobile.cardmanagement.model.entity.Card;
import com.effectivemobile.cardmanagement.model.entity.CardStatus;
import com.effectivemobile.cardmanagement.model.entity.User;
import com.effectivemobile.cardmanagement.repository.CardRepository;
import com.effectivemobile.cardmanagement.repository.UserRepository;
import com.effectivemobile.cardmanagement.security.AuthContext;
import com.effectivemobile.cardmanagement.service.CardService;
import com.effectivemobile.cardmanagement.util.CardNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сервис работы с картами текущего пользователя.
 *
 * @author Дмитрий Ельцов
 **/
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    @Value("${app.crypto.aesKey}")
    private String aesKey;

    /**
     * Создать карту текущему пользователю.
     */
    @Override
    @Transactional
    public CardDto createCard(CardCreateRequest request) {
        User owner = getCurrentUser();

        String encrypted = CardNumberUtil.encrypt(request.getCardNumber(), aesKey);
        if (cardRepository.existsByEncryptedNumber(encrypted)) {
            throw new BusinessException(ErrorCode.VALIDATION_ERROR, "Карта уже существует");
        }

        Card card = new Card();
        card.setEncryptedNumber(encrypted);
        card.setMaskedNumber(CardNumberUtil.mask(request.getCardNumber()));
        card.setCardHolder(request.getCardHolder());
        card.setExpirationDate(LocalDate.now().plusYears(5));
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.ZERO);
        card.setUser(owner);

        cardRepository.save(card);
        return CardMapper.toDto(card);
    }

    /**
     * Получить карты текущего пользователя с пагинацией.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> getMyCards(Pageable pageable) {
        User user = getCurrentUser();
        return cardRepository.findByUser(user, pageable).map(CardMapper::toDto);
    }

    /**
     * Просмотреть карту по id (проверка владения).
     */
    @Override
    @Transactional(readOnly = true)
    public CardDto getMyCard(Long id) {
        Card card = requireMyCard(id);
        return CardMapper.toDto(card);
    }

    /**
     * Удалить свою карту.
     */
    @Override
    @Transactional
    public void deleteMyCard(Long id) {
        Card card = requireMyCard(id);
        cardRepository.delete(card);
    }

    /**
     * ADMIN: смена статуса карты.
     */
    @Override
    @Transactional
    public void setStatusAdmin(Long cardId, CardStatus status) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new BusinessException(ErrorCode.CARD_NOT_FOUND, "Карта не найдена"));
        card.setStatus(status);
        cardRepository.save(card);
    }

    /**
     * ADMIN: найти все карты, с пагинацией.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CardDto> getAllCards(Pageable pageable) {
        return cardRepository.findAll(pageable).map(CardMapper::toDto);
    }

    private User getCurrentUser() {
        String username = AuthContext.currentUsername();
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.VALIDATION_ERROR, "Пользователь не найден"));
    }

    private Card requireMyCard(Long id) {
        User u = getCurrentUser();
        return cardRepository.findById(id)
                .filter(c -> c.getUser().getId().equals(u.getId()))
                .orElseThrow(() -> new BusinessException(ErrorCode.CARD_NOT_FOUND, "Карта не найдена или нет доступа"));
    }
}
