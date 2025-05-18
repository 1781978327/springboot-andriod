package com.example.demo2.service.impl;

import com.example.demo2.model.User;
import com.example.demo2.model.UserRequest;
import com.example.demo2.repository.UserRepository;
import com.example.demo2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User register(UserRequest request) {
        log.info("开始注册用户: {}", request.getUsername());
        log.info("注册信息: username={}, email={}", request.getUsername(), request.getEmail());
        
        // 检查用户名是否已存在
        User existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser != null) {
            log.warn("用户名已存在: {}", request.getUsername());
            throw new RuntimeException("用户名已存在");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setAvatar("default_avatar.png");

        try {
            // 保存用户
            User savedUser = userRepository.save(user);
            log.info("用户注册成功: id={}, username={}", savedUser.getId(), savedUser.getUsername());
            return savedUser;
        } catch (Exception e) {
            log.error("用户注册失败: {}", e.getMessage(), e);
            throw new RuntimeException("注册失败: " + e.getMessage());
        }
    }

    @Override
    public User login(String username, String password) {
        log.info("尝试登录用户: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.warn("登录失败: 用户不存在 - {}", username);
            throw new RuntimeException("用户名或密码错误");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("登录失败: 密码错误 - {}", username);
            throw new RuntimeException("用户名或密码错误");
        }
        log.info("用户登录成功: id={}, username={}", user.getId(), user.getUsername());
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
} 