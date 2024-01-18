package com.vincent.springbootmall.dao;

import com.vincent.springbootmall.dto.GetOrderItemAndProduct;
import com.vincent.springbootmall.model.Order;
import com.vincent.springbootmall.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {

    Order save(Order order);

    OrderItem save(OrderItem orderItem);

    Optional<Order> findById(Integer orderId);

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

    @Query(value = "SELECT oi.order_item_id, oi.order_id, oi.product_id, oi.quantity, oi.amount, p.product_name, p.image_url " +
            "FROM order_item as oi LEFT JOIN product as p ON oi.product_id = p.product_id WHERE oi.order_id = :orderId", nativeQuery = true)
    List<Object[]> getOrderItemsWithProduct(@Param("orderId") Integer orderId);
}



