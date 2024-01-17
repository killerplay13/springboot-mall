package com.vincent.springbootmall.service.impl;

import com.vincent.springbootmall.dao.OrderDao;
import com.vincent.springbootmall.dao.ProductDao;
import com.vincent.springbootmall.dto.BuyItem;
import com.vincent.springbootmall.dto.CreateOrderRequest;
import com.vincent.springbootmall.model.Order;
import com.vincent.springbootmall.model.OrderItem;
import com.vincent.springbootmall.model.Product;
import com.vincent.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        int totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());
            int amount = buyItem.getQuantity() * product.getPrice();
            totalAmount += amount;

            // Create OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getProductId());
            orderItem.setQuantity(buyItem.getQuantity());
            orderItem.setAmount(amount);

            orderItems.add(orderItem);
        }

        // Create Order
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalAmount);
        order.setCreatedDate(new Date());
        order.setLastModifiedDate(new Date());

        // Save Order
        Integer orderId = orderDao.createOrderAndItems(order, orderItems);

        return orderId;
    }

}
