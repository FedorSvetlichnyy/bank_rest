package com.example.bankcards.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Encrypts card number at rest.
 *
 * Important: JPA converter is instantiated by JPA provider, so we access the crypto bean via SpringContext.
 */
@Converter
public class CardNumberCryptoConverter implements AttributeConverter<String, String> {
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return SpringContext.getBean(AesGcmCrypto.class).encryptToBase64(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return SpringContext.getBean(AesGcmCrypto.class).decryptFromBase64(dbData);
    }
}

