package com.nagp.order.service.impl;

import com.nagp.order.dto.*;
import com.nagp.order.enums.OrderStatus;
import com.nagp.order.exceptions.BadInputException;
import com.nagp.order.service.OrderService;
import com.nagp.order.utils.CartUtils;
import com.nagp.order.utils.PaymentUtils;
import com.nagp.order.utils.ProductUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    Map<Integer,List<Order>> orders = new HashMap<>();

    @Autowired
    private ProductUtils productUtils;

    @Autowired
    private CartUtils cartUtils;

    @Autowired
    private PaymentUtils paymentUtils;

    @Override
    public List<Order> processOrder(int userId, String session) {
        List<Product> products;
            products = cartUtils.checkout(session,null);


        products.forEach(product -> {
            Order order = Order.builder()
                    .orderId(String.valueOf(System.currentTimeMillis()))
                    .productId(product.getId())
                    .productName(product.getName())
                    .orderDate(String.valueOf(LocalDate.now()))
                    .price(product.getPrice() * product.getQuantity())
                    .paymentType(null)
                    .paymentDate(null)
                    .paymentId(null)
                    .status(OrderStatus.PENDING.getStatus())
                    .productDescription(product.getDescription())
                    .quantity(String.valueOf(product.getQuantity()))
                    .build();

            orders.putIfAbsent(userId,new ArrayList<>());
            orders.get(userId).add(order);
        });
        return products.isEmpty() ? new ArrayList<>() : orders.get(userId);
    }

    @Override
    public List<Order> getOrders(int userId, String orderId) {
        if(StringUtils.isEmpty(orderId)) {
            return orders.get(userId);
        } else {
            return orders.get(userId).stream().filter(order -> order.getOrderId().equalsIgnoreCase(orderId)).toList();
        }
    }
    @Override
    public OrderResponseDTO placeOrder(PlaceOrderRequestDTO placeOrderRequestDTO, int userId, String token) {

        if(orders.get(userId) == null)
            throw new BadInputException("Order not found", "Order not found");
        List<Order> orderList = orders.get(userId).stream().filter(order -> order.getOrderId().equalsIgnoreCase(placeOrderRequestDTO.getOrderId())).collect(Collectors.toList());

        if(orderList.isEmpty())
            throw new BadInputException("Order not found", "Order not found");

        if(orderList.get(0).getPrice() != placeOrderRequestDTO.getPaymentDetails().getAmount())
            throw new BadInputException("Invalid amount", "please check the amount");
        if(orderList.get(0).getStatus().equalsIgnoreCase(OrderStatus.CONFIRMED.getStatus()))
            throw new BadInputException("Order already confirmed", "Order already confirmed");

        productUtils.canplaceOrder(token,placeOrderRequestDTO.getProductId(), orderList.get(0).getQuantity());
        PaymentResponseDTO paymentResponseDTO = paymentUtils.processPayment(placeOrderRequestDTO.getPaymentDetails(), token);

        if(paymentResponseDTO.getStatus().equalsIgnoreCase("success")) {
            orderList.get(0).setPaymentId(paymentResponseDTO.getTransactionId());
            orderList.get(0).setPaymentDate(String.valueOf(LocalDate.now()));
            orderList.get(0).setStatus(OrderStatus.CONFIRMED.getStatus());
            return OrderResponseDTO.builder().order(orderList.get(0)).paymentResponse(paymentResponseDTO).build();
        }
        else {
            orderList.get(0).setStatus(OrderStatus.FAILED.getStatus());
            return OrderResponseDTO.builder().order(orderList.get(0)).paymentResponse(paymentResponseDTO).build();
        }
    }

    @Override
    public String cancelOrder(String orderId, int userId, String token) {
        if(orders.get(userId) == null)
            throw new BadInputException("Order not found", "Order not found");
        List<Order> orderList = orders.get(userId).stream().filter(order -> order.getOrderId().equalsIgnoreCase(orderId)).collect(Collectors.toList());

        if(orderList.isEmpty())
            throw new BadInputException("Order not found", "Order not found");

        if(orderList.get(0).getStatus().equalsIgnoreCase(OrderStatus.CANCELLED.getStatus()))
            throw new BadInputException("Order already cancelled", "Order already cancelled");

        orderList.get(0).setStatus(OrderStatus.CANCELLED.getStatus());
        return "Order cancelled successfully";
    }
}
