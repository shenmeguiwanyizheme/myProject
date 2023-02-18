package com.example.service;

import com.example.mapper.user.UserMapper;
import com.example.pojo.user.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;

    public User extractByUsername(String username) {
        return userMapper.extractByUsername(username);
    }
}
