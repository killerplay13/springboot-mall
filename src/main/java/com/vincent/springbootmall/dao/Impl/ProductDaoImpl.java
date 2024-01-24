package com.vincent.springbootmall.dao.Impl;

import com.tibco.tibjms.TibjmsQueueConnectionFactory;
import com.vincent.springbootmall.constant.ProductCategory;
import com.vincent.springbootmall.dao.ProductDao;
import com.vincent.springbootmall.dto.ProductQueryParams;
import com.vincent.springbootmall.dto.ProductRequest;
import com.vincent.springbootmall.model.Product;
import com.vincent.springbootmall.util.ShortUuidGenerator;
import com.vincent.springbootmall.rowmapper.ProductRowMapper;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.support.KeyHolder;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import javax.jms.*;
import javax.jms.Queue;

import com.tibco.tibjms.TibjmsConnectionFactory;

@Component
public class ProductDaoImpl implements ProductDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    static Integer fileProductId = 0;
    static Integer jmsProductId = 0;

    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date FROM product WHERE product_id = :productId";

        Map<String, Integer> map = new HashMap<>();
        map.put("productId", productId);
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        if (productList.size() > 0) {
            return productList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)" +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int productId = keyHolder.getKey().intValue();
        return productId;
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {

        String sql = "UPDATE product SET product_name = :productName, " +
                "category = :category, " +
                "image_url = :imageUrl, " +
                "price = :price, " +
                "stock = :stock, " +
                "description = :description, " +
                "last_modified_date = :lastModifiedDate " +
                "WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());
        map.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public void deleteProductById(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);

        namedParameterJdbcTemplate.update(sql, map);
    }

    @Override
    public List<Product> getProducts(ProductQueryParams productQueryParams) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date FROM product WHERE 1=1";
        Map<String, Object> map = new HashMap<>();
        sql = addFiltreringSql(sql, map, productQueryParams);
        //sort
        sql = sql + " ORDER BY " + productQueryParams.getOrderBy() + " " + productQueryParams.getSort();
        //page
        sql = sql + " LIMIT :limit OFFSET :offset";
        map.put("limit", productQueryParams.getLimit());
        map.put("offset", productQueryParams.getOffset());
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        return productList;
    }

    @Override
    public Integer countProduct(ProductQueryParams productQueryParams) {
        String sql = "SELECT count(*) FROM product WHERE 1=1";

        Map<String, Object> map = new HashMap<>();

        sql = addFiltreringSql(sql, map, productQueryParams);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);
        return total;
    }

    @Override
    public void updateStock(Integer productId, Integer stock) {
        String sql = "UPDATE product SET stock = :stock, last_modified_date = :lastModifiedDate WHERE product_id = :productId";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("productId", productId);
        paramMap.put("stock", stock);
        paramMap.put("lastModifiedDate", new Date());

        namedParameterJdbcTemplate.update(sql, paramMap);
    }

    @Override
    public Integer recordToLocalFile(ProductRequest productRequest) {
        String fileName = null;
        String fileContent = null;
        Integer productId = createProduct(productRequest);
        String outputfile = "D:/HahowSpringBootLab/productFile";
        try {
            fileName = productId + "_" + productRequest.getCategory() + "_" + productRequest.getProductName() + ".txt";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("productName", productRequest.getProductName());
            jsonObject.put("category", productRequest.getCategory().name());
            jsonObject.put("imageUrl", productRequest.getImageUrl());
            jsonObject.put("price", productRequest.getPrice());
            jsonObject.put("stock", productRequest.getStock());
            jsonObject.put("description", productRequest.getDescription());
            fileContent = jsonObject.toString();
            Path filePath = Paths.get(outputfile, fileName);
            writeToFile(filePath, fileContent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return productId;
    }

    private String addFiltreringSql(String sql, Map<String, Object> map, ProductQueryParams productQueryParams) {
        if (productQueryParams.getCategory() != null) {
            sql = sql + " AND category = :category";
            map.put("category", productQueryParams.getCategory().name());
        }

        if (productQueryParams.getSearch() != null) {
            sql = sql + " AND product_name LIKE :search";
            map.put("search", "%" + productQueryParams.getSearch() + "%");
        }
        return sql;
    }

    public void sendToTIBCOEMS(String message) throws JMSException {
        String serverUrl = "tcp://127.0.0.1:7222";
        String username = "admin";
        String password = "admin";
        ConnectionFactory connectionFactory = createConnectionFactory(serverUrl, username, password);
        try (Connection connection = connectionFactory.createConnection()) {
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("mall.FOO");
            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            TextMessage textMessage = session.createTextMessage(message);
            producer.send(textMessage);
        } catch (JMSException jmsException) {
            jmsException.printStackTrace();
        }
    }

    @Override
    public Integer createProductDatabaseOperation(ProductRequest productRequest) {
        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, description, created_date, last_modified_date)" +
                "VALUES (:productName, :category, :imageUrl, :price, :stock, :description, :createdDate, :lastModifiedDate)";

        Map<String, Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int productId = keyHolder.getKey().intValue();
        return productId;
    }

    @Override
    public Integer createProductLocalFileOperation(ProductRequest productRequest) {
        String fileName = null;
        String fileContent = null;
        String outputfile = "D:/HahowSpringBootLab/productFile";
        int fileProductId = getLargestId(outputfile);
        try {
            fileProductId++;
            fileName = fileProductId + "_" + productRequest.getCategory() + "_" + productRequest.getProductName() + ".txt";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("productName", productRequest.getProductName());
            jsonObject.put("category", productRequest.getCategory().name());
            jsonObject.put("imageUrl", productRequest.getImageUrl());
            jsonObject.put("price", productRequest.getPrice());
            jsonObject.put("stock", productRequest.getStock());
            jsonObject.put("description", productRequest.getDescription());
            fileContent = jsonObject.toString();
            Path filePath = Paths.get(outputfile, fileName);
            writeToFile(filePath, fileContent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileProductId;
    }

    private void writeToFile(Path filePath, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath.toFile())) {
            fileWriter.write(content);
        }
    }

    private static int getLargestId(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        int largestId = 0;

        if (files != null) {
            for (File file : files) {
                int fileId = extractId(file.getName());
                if (fileId != -1) {
                    largestId = Math.max(largestId, fileId);
                }
            }
        }

        return largestId;
    }

    private static int extractId(String fileName) {
        try {
            String[] parts = fileName.split("_");
            return Integer.parseInt(parts[0]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return -1; // Invalid ID
        }
    }

    @Override
    public Integer createProductJMSOperation(ProductRequest productRequest) {
        String serverUrl = "tcp://127.0.0.1:7222";
        String username = "admin";
        String password = "admin";
        ConnectionFactory connectionFactory = null;
        String productUuid = null;
        try {
            connectionFactory = createConnectionFactory(serverUrl, username, password);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        try (Connection connection = connectionFactory.createConnection()) {
            connection.start();
            jmsProductId++;
            productUuid =  ShortUuidGenerator.generatorShortUuid();
            String message = CovertToMessage(productRequest);
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("mall.FOO"); //send to Queue
            Destination destination1 = session.createTopic("mall.FOO");//send to Topic
            MessageProducer producer = session.createProducer(destination);
            MessageProducer producer1 = session.createProducer(destination1);
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            producer1.setDeliveryMode(DeliveryMode.PERSISTENT);
            TextMessage textMessage = session.createTextMessage(message);
            textMessage.setStringProperty("productId", jmsProductId.toString());
            textMessage.setStringProperty("productUuid",productUuid);
            producer.send(textMessage);
            producer1.send(textMessage);
        } catch (JMSException jmsException) {
            jmsException.printStackTrace();
        }
        return jmsProductId;
    }

    private String CovertToMessage(ProductRequest productRequest) {
        String message = null;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("productName", productRequest.getProductName());
        jsonObject.put("category", productRequest.getCategory().name());
        jsonObject.put("imageUrl", productRequest.getImageUrl());
        jsonObject.put("price", productRequest.getPrice());
        jsonObject.put("stock", productRequest.getStock());
        jsonObject.put("description", productRequest.getDescription());
        message = jsonObject.toString();
        return message;
    }

    private static ConnectionFactory createConnectionFactory(String serverUrl, String username, String password) throws JMSException {
        TibjmsConnectionFactory connectionFactory = new TibjmsConnectionFactory();
        connectionFactory.setServerUrl(serverUrl);
        connectionFactory.setUserName(username);
        connectionFactory.setUserPassword(password);

        return connectionFactory;
    }

    @Override
    public Product getProductDatabaseOperation(Integer productId) {
        String sql = "SELECT product_id, product_name, category, image_url, price, stock, description, created_date, last_modified_date FROM product WHERE product_id = :productId";

        Map<String, Integer> map = new HashMap<>();
        map.put("productId", productId);
        List<Product> productList = namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        if (productList.size() > 0) {
            return productList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public Product getProductLocalFileOperation(Integer productId) {
        String fileLocation = "D:/HahowSpringBootLab/productFile";
        File directory = new File(fileLocation);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().contains(productId.toString())) {
                    String filePath = file.getAbsolutePath();
                    return readProductFromFile(productId, filePath);
                }
            }
        }
        return null;
    }

    private Product readProductFromFile(Integer productId, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String productInfo = reader.readLine();
            Product product = Product.fromJson(productInfo);
            product.setProductId(productId);
            return product;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Product getProductJMSOperation(Integer productId) {
        String serverUrl = "tcp://127.0.0.1:7222";
        String username = "admin";
        String password = "admin";
        String queueName = "mall.FOO";


        AtomicReference<Product> receivedProduct = new AtomicReference<>();
        try {
        QueueConnectionFactory factory = new TibjmsQueueConnectionFactory(serverUrl);
        QueueConnection connection = factory.createQueueConnection(username, password);
        connection.start();
        QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(queueName);
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(message -> {
            if (message instanceof TextMessage) {
                try {
                    TextMessage textMessage = (TextMessage) message;
                    Product product = Product.fromJson(textMessage.getText());
                    receivedProduct.set(product);
                } catch (JMSException e) {
                    e.printStackTrace();
                }}
        });
            Thread.sleep(1000);
            connection.close();
        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }
        Product product = receivedProduct.get();
        return product;
    }
}





