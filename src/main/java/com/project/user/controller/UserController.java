package com.project.user.controller;

import com.project.user.dto.request.UserRequest;
import com.project.user.dto.response.UserResponse;
import com.project.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<?>> allUser() {
        return ResponseEntity.ok(userService.findAllUsers());
    }
    @PostMapping()
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest) {
        UserResponse response = userService.registerUser(userRequest);
        return ResponseEntity.created(URI.create("/users/"+response.getUserId())).body(response);
    }
    @PostMapping("/{userId}")
    public ResponseEntity<UserResponse> findUserByUserId(@PathVariable long userId) {
        return userService.findUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
