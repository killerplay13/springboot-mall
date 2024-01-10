package com.vincent.springbootmall.service;

import com.vincent.springbootmall.dto.ProductRequest;
import com.vincent.springbootmall.model.Product;

public interface ProductService{

    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    void updatedProduct(Integer productId, ProductRequest productRequest);
}
