package com.effectivemobile.cardmanagement.service;

import com.effectivemobile.cardmanagement.dto.CardCreateRequest;
import com.effectivemobile.cardmanagement.dto.CardDto;
import com.effectivemobile.cardmanagement.model.entity.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author Дмитрий Ельцов
 **/
public interface CardService {
    CardDto createCard(CardCreateRequest request);

    Page<CardDto> getMyCards(Pageable pageable);

    CardDto getMyCard(Long id);

    void deleteMyCard(Long id);

    void setStatusAdmin(Long cardId, CardStatus status);

    Page<CardDto> getAllCards(Pageable pageable);
}
