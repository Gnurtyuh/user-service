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

import java.util.ArrayList;
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
    public Users registerUser(UserRequest userRequest){
        return userRepository.save(userMapper.toEntity(userRequest));
    }
    @Override
    public List<UserResponse> findAllUsers() {
        List<Users> users = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();
        for (Users user : users) {
            userResponseList.add(userMapper.toResponse(user,roleService));
        }
        return userResponseList;
    }

    @Override
    public Optional<UserResponse> findUserById(long userId){
        return userRepository.findUserByUserId(userId)
                .map(user -> userMapper.toResponse(user,roleService));
    }
}