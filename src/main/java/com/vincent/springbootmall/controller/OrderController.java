package com.vincent.springbootmall.controller;

import com.vincent.springbootmall.dto.CreateOrderRequest;
import com.vincent.springbootmall.dto.OrderQueryParams;
import com.vincent.springbootmall.model.Order;
import com.vincent.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import com.vincent.springbootmall.util.page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/users/{userId}/orders")
    public ResponseEntity<?> createOrder(@PathVariable Integer userId,
                                         @RequestBody @Valid CreateOrderRequest createOrderRequest) {
        Integer orderId = orderService.createOrder(userId, createOrderRequest);

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<page<Order>> getOrders(
            @PathVariable Integer userId,
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset) {
        OrderQueryParams queryParams = new OrderQueryParams();
        queryParams.setUserId(userId);
        queryParams.setLimit(limit);
        queryParams.setOffset(offset);

        List<Order> orderList = orderService.getOrders(queryParams);
        Integer count = orderService.countOrder(queryParams);

        page<Order> page = new page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(count);
        page.setResult(orderList);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }
}
