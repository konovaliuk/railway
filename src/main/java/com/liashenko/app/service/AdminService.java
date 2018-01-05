package com.liashenko.app.service;

import com.liashenko.app.persistance.domain.Role;
import com.liashenko.app.persistance.domain.User;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    Optional<List<User>> showUsers(int rowsPerPage, int offset);

    Optional<List<Role>> showRoles();

    void updateUserInfo(User user);

    Integer getUsersCount();
}
