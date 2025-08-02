package com.nagp.payment.delegate;

import com.nagp.payment.constants.Constants;
import com.nagp.payment.dto.PaymentRequestDTO;
import com.nagp.payment.dto.PaymentResponseDTO;
import com.nagp.payment.enums.PaymentCurrencyEnum;
import com.nagp.payment.enums.PaymentTypeEnum;
import com.nagp.payment.exceptions.BadInputException;
import com.nagp.payment.service.PaymentService;
import com.nagp.payment.utils.AuthorizationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentDelegate {

    private final AuthorizationUtils authorizationUtils;
    private final PaymentService paymentService;

    public PaymentResponseDTO processPayment(PaymentRequestDTO paymentDetails, String token) {

        log.info("Inside PaymentDelegate :: processPayment() method with paymentDetails: {}", paymentDetails);
        isAuthorizedUser(token);
        if(paymentDetails.getAmount() <= 0) {
            log.error("Invalid payment amount: {}", paymentDetails.getAmount());
            throw new BadInputException(Constants.INVALID_AMOUNT,"Invalid payment amount: " + paymentDetails.getAmount());
        } else if (StringUtils.isEmpty(paymentDetails.getMethod()) || !PaymentTypeEnum.isValidType(paymentDetails.getMethod().toUpperCase())) {
            log.error("Invalid payment type: {}", paymentDetails.getMethod());
            throw new BadInputException(Constants.INVALID_PAYMENT_TYPE,"Invalid payment type: " + paymentDetails.getMethod().toUpperCase());
        } else if(StringUtils.isEmpty(paymentDetails.getPaymentCurrency()) || !PaymentCurrencyEnum.isValidCurrency(paymentDetails.getPaymentCurrency())){
            log.error("Invalid payment currency: {}", paymentDetails.getPaymentCurrency());
            throw new BadInputException(Constants.INVALID_PAYMENT_CURRENCY,"Invalid payment currency: " + paymentDetails.getPaymentCurrency());
        }

        isAllMandatoryParametresPresent(paymentDetails);

        PaymentResponseDTO response = paymentService.processPayment(paymentDetails);
        response.setUserId(extractUserIdFromToken(token));
        log.info("PaymentDelegate::processPayment ends successfully");
        return response;
    }

    private void isAllMandatoryParametresPresent(PaymentRequestDTO paymentDetails) {
        if(paymentDetails.getMethod().equalsIgnoreCase(PaymentTypeEnum.UPI.getType())){
            if(StringUtils.isEmpty(paymentDetails.getUpiId()) || StringUtils.isEmpty(paymentDetails.getUpiPin())) {
                log.error("UPI ID or UPI PIN is empty");
                throw new BadInputException(Constants.INVALID_UPI_ID,"UPI ID or UPI PIN is empty");
            }
        } else if(paymentDetails.getMethod().equalsIgnoreCase(PaymentTypeEnum.WALLET.getType())) {
            if(StringUtils.isEmpty(paymentDetails.getWalletId()) || StringUtils.isEmpty(paymentDetails.getWalletProvider())) {
                log.error("Wallet ID or Wallet Provider is empty");
                throw new BadInputException(Constants.INVALID_WALLET_ID,"Wallet ID or Wallet Provider is empty");
            }
        } else if(paymentDetails.getMethod().equalsIgnoreCase(PaymentTypeEnum.CREDIT_CARD.getType()) || paymentDetails.getMethod().equalsIgnoreCase(PaymentTypeEnum.DEBIT_CARD.getType())) {
            if(StringUtils.isEmpty(paymentDetails.getCardNumber()) || StringUtils.isEmpty(paymentDetails.getCardHolderName()) || StringUtils.isEmpty(paymentDetails.getCvv()) || Objects.isNull(paymentDetails.getExpiryDate())) {
                log.error("Card details are incomplete");
                throw new BadInputException(Constants.INVALID_CARD_DETAILS,"Card details are incomplete [cardNumber, cardHolderName, cvv, expiryDate]");
            }
        } else if(paymentDetails.getMethod().equalsIgnoreCase(PaymentTypeEnum.NET_BANKING.getType())) {
            if(StringUtils.isEmpty(paymentDetails.getBankName()) || StringUtils.isEmpty(paymentDetails.getAccountNumber()) || StringUtils.isEmpty(paymentDetails.getIfscCode())) {
                log.error("Bank details are incomplete");
                throw new BadInputException(Constants.INVALID_BANK_DETAILS,"Bank details are incomplete [bankName, accountNumber, ifscCode]");
            }
        }
    }

    private void isAuthorizedUser(String token) {
        if(StringUtils.isEmpty(token)) {
            log.error("Token is empty");
            throw new BadInputException(Constants.INVALID_TOKEN, "Token is empty");
        }
        authorizationUtils.isValidSession(token);
    }

    private int extractUserIdFromToken(String token) {
        return Integer.parseInt(token.split(Constants.DELIMITER)[0]);
    }
}
