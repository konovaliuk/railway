package com.liashenko.app.controller.commands;

import com.google.gson.Gson;
import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.MsgSender;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import com.liashenko.app.controller.utils.Validator;
import com.liashenko.app.controller.utils.exceptions.ControllerException;
import com.liashenko.app.persistance.domain.Role;
import com.liashenko.app.service.UserProfileService;
import com.liashenko.app.service.dto.AdminViewDto;
import com.liashenko.app.service.dto.UserProfileDto;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.implementation.UserProfileServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

public class RegisterNewUserCommand implements ICommand {

    private static final Logger classLogger = LogManager.getLogger(RegisterNewUserCommand.class);
    private static final Gson GSON = new Gson();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String currentLocaleStr = HttpParser.getStringSessionAttr(SessionAttrInitializer.USER_LOCALE, request.getSession());
            ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(currentLocaleStr);
            UserProfileService userProfileService = new UserProfileServiceImpl(localeQueries);
            String jsonData = HttpParser.getJsonDataFromRequest(request);
            AdminViewDto adminViewDto = getValidUserDtoFromRequest(jsonData, request, currentLocaleStr);
            //better to do as a special command
            if (!userProfileService.isEmailExists(adminViewDto.getEmail())) {
                userProfileService.createProfile(adminViewDto);
                MsgSender.sendJsonMsg(response, "User profile is created", true);
            } else {
                MsgSender.sendJsonMsg(response, "Email already exists", false);
            }
        } catch (ControllerException | ServiceException e) {
            classLogger.error(e);
            MsgSender.sendJsonMsg(response, "User profile is not created", false);
        }
        return PageManagerConf.getInstance().getProperty(PageManagerConf.EMPTY_RESULT);
    }

    private AdminViewDto getValidUserDtoFromRequest(String jsonData, HttpServletRequest request, String currentLocaleStr) {
        UserProfileDto userProfileDto = GSON.fromJson(jsonData, UserProfileDto.class);
        return AdminViewDto.builder()
                .firstName(Validator.validateFirstName(userProfileDto.getFirstName()))
                .lastName(Validator.validateLastName(userProfileDto.getLastName()))
                .email(Validator.validateEmail(userProfileDto.getEmail()))
                .password(Validator.checkRawPasswordsOnEquivalence(userProfileDto.getPass(),
                        userProfileDto.getRepeatedPass()))
                .language(currentLocaleStr)
                .isBanned(false)
                .roleId(Role.USER_ROLE_ID)
                .language(HttpParser.getStringSessionAttr(SessionAttrInitializer.USER_LOCALE,
                        request.getSession()))
                .build();
    }
}
