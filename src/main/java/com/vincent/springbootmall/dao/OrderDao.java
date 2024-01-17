package com.vincent.springbootmall.dao;

import com.vincent.springbootmall.model.Order;
import com.vincent.springbootmall.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {

    Order save(Order order);

    OrderItem save(OrderItem orderItem);

    // Custom method to create an order and associated order items
    default Integer createOrderAndItems(Order order, List<OrderItem> orderItems) {
        Order savedOrder = save(order);
        Integer orderId = savedOrder.getOrderId();
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(orderId);
            save(orderItem);
        }
        return orderId;
    }
}



