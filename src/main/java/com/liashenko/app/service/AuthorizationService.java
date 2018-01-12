package com.liashenko.app.service;

import com.liashenko.app.service.dto.PrinciplesDto;
import com.liashenko.app.service.dto.UserSessionProfileDto;

import java.util.Optional;

public interface AuthorizationService {

    Optional<UserSessionProfileDto> logIn(PrinciplesDto principlesDto);
}
