package com.effectivemobile.cardmanagement.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author Дмитрий Ельцов
 **/
public class CardNumberUtil {

    private static final String SECRET_KEY = "EffectiveMobile";
    private static final String AES = "AES";
    private static final String ERROR_ENCRYPTING_THE_CARD_NUMBER = "Error encrypting the card number: ";
    private static final String STRING = "****";
    private static final String MASK = "**** **** **** ";

    public static String encrypt(String cardNumber) {
        try {
            Cipher cipher = Cipher.getInstance(AES);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), AES);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(cardNumber.getBytes()));
        } catch (Exception ex) {
            throw new RuntimeException(ERROR_ENCRYPTING_THE_CARD_NUMBER + ex);
        }
    }

    public static String mask(String cardNumber) {
        if (cardNumber.length() < 4) return STRING;
        return MASK + cardNumber.substring(cardNumber.length() - 4);
    }
}
