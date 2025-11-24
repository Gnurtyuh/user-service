package com.project.user.service.impl;
import com.project.user.entity.Roles;
import com.project.user.repository.RoleRepository;
import com.project.user.service.RoleService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public Optional<String> getRoleNameByRoleId(long roleId) {
        return roleRepository.findById(roleId)
                .map(Roles::getRoleName);
    }
}