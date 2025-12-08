package com.project.user.repository;

import com.project.user.dto.response.UserResponse;
import com.project.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUserByUserId(long id);
    Users findUserByEmail(String email);
    Users findUserByUsername(String username);

}
