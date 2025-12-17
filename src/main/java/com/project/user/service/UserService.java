package com.project.user.service;


import com.project.user.dto.request.UserRequest;
import com.project.user.dto.response.UserResponse;
import com.project.user.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponse> findAllUsers();
    Optional<UserResponse> findUserById(long userId);
    UserResponse registerUser(UserRequest userRequest);
    Users findUserByEmail(String email);
    Users findUserByUsername(String username);
}
