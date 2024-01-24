//package com.vincent.springbootmall.dao.Impl;
//
//import com.vincent.springbootmall.dao.DataOperation;
//import com.vincent.springbootmall.dto.ProductRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.support.GeneratedKeyHolder;
//import org.springframework.jdbc.support.KeyHolder;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//public class DatabaseOperation implements DataOperation {
//    @Autowired
//    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
//
//
//    @Override
//    public Integer performOperation(ProductRequest productRequest) {
//        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)" +
//                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("productName", productRequest.getProductName());
//        map.put("category", productRequest.getCategory().toString());
//        map.put("imageUrl", productRequest.getImageUrl());
//        map.put("price", productRequest.getPrice());
//        map.put("stock", productRequest.getStock());
//        map.put("description", productRequest.getDescription());
//
//        Date now = new Date();
//        map.put("createdDate", now);
//        map.put("lastModifiedDate", now);
//
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
//
//        int productId = keyHolder.getKey().intValue();
//        return productId;
//    }
//}
