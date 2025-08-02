package com.nagp.payment.dto;

import com.nagp.payment.constants.Constants;
import com.nagp.payment.enums.PaymentCurrencyEnum;
import com.nagp.payment.enums.PaymentTypeEnum;
import com.nagp.payment.exceptions.BadInputException;

import java.util.Map;

public class PaymentFactory {

    public static Payment createPayment(PaymentTypeEnum type, Map<String, String> data) {
        switch (type) {
            case UPI -> {
                UpiPaymnet upi = new UpiPaymnet();
                upi.setAmount(data.get("amount"));
                upi.setDate(data.get("date"));
                upi.setUpiId(data.get("upiId"));
                upi.setUpiPin(data.get("upiPin"));
                upi.setPaymentCurrency(PaymentCurrencyEnum.valueOf(data.get("paymentCurrency").toUpperCase()));
                return upi;
            }
            case WALLET -> {
                WalletPayment wallet = new WalletPayment();
                wallet.setAmount(data.get("amount"));
                wallet.setDate(data.get("date"));
                wallet.setWalletId(data.get("walletId"));
                wallet.setWalletProvider(data.get("walletProvider"));
                wallet.setPaymentCurrency(PaymentCurrencyEnum.valueOf(data.get("paymentCurrency").toUpperCase()));
                return wallet;
            }
            case CREDIT_CARD, DEBIT_CARD -> {
                CreditAndDebitCardPayment card = new CreditAndDebitCardPayment();
                card.setAmount(data.get("amount"));
                card.setDate(data.get("date"));
                card.setCardNumber(data.get("cardNumber"));
                card.setCardHolderName(data.get("cardHolderName"));
                card.setCvv(data.get("cvv"));
                card.setExpiryDate(data.get("expiryDate"));
                card.setPaymentCurrency(PaymentCurrencyEnum.valueOf(data.get("paymentCurrency").toUpperCase()));
                return card;
            }
            case NET_BANKING -> {
                NetBankingPayment netBanking = new NetBankingPayment();
                netBanking.setAmount(data.get("amount"));
                netBanking.setDate(data.get("date"));
                netBanking.setBankName(data.get("bankName"));
                netBanking.setAccountNumber(data.get("accountNumber"));
                netBanking.setIfscCode(data.get("ifscCode"));
                netBanking.setPaymentCurrency(PaymentCurrencyEnum.valueOf(data.get("paymentCurrency").toUpperCase()));
                return netBanking;
            }
            default -> throw new BadInputException(Constants.INVALID_PAYMENT_TYPE, "Unsupported payment type: " + type);
        }
    }
}
