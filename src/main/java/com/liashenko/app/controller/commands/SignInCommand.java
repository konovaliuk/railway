package com.liashenko.app.controller.commands;

import com.google.gson.Gson;
import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.manager.MessageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.MsgSender;
import com.liashenko.app.controller.utils.SessionParamsInitializer;
import com.liashenko.app.controller.utils.Validator;
import com.liashenko.app.controller.utils.exceptions.SendMsgException;
import com.liashenko.app.controller.utils.exceptions.ValidationException;
import com.liashenko.app.service.AuthorizationService;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.dto.PrinciplesDto;
import com.liashenko.app.service.dto.UserSessionProfileDto;
import com.liashenko.app.service.implementation.AuthorizationServiceImpl;
import com.liashenko.app.utils.AppProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.liashenko.app.controller.commands.UsersCommand.ERROR_MSG_ATTR;
import static com.liashenko.app.controller.utils.Asserts.assertIsNull;
import static com.liashenko.app.controller.utils.Asserts.assertStringIsNullOrEmpty;

public class SignInCommand implements ICommand {

    private static final Logger classLogger = LogManager.getLogger(SignInCommand.class);
    private static final Gson GSON = new Gson();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String currentLocaleStr =  HttpParser.getStringSessionAttr(SessionParamsInitializer.USER_LOCALE, request.getSession());
        ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(currentLocaleStr);

        AuthorizationService authorizationService = new AuthorizationServiceImpl(localeQueries);
        String jsonData = HttpParser.getJsonDataFromRequest(request);

        try {
            Optional<UserSessionProfileDto> userProfile = authorizationService.logIn(getValidPrinciples(jsonData));

            if (userProfile.isPresent()) {
                request.getSession().setAttribute(SessionParamsInitializer.USER_ID, userProfile.get().getUserId());
                request.getSession().setAttribute(SessionParamsInitializer.USER_CURRENT_ROLE, userProfile.get().getUserRoleId());
                request.getSession().setAttribute(SessionParamsInitializer.USER_LOCALE, userProfile.get().getLanguage());
            } else {
                MsgSender.sendJsonMsg(response,"There is no user with such password or e-mail", false);
            }

            MsgSender.sendJsonMsg(response,"You logged in successful", true);
        } catch (ValidationException | SendMsgException | ServiceException e) {
            classLogger.error(e.getMessage());
            MsgSender.sendJsonMsg(response,"Sorry, we got an error", false);
        }
        return PageManagerConf.getInstance().getProperty(PageManagerConf.EMPTY_RESULT);
    }

    private PrinciplesDto getValidPrinciples(String jsonString){
        PrinciplesDto principlesDto;
        try{
            principlesDto = GSON.fromJson(jsonString, PrinciplesDto.class);
        } catch (ClassCastException ex){
            throw new ValidationException(ex.getMessage());
        }
        if (assertIsNull(principlesDto.getPassword())) {
            throw new ValidationException("Object is not valid") ;
        }
        return new PrinciplesDto(Validator.validateEmail(principlesDto.getEmail()), principlesDto.getPassword());
    }
}
