package com.example.demo2.service;

import com.example.demo2.model.User;
import com.example.demo2.model.UserRequest;
import java.util.List;

public interface UserService {
    User register(UserRequest request);
    User login(String username, String password);
    User findByUsername(String username);
    List<User> findAll();
} 