package com.vincent.springbootmall.dao;

import com.vincent.springbootmall.model.Product;

public interface ProductDao {

    Product getProductById(Integer productId);
}
