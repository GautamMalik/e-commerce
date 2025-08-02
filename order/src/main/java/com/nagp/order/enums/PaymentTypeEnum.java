package com.nagp.order.enums;

public enum PaymentTypeEnum {
    CREDIT_CARD("CREDIT_CARD"),
    DEBIT_CARD("DEBIT_CARD"),
    NET_BANKING("NET_BANKING"),
    UPI("UPI"),
    WALLET("WALLET");

    private String type;

    PaymentTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static boolean isValidType(String type) {
        for (PaymentTypeEnum paymentType : PaymentTypeEnum.values()) {
            if (paymentType.getType().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }
}
