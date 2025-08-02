package com.nagp.payment.enums;

public enum PaymentCurrencyEnum {
    INR("INR"),
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    AUD("AUD");

    private String currency;

    PaymentCurrencyEnum(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public static boolean isValidCurrency(String currency) {
        for (PaymentCurrencyEnum paymentCurrency : PaymentCurrencyEnum.values()) {
            if (paymentCurrency.getCurrency().equalsIgnoreCase(currency)) {
                return true;
            }
        }
        return false;
    }
}
