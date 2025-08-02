package com.nagp.order.delegate;

import com.nagp.order.contstants.Constants;
import com.nagp.order.dto.*;
import com.nagp.order.enums.PaymentCurrencyEnum;
import com.nagp.order.enums.PaymentTypeEnum;
import com.nagp.order.exceptions.BadInputException;
import com.nagp.order.service.NotificationService;
import com.nagp.order.service.OrderService;
import com.nagp.order.utils.AuthorizationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderDelegate {

    private final OrderService orderService;
    private final AuthorizationUtils authorizationUtils;
    private final NotificationService notificationService;


    private int extractUserIdFromToken(String token) {
        return Integer.parseInt(token.split(Constants.DELIMITER)[0]);
    }
    private void isAuthorizedUser(String token) {
        if(StringUtils.isEmpty(token)) {
            log.error("Token is empty");
            throw new BadInputException(Constants.INVALID_TOKEN, "Token is empty");
        }
        authorizationUtils.isValidSession(token);
    }

    public List<Order> processOrder(String token) {
        log.info("Inside OrderDelegate :: processOrder() method");
        isAuthorizedUser(token);
        List<Order> response = orderService.processOrder(extractUserIdFromToken(token), token);
        log.info("OrderDelegate::processOrder ends successfully");
        return response == null ? new ArrayList<>(): response;
    }

    public List<Order> getOrders(String token, String orderId) {
        log.info("Inside OrderDelegate :: getOrders() method with orderId: {}", orderId);
        isAuthorizedUser(token);
        List<Order> response = orderService.getOrders(extractUserIdFromToken(token), orderId);
        if (Objects.isNull(response) || response.isEmpty()) {
            response = new ArrayList<>();
        }
        log.info("OrderDelegate::getOrders ends successfully");
        return response;
    }

    public OrderResponseDTO placeOrder(PlaceOrderRequestDTO placeOrderRequestDTO, String token) {
        log.info("Inside OrderDelegate :: placeOrder() method with processOrderRequestDTO: {}", placeOrderRequestDTO);
        isAuthorizedUser(token);

        if (StringUtils.isEmpty(placeOrderRequestDTO.getProductId())) {
            log.error("Product ID is empty");
            throw new BadInputException(Constants.INVALID_PRODUCT_ID, "Product ID is empty");
        }
        if(Objects.isNull(placeOrderRequestDTO.getPaymentDetails()))
            throw new BadInputException(Constants.INVALID_PAYMENT_DETAILS, "Payment details are empty");

        PaymentRequestDTO paymentDetails = placeOrderRequestDTO.getPaymentDetails();

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

        OrderResponseDTO response = orderService.placeOrder(placeOrderRequestDTO, extractUserIdFromToken(token), token);
        notificationService.sendNotification("Success");
        log.info("OrderDelegate::placeOrder ends successfully");
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

    public String cancelOrder(String orderId, String token) {
        log.info("Inside OrderDelegate :: cancelOrder() method with orderId: {}", orderId);
        isAuthorizedUser(token);
        if (StringUtils.isEmpty(orderId)) {
            log.error("Order ID is empty");
            throw new BadInputException(Constants.INVALID_ORDER_ID, "Order ID is empty");
        }
        String response = orderService.cancelOrder(orderId, extractUserIdFromToken(token), token);
        notificationService.sendNotification("Cancelled");
        log.info("OrderDelegate::cancelOrder ends successfully");
        return response;
    }
}
