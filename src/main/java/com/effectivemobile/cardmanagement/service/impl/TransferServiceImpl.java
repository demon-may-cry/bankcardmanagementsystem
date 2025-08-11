package com.effectivemobile.cardmanagement.service.impl;

import com.effectivemobile.cardmanagement.dto.TransferRequest;
import com.effectivemobile.cardmanagement.exception.BusinessException;
import com.effectivemobile.cardmanagement.exception.ErrorCode;
import com.effectivemobile.cardmanagement.model.entity.Card;
import com.effectivemobile.cardmanagement.model.entity.CardStatus;
import com.effectivemobile.cardmanagement.model.entity.Transfer;
import com.effectivemobile.cardmanagement.repository.CardRepository;
import com.effectivemobile.cardmanagement.repository.TransferRepository;
import com.effectivemobile.cardmanagement.service.TransferService;
import com.effectivemobile.cardmanagement.util.CardNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * @author Дмитрий Ельцов
 **/
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;


    @Override
    @Transactional
    public void transfer(TransferRequest request) {
        if (request.getFromCard().equals(request.getToCard())) {
            throw new BusinessException(ErrorCode.SAME_CARD_TRANSFER_NOT_ALLOWED,
                    "Нельзя переводить на ту же карту");
        }

        Card fromCard = findCardByMaskedOrRaw(request.getFromCard());
        Card toCard = findCardByMaskedOrRaw(request.getToCard());

        // Проверка статуса
        if (fromCard.getStatus() != CardStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.CARD_BLOCKED,
                    "Карта отправителя заблокирована или недоступна");
        }

        if (toCard.getStatus() != CardStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.CARD_BLOCKED,
                    "Карта получателя заблокирована или недоступна");
        }

        // Проверка срока действия
        if (fromCard.getExpirationDate().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.CARD_EXPIRED,
                    "Срок действия карты отправителя истек");
        }

        if (toCard.getExpirationDate().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.CARD_EXPIRED,
                    "Срок действия карты получателя истек");
        }

        // Проверка баланса
        if (fromCard.getBalance().compareTo(request.getAmount()) < 0) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_FUNDS,
                    "Недостаточно средств");
        }

        // Списание и зачисление
        fromCard.setBalance(fromCard.getBalance().subtract(request.getAmount()));
        toCard.setBalance(toCard.getBalance().add(request.getAmount()));

        // Сохранение изменений
        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        // Лог транзакции
        Transfer transfer = new Transfer();
        transfer.setFromCard(fromCard);
        transfer.setToCard(toCard);
        transfer.setAmount(request.getAmount());
        transferRepository.save(transfer);
    }

    @Override
    public Card findCardByMaskedOrRaw(String number) {
        String encrypted = CardNumberUtil.encrypt(number);
        return cardRepository.findAll().stream()
                .filter(card -> card.getEncryptedNumber().equals(encrypted)
                || card.getMaskedNumber().equals(number))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.CARD_NOT_FOUND,
                        "Карта не найдена"));
    }
}
