package com.vincent.springbootmall.service;

import com.vincent.springbootmall.dto.UserRegisterRequest;
import com.vincent.springbootmall.model.User;

public interface UserService {

    Integer register(UserRegisterRequest userRegisterRequest);
    User getUserById(Integer userId);
}
