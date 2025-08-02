package com.nagp.order.controller;

import com.nagp.order.delegate.OrderDelegate;
import com.nagp.order.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderDelegate orderDelegate;

    @PostMapping("/processOrder")
    public ResponseEntity<ResponseTO<List<Order>>> processOrder(@RequestParam("token") String token) {
        log.info("Inside OrderController :: processOrder() method");
        ResponseTO<List<Order>> responseTO = new ResponseTO<>();
        responseTO.setData(orderDelegate.processOrder(token));
        log.info("OrderController::processOrder ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    @GetMapping("/getOrders")
    public ResponseEntity<ResponseTO<List<Order>>> getOrders(@RequestParam("token") String token,
                                                             @RequestParam(value = "orderId", required = false) String orderId) {
        log.info("Inside OrderController :: getOrders() method");
        ResponseTO<List<Order>> responseTO = new ResponseTO<>();
        responseTO.setData(orderDelegate.getOrders(token, orderId));
        log.info("OrderController::getOrders ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<ResponseTO<OrderResponseDTO>> placeOrder(@RequestBody PlaceOrderRequestDTO placeOrderRequestDTO,
                                                                   @RequestParam("token") String token) {
        log.info("Inside OrderController :: placeOrder() method with processOrderRequestDTO: {}", placeOrderRequestDTO);
        ResponseTO<OrderResponseDTO> responseTO = new ResponseTO<>();
        responseTO.setData(orderDelegate.placeOrder(placeOrderRequestDTO, token));
        log.info("OrderController::placeOrder ends successfully");
        return ResponseEntity.ok(responseTO);
    }

    @PostMapping("/cancelOrder")
    public ResponseEntity<ResponseTO<String>> cancelOrder(@RequestParam(value = "orderId") String orderId,
                                                           @RequestParam("token") String token) {
        log.info("Inside OrderController :: cancelOrder() method with orderId: {}", orderId);
        ResponseTO<String> responseTO = new ResponseTO<>();
        responseTO.setData(orderDelegate.cancelOrder(orderId, token));
        log.info("OrderController::cancelOrder ends successfully");
        return ResponseEntity.ok(responseTO);
    }

}
