package com.vincent.springbootmall.model;

import com.vincent.springbootmall.dto.GetOrderItemAndProduct;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "`ORDER`")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "total_amount", nullable = false)
    private Integer totalAmount;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "last_modified_date", nullable = false)
    private Date lastModifiedDate;
    @Transient
    private List<GetOrderItemAndProduct> orderItemList;

    public List<GetOrderItemAndProduct> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<GetOrderItemAndProduct> orderItemList) {
        this.orderItemList = orderItemList;
    }

    public Order() {
    }

    public Order(Integer userId, Integer totalAmount, Date createdDate, Date lastModifiedDate) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}

