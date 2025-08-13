package com.effectivemobile.cardmanagement.util;

import lombok.NoArgsConstructor;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * Утилиты шифрования/маскирования номера карты.
 *
 * @author Дмитрий Ельцов
 **/
@NoArgsConstructor
public class CardNumberUtil {

    public static String encrypt(String cardNumber, String aesKey) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(aesKey.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(cardNumber.getBytes()));
        } catch (Exception ex) {
            throw new RuntimeException("Ошибка шифрования номера карты " + ex);
        }
    }

    public static String mask(String cardNumber) {
        String last4 = cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : cardNumber;
        return "**** **** **** " + last4;
    }
}
