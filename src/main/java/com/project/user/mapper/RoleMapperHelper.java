package com.project.user.mapper;

import com.project.user.service.RoleService;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleMapperHelper {
    private final RoleService roleService;

    public RoleMapperHelper(RoleService roleService) {
        this.roleService = roleService;
    }

    @Named("roleIdToRoleName")
    public String roleIdToRoleName(Long roleId) {
        return roleService.getRoleNameByRoleId(roleId).orElse("UNKNOWN");
    }
}
