package com.example.bankcards.util;

public final class CardMasker {
    private CardMasker() {
    }

    public static String mask(String plainCardNumber) {
        if (plainCardNumber == null) return null;
        String digits = plainCardNumber.replaceAll("\\s+", "");
        if (digits.length() < 4) return "****";
        String last4 = digits.substring(digits.length() - 4);
        return "**** **** **** " + last4;
    }

    public static String last4(String plainCardNumber) {
        if (plainCardNumber == null) return null;
        String digits = plainCardNumber.replaceAll("\\s+", "");
        if (digits.length() < 4) return null;
        return digits.substring(digits.length() - 4);
    }
}

