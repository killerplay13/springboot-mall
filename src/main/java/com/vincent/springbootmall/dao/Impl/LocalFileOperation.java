//package com.vincent.springbootmall.dao.Impl;
//
//import com.vincent.springbootmall.dao.DataOperation;
//import com.vincent.springbootmall.dto.ProductRequest;
//import org.json.simple.JSONObject;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//public class LocalFileOperation implements DataOperation {
//    @Override
//    public Integer performOperation(ProductRequest productRequest) {
//        String fileName = null;
//        String fileContent = null;
//        String outputfile = "D:/HahowSpringBootLab/productFile";
//        Integer productId = 0;
//        try {
//            productId++;
//            fileName = productId + "_" + productRequest.getCategory() + "_" + productRequest.getProductName() + ".txt";
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("productName", productRequest.getProductName());
//            jsonObject.put("category", productRequest.getCategory().name());
//            jsonObject.put("imageUrl", productRequest.getImageUrl());
//            jsonObject.put("price", productRequest.getPrice());
//            jsonObject.put("stock", productRequest.getStock());
//            jsonObject.put("description", productRequest.getDescription());
//            fileContent = jsonObject.toString();
//            Path filePath = Paths.get(outputfile, fileName);
//            writeToFile(filePath, fileContent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return productId;
//    }
//
//    private void writeToFile(Path filePath, String content) throws IOException {
//        try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
//            fileWriter.write(content);
//        }
//    }
//}
