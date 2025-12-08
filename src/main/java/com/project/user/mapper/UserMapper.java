package com.project.user.mapper;

import com.project.user.dto.request.UserRequest;
import com.project.user.dto.response.UserResponse;
import com.project.user.entity.Users;
import com.project.user.service.RoleService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Optional;

@Mapper(componentModel = "spring", uses = RoleMapperHelper.class)
public interface UserMapper {

    Users toEntity(UserRequest userRequest);
    @Mapping(source = "roleId", target = "roleName", qualifiedByName = "roleIdToRoleName")
    UserResponse toResponse(Users users);
}
