package com.liashenko.app.service;

import com.liashenko.app.service.dto.RoleDto;
import com.liashenko.app.service.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    Optional<List<UserDto>> showUsers(int rowsPerPage, int offset);

    Optional<List<RoleDto>> showRoles();

    void updateUserInfo(UserDto userDto);

    Integer getUsersCount();
}
