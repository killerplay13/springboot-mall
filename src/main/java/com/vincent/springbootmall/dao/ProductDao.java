package com.vincent.springbootmall.dao;

import com.vincent.springbootmall.constant.ProductCategory;
import com.vincent.springbootmall.dto.ProductQueryParams;
import com.vincent.springbootmall.dto.ProductRequest;
import com.vincent.springbootmall.model.Product;
import com.vincent.springbootmall.service.ProductService;

import javax.jms.JMSException;
import java.util.List;

public interface ProductDao {

    Product getProductById(Integer productId);

    Integer createProduct(ProductRequest productRequest);

    void updateProduct(Integer productId, ProductRequest productRequest);

    void deleteProductById(Integer productId);

    List<Product> getProducts(ProductQueryParams productQueryParams);

    Integer countProduct(ProductQueryParams productQueryParams);

    void updateStock(Integer productId, Integer stock);

    Integer recordToLocalFile(ProductRequest productRequest);

    void sendToTIBCOEMS(String message) throws JMSException;

    Integer createProductDatabaseOperation(ProductRequest productRequest);

    Integer createProductLocalFileOperation(ProductRequest productRequest);

    Integer createProductJMSOperation(ProductRequest productRequest);

    Product getProductDatabaseOperation(Integer productId);

    Product getProductLocalFileOperation(Integer productId);

    Product getProductJMSOperation(Integer productId);
}
