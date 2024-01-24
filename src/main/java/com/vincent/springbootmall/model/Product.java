package com.vincent.springbootmall.model;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.vincent.springbootmall.constant.ProductCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

@ApiModel("產品實體")
public class Product {
    @ApiModelProperty("產品ID")
    private Integer productId;
    @ApiModelProperty("產品名稱")
    private String productName;
    @ApiModelProperty("產品分類")
    private ProductCategory category;
    @ApiModelProperty("產品照片")
    private String imageUrl;
    @ApiModelProperty("產品價格")
    private Integer price;
    @ApiModelProperty("產品庫存")
    private Integer stock;
    @ApiModelProperty("產品描述")
    private String description;
    @ApiModelProperty("建立時間")
    private Date createdDate;
    @ApiModelProperty("最後修改時間")
    private Date lastModifiedDate;

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return String.format("{\n\"productId\": %d,\n\"productName\": \"%s\",\n\"category\": \"%s\",\n" +
                        "\"imageUrl\": \"%s\",\n\"price\": %d,\n\"stock\": %d,\n\"description\": \"%s\",\n" +
                        "\"createdDate\": \"%s\",\n\"lastModifiedDate\": \"%s\"\n}",
                productId, productName, category, imageUrl, price, stock, description,
                dateFormat.format(createdDate), dateFormat.format(lastModifiedDate));
    }

    public static Product fromJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, Product.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error converting JSON to Product object", e);
        }
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
