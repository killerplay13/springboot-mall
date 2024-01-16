package com.vincent.springbootmall.service.impl;

import com.vincent.springbootmall.dao.UserDao;
import com.vincent.springbootmall.dto.UserRegisterRequest;
import com.vincent.springbootmall.model.User;
import com.vincent.springbootmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        return userDao.createUser(userRegisterRequest);
    }

    @Override
    public User getUserById(Integer userId) {
        return userDao.getUserById(userId);
    }
}
