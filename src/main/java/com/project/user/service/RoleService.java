package com.project.user.service;

import com.project.user.entity.Roles;
import com.project.user.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public interface RoleService {
    Optional<String> getRoleNameByRoleId(long roleId);
}
