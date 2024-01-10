package com.vincent.springbootmall.service.impl;

import com.vincent.springbootmall.dao.ProductDao;
import com.vincent.springbootmall.model.Product;
import com.vincent.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }
}
