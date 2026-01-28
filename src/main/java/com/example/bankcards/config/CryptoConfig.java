package com.example.bankcards.config;

import com.example.bankcards.util.AesGcmCrypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CryptoConfig {
    @Bean
    public AesGcmCrypto cardNumberCrypto(@Value("${app.crypto.card-number.secret-base64}") String base64Key) {
        return new AesGcmCrypto(AesGcmCrypto.decodeBase64Key(base64Key));
    }
}

