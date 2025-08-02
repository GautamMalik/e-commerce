package com.nagp.order.service;

import com.nagp.order.dto.Order;
import com.nagp.order.dto.OrderResponseDTO;
import com.nagp.order.dto.PlaceOrderRequestDTO;

import java.util.List;

public interface OrderService {
    List<Order> processOrder( int userId, String session);

    List<Order> getOrders(int userID, String orderId);

    OrderResponseDTO placeOrder(PlaceOrderRequestDTO placeOrderRequestDTO, int userId, String token);

    String cancelOrder(String orderId, int userId, String token);
}
