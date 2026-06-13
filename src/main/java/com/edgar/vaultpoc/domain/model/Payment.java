package com.edgar.vaultpoc.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Payment(UUID id, String merchantId, BigDecimal amount, String currency,
                      String maskedCard, String encryptedCard, PaymentStatus status,
                      Instant createdAt) {
    public static Payment create(String merchantId, BigDecimal amount, String currency, String pan, String encryptedCard) {
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("amount must be positive");
        return new Payment(UUID.randomUUID(), merchantId, amount, currency, mask(pan), encryptedCard, PaymentStatus.AUTHORIZED, Instant.now());
    }
    private static String mask(String pan) {
        if (pan == null || pan.length() < 10) return "****";
        return pan.substring(0, 6) + "******" + pan.substring(pan.length() - 4);
    }
}
