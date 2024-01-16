package com.vincent.springbootmall.dao;

import com.vincent.springbootmall.dto.UserRegisterRequest;
import com.vincent.springbootmall.model.User;

public interface UserDao {
    Integer createUser(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);

    User getUserByEmail(String email);
}
