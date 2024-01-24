package com.vincent.springbootmall.controller;

import com.vincent.springbootmall.constant.ProductCategory;
import com.vincent.springbootmall.dto.ProductQueryParams;
import com.vincent.springbootmall.dto.ProductRequest;
import com.vincent.springbootmall.model.Product;
import com.vincent.springbootmall.service.ProductService;
import com.vincent.springbootmall.util.page;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Api(tags = "product 相關API")
@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation("取得產品 by ID")
    @ApiResponses({
            @ApiResponse(code = 401, message = "沒有權限"),
            @ApiResponse(code = 404, message = "找不到路徑")
    })
    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);// then send to EMS
//        Product product = productService.getProductFromLocalFile(productId); //
        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Integer productId = productService.createProduct(productRequest);
        Product product = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @ApiOperation("更新產品")
    @ApiResponses({
            @ApiResponse(code = 401, message = "沒有權限"),
            @ApiResponse(code = 404, message = "找不到路徑")
    })
    @PutMapping("/products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId, @RequestBody @Valid ProductRequest productRequest) {
        Product product = productService.getProductById(productId);

        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        productService.updatedProduct(productId, productRequest);
        Product updateProduct = productService.getProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @ApiOperation("刪除產品")
    @ApiResponses({
            @ApiResponse(code = 401, message = "沒有權限"),
            @ApiResponse(code = 404, message = "找不到路徑")
    })
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProductById(productId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @ApiOperation("取得全部產品")
    @ApiResponses({
            @ApiResponse(code = 401, message = "沒有權限"),
            @ApiResponse(code = 404, message = "找不到路徑")
    })
    @GetMapping("/products")
    public ResponseEntity<page<Product>> getProducts(
            //Filtering
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,
            //Sorting
            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,
            //pagination
            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ) {
        ProductQueryParams productQueryParams = new ProductQueryParams();
        productQueryParams.setCategory(category);
        productQueryParams.setSearch(search);
        productQueryParams.setOrderBy(orderBy);
        productQueryParams.setSort(sort);
        productQueryParams.setLimit(limit);
        productQueryParams.setOffset(offset);
        List<Product> productList = productService.getProducts(productQueryParams);

        Integer total = productService.countProduct(productQueryParams);
        page<Product> page = new page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResult(productList);
        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    @ApiOperation("新增產品(3種方式)")
    @ApiResponses({
            @ApiResponse(code = 401, message = "沒有權限"),
            @ApiResponse(code = 404, message = "找不到路徑")
    })
    @PostMapping("/products/{dataOperation}")
    public ResponseEntity<Product> createProductByOperation(@RequestBody @Valid ProductRequest productRequest, @PathVariable @ApiParam("操作模式(database/localFile/jms)") String dataOperation) {
        Integer productId = productService.createProductByOperation(productRequest, dataOperation);
        Product product = productService.getProductByOperation(productId, dataOperation);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }
}
