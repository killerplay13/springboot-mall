//package com.vincent.springbootmall.dao.Impl;
//
//import com.tibco.tibjms.TibjmsConnectionFactory;
//import com.vincent.springbootmall.dao.DataOperation;
//import com.vincent.springbootmall.dto.ProductRequest;
//import org.json.simple.JSONObject;
//
//import javax.jms.*;
//
//public class JMSOperation implements DataOperation {
//    @Override
//    public Integer performOperation(ProductRequest productRequest) {
//        String serverUrl = "tcp://127.0.0.1:7222";
//        String username = "admin";
//        String password = "admin";
//        ConnectionFactory connectionFactory = null;
//        Integer productId = 0;
//        try {
//            connectionFactory = createConnectionFactory(serverUrl, username, password);
//        } catch (JMSException e) {
//            throw new RuntimeException(e);
//        }
//        try (Connection connection = connectionFactory.createConnection()) {
//            connection.start();
//            productId++;
//            String message = CovertToMessage(productRequest);
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            Destination destination = session.createQueue("mall.FOO");
//            MessageProducer producer = session.createProducer(destination);
//            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
//            TextMessage textMessage = session.createTextMessage(message);
//            textMessage.setStringProperty("productId",productId.toString());
//            producer.send(textMessage);
//        } catch (JMSException jmsException) {
//            jmsException.printStackTrace();
//        }
//        return productId;
//    }
//    private static ConnectionFactory createConnectionFactory(String serverUrl, String username, String password) throws JMSException {
//        TibjmsConnectionFactory connectionFactory = new TibjmsConnectionFactory();
//        connectionFactory.setServerUrl(serverUrl);
//        connectionFactory.setUserName(username);
//        connectionFactory.setUserPassword(password);
//
//        return connectionFactory;
//    }
//    private String CovertToMessage(ProductRequest productRequest){
//        String message = null;
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("productName", productRequest.getProductName());
//        jsonObject.put("category", productRequest.getCategory().name());
//        jsonObject.put("imageUrl", productRequest.getImageUrl());
//        jsonObject.put("price", productRequest.getPrice());
//        jsonObject.put("stock", productRequest.getStock());
//        jsonObject.put("description", productRequest.getDescription());
//        message = jsonObject.toString();
//        return message;
//    }
//}
