package com.vincent.springbootmall.service;

import com.vincent.springbootmall.dto.CreateOrderRequest;
import com.vincent.springbootmall.dto.GetOrderItemAndProduct;
import com.vincent.springbootmall.model.Order;

import java.util.List;

public interface OrderService {

    Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest);

    Order getOrderById(Integer orderId);

//    List<GetOrderItemAndProduct> getOrderItemsByOrderId(Integer orderId);

}
