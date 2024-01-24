package com.vincent.springbootmall.dao;

import com.vincent.springbootmall.dto.ProductRequest;

public interface DataOperation {
    Integer performOperation(ProductRequest productRequest);
}
