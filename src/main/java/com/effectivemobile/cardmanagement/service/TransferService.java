package com.effectivemobile.cardmanagement.service;

import com.effectivemobile.cardmanagement.dto.TransferRequest;
import com.effectivemobile.cardmanagement.model.entity.Card;
import com.effectivemobile.cardmanagement.model.entity.User;

/**
 * @author Дмитрий Ельцов
 **/
public interface TransferService {

    void transfer(TransferRequest request);

    Card findOwnedCard(User owner, String maskedOrRaw);
}
