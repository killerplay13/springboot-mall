package com.vincent.springbootmall.dto;

import com.vincent.springbootmall.constant.ProductCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel("產品需求")
public class ProductRequest {

    @NotNull
    @ApiModelProperty("產品名稱")
    private String productName;

    @NotNull
    @ApiModelProperty("產品分類")
    private ProductCategory category;

    @NotNull
    @ApiModelProperty("產品照片")
    private String imageUrl;

    @NotNull
    @ApiModelProperty("產品價格")
    private Integer price;

    @NotNull
    @ApiModelProperty("產品庫存")
    private Integer stock;

    @ApiModelProperty("產品描述")
    private String description;

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
}
