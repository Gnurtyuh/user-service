package com.project.user.service.impl;

import com.project.user.dto.request.UserRequest;
import com.project.user.dto.response.UserResponse;
import com.project.user.entity.Users;
import com.project.user.mapper.UserMapper;
import com.project.user.repository.UserRepository;
import com.project.user.service.RoleService;
import com.project.user.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RoleService roleService;
    @Override
    public UserResponse registerUser(UserRequest userRequest){
        Users user = userRepository.save(userMapper.toEntity(userRequest));
        return userMapper.toResponse(user);
    }
    @Override
    public List<UserResponse> findAllUsers() {
        return  userRepository.findAll()
                .stream()
                .map(user -> userMapper.toResponse(user))
                .toList();
    }

    @Override
    public Optional<UserResponse> findUserById(long userId){
        return userRepository.findUserByUserId(userId)
                .map(user -> userMapper.toResponse(user));
    }
    @Override
    public Users findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);    }
    @Override
    public Users findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}