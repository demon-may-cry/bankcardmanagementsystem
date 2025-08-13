package com.effectivemobile.cardmanagement.service.impl;

import com.effectivemobile.cardmanagement.dto.TransferRequest;
import com.effectivemobile.cardmanagement.exception.BusinessException;
import com.effectivemobile.cardmanagement.exception.ErrorCode;
import com.effectivemobile.cardmanagement.model.entity.Card;
import com.effectivemobile.cardmanagement.model.entity.CardStatus;
import com.effectivemobile.cardmanagement.model.entity.Transfer;
import com.effectivemobile.cardmanagement.model.entity.User;
import com.effectivemobile.cardmanagement.repository.CardRepository;
import com.effectivemobile.cardmanagement.repository.TransferRepository;
import com.effectivemobile.cardmanagement.repository.UserRepository;
import com.effectivemobile.cardmanagement.security.AuthContext;
import com.effectivemobile.cardmanagement.service.TransferService;
import com.effectivemobile.cardmanagement.util.CardNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Сервис переводов между СВОИМИ картами текущего пользователя.
 *
 * @author Дмитрий Ельцов
 **/
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final CardRepository cardRepository;
    private final TransferRepository transferRepository;
    private final UserRepository userRepository;

    @Value("${app.crypto.aesKey}")
    private String aesKey;

    /**
     * Перевод между своими картами по маске или сырому номеру.
     */
    @Override
    @Transactional
    public void transfer(TransferRequest request) {
        if (request.getFromCard().equals(request.getToCard())) {
            throw new BusinessException(ErrorCode.SAME_CARD_TRANSFER_NOT_ALLOWED, "Нельзя переводить на ту же карту");
        }

        User owner = getCurrentUser();

        Card from = findOwnedCard(owner, request.getFromCard());
        Card to = findOwnedCard(owner, request.getToCard());

        requireActiveAndNotExpired(from, "отправителя");
        requireActiveAndNotExpired(to, "получателя");

        BigDecimal amount = request.getAmount();
        if (from.getBalance().compareTo(amount) < 0) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_FUNDS, "Недостаточно средств");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        cardRepository.save(from);
        cardRepository.save(to);

        Transfer t = new Transfer();
        t.setFromCard(from);
        t.setToCard(to);
        t.setAmount(amount);
        transferRepository.save(t);
    }

    /**
     * Находим карту текущего пользователя по маске или сырому номеру.
     */
    @Override
    public Card findOwnedCard(User owner, String maskedOrRaw) {
        String encrypted = CardNumberUtil.encrypt(maskedOrRaw, aesKey);
        return cardRepository.findAll().stream()
                .filter(c -> c.getUser().getId().equals(owner.getId()))
                .filter(c -> c.getMaskedNumber().equals(maskedOrRaw) || c.getEncryptedNumber().equals(encrypted))
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.CARD_NOT_FOUND, "Карта не найдена"));
    }

    private User getCurrentUser() {
        String username = AuthContext.currentUsername();
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.VALIDATION_ERROR, "Пользователь не найден"));
    }

    private void requireActiveAndNotExpired(Card c, String who) {
        if (c.getStatus() != CardStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.CARD_BLOCKED, "Карта " + who + " не активна");
        }
        if (c.getExpirationDate().isBefore(LocalDate.now())) {
            throw new BusinessException(ErrorCode.CARD_EXPIRED, "Срок карты " + who + " истек");
        }
    }
}
