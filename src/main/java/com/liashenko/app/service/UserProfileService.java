package com.liashenko.app.service;

import com.liashenko.app.service.dto.AdminViewDto;

public interface UserProfileService {

    void createProfile(AdminViewDto adminViewDto);

    void changeLanguage(Long userId, String newLanguage);

    boolean isEmailExists(String email);

//    delete profileProfile(Long id);
//    showProfile();
//    updateProfile();
}
