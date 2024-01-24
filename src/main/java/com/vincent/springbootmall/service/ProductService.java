package com.vincent.springbootmall.service;

import com.vincent.springbootmall.dto.ProductQueryParams;
import com.vincent.springbootmall.dto.ProductRequest;
import com.vincent.springbootmall.model.Product;

import java.util.List;

public interface ProductService {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updatedProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Integer countProduct(ProductQueryParams productQueryParams);

    Product getProductByOperation(Integer productId, String dataOperation);

    Integer createProductByOperation(ProductRequest productRequest, String dataOperation);


}
