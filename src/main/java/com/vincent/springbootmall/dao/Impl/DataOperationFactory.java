//package com.vincent.springbootmall.dao.Impl;
//
//import com.vincent.springbootmall.dao.DataOperation;
//
//public class DataOperationFactory {
//    public static DataOperation createDataOperation(String type) {
//        switch (type) {
//            case "database":
//                return new DatabaseOperation();
//            case "localFile":
//                return new LocalFileOperation();
//            case "jms":
//                return new JMSOperation();
//            default:
//                throw new IllegalArgumentException("Invalid data operation type");
//        }
//    }
//}
