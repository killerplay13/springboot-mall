package com.vincent.springbootmall.service.impl;

import com.vincent.springbootmall.constant.ProductCategory;
//import com.vincent.springbootmall.dao.Impl.DatabaseOperation;
//import com.vincent.springbootmall.dao.Impl.JMSOperation;
//import com.vincent.springbootmall.dao.Impl.LocalFileOperation;
import com.vincent.springbootmall.dao.ProductDao;
import com.vincent.springbootmall.dto.ProductQueryParams;
import com.vincent.springbootmall.dto.ProductRequest;
import com.vincent.springbootmall.model.Product;
import com.vincent.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.Operation;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    @Override
    public Product getProductById(Integer productId) {
        try {
            Product product = productDao.getProductById(productId);
            if (product != null) {
                productDao.sendToTIBCOEMS(product.toString());
            } else {
                System.err.println("Error: getProductById returned null for productId: " + productId);
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        return productDao.getProductById(productId);
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
//        productDao.recordToLocalFile(productRequest);
        return productDao.recordToLocalFile(productRequest);
    }

    @Override
    public void updatedProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId, productRequest);

    }

    @Override
    public void deleteProductById(Integer productId) {
        productDao.deleteProductById(productId);

    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        return productDao.getProducts(productQueryParams);
    }

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        return productDao.countProduct(productQueryParams);
    }

    @Override
    public Product getProductByOperation(Integer productId, String dataOperation) {
        String operation = dataOperation.toLowerCase();
        switch (operation) {
            case "database":
                return productDao.getProductDatabaseOperation(productId);
            case "localfile":
                return productDao.getProductLocalFileOperation(productId);
            case "jms":
                return null;
            default:
                throw new IllegalArgumentException("Invalid data operation type");
        }
    }

    @Override
    public Integer createProductByOperation(ProductRequest productRequest, String dataOperation) {
        String operation = dataOperation.toLowerCase();
        switch (operation) {
            case "database":
                return productDao.createProductDatabaseOperation(productRequest);
            case "localfile":
                return productDao.createProductLocalFileOperation(productRequest);
            case "jms":
                return productDao.createProductJMSOperation(productRequest);
            default:
                throw new IllegalArgumentException("Invalid data operation type");
        }
    }
}
