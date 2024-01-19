package com.vincent.springbootmall.service.impl;

import com.vincent.springbootmall.dao.OrderDao;
import com.vincent.springbootmall.dao.ProductDao;
import com.vincent.springbootmall.dao.UserDao;
import com.vincent.springbootmall.dto.BuyItem;
import com.vincent.springbootmall.dto.CreateOrderRequest;
import com.vincent.springbootmall.dto.GetOrderItemAndProduct;
import com.vincent.springbootmall.dto.OrderQueryParams;
import com.vincent.springbootmall.model.Order;
import com.vincent.springbootmall.model.OrderItem;
import com.vincent.springbootmall.model.Product;
import com.vincent.springbootmall.model.User;
import com.vincent.springbootmall.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class OrderServiceImpl implements OrderService {

    private final static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private UserDao userDao;

    @Override
    @Transactional
    public Integer createOrder(Integer userId, CreateOrderRequest createOrderRequest) {
        //check the user
        User user = userDao.getUserById(userId);

        if (user == null){
            log.warn("userId{} not exist", userId);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        int totalAmount = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (BuyItem buyItem : createOrderRequest.getBuyItemList()) {
            Product product = productDao.getProductById(buyItem.getProductId());

            //check stock
            if (product == null){
                log.warn("product {} not exist", buyItem.getProductId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }else if(product.getStock() < buyItem.getQuantity()){
                log.warn("product {} run out of stock, can't buy it 。 remain stock {} , require quantity{}",
                        buyItem.getProductId(),product.getStock(),buyItem.getQuantity());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            productDao.updateStock(product.getProductId(), product.getStock() - buyItem.getQuantity());

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

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        return orderDao.countOrder(orderQueryParams);
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        List<Order> orderList = orderDao.getOrders(orderQueryParams);
        for (Order order : orderList){
            List<Object[]> result = orderDao.getOrderItemsWithProduct(order.getOrderId());
            List<GetOrderItemAndProduct> orderItemList  = convertToDto(result);
            order.setOrderItemList(orderItemList);
        }
        return orderList;
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
