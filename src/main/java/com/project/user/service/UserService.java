package com.project.user.service;


import com.project.user.dto.request.UserRequest;
import com.project.user.dto.response.UserResponse;
import com.project.user.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserResponse> findAllUsers();
    Optional<UserResponse> findUserById(long id);
    UserResponse registerUser(UserRequest userRequest);
}
