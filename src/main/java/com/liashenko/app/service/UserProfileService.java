package com.liashenko.app.service;

import com.liashenko.app.service.dto.UserDto;

import java.util.Optional;

public interface UserProfileService {

    void createProfile(UserDto userDto);

    void changeLanguage(Long userId, String newLanguage);

    boolean isEmailExists(String email);

    Optional<UserDto> getUserById(Long userId);

    void banUserProfile(Long userId);

    void updateProfile(UserDto userDto);

    boolean isOtherUsersWithEmailExist(Long userId, String email);
}
