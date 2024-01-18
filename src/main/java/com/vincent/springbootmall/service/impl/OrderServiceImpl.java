package com.vincent.springbootmall.service.impl;

import com.vincent.springbootmall.dao.OrderDao;
import com.vincent.springbootmall.dao.ProductDao;
import com.vincent.springbootmall.dto.BuyItem;
import com.vincent.springbootmall.dto.CreateOrderRequest;
import com.vincent.springbootmall.dto.GetOrderItemAndProduct;
import com.vincent.springbootmall.model.Order;
import com.vincent.springbootmall.model.OrderItem;
import com.vincent.springbootmall.model.Product;
import com.vincent.springbootmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;

    @Override
    @Transactional
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

    @Override
    public Order getOrderById(Integer orderId) {
        Optional<Order> optionalOrder = orderDao.findById(orderId);
        Order order = optionalOrder.orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        List<Object[]> result = orderDao.getOrderItemsWithProduct(orderId);
        List<GetOrderItemAndProduct> orderItemList  = convertToDto(result);
        order.setOrderItemList(orderItemList );
        return order;
    }

//    public List<GetOrderItemAndProduct> getOrderItemsByOrderId(Integer orderId) {
//        return orderDao.getOrderItemsWithProduct(orderId);
//    }


    public List<GetOrderItemAndProduct> convertToDto(List<Object[]> result) {
        List<GetOrderItemAndProduct> dtoList = new ArrayList<>();

        for (Object[] row : result) {
            dtoList.add(new GetOrderItemAndProduct(
                    (Integer) row[0],  // order_item_id
                    (Integer) row[1],  // order_id
                    (Integer) row[2],  // product_id
                    (Integer) row[3],  // quantity
                    (Integer) row[4],  // amount
                    (String) row[5],   // product_name
                    (String) row[6]    // image_url
            ));
        }

        return dtoList;
    }

}
