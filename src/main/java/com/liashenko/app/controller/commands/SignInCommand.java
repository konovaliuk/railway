package com.liashenko.app.controller.commands;

import com.google.gson.Gson;
import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.MsgSender;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import com.liashenko.app.controller.utils.Validator;
import com.liashenko.app.controller.utils.exceptions.ControllerException;
import com.liashenko.app.controller.utils.exceptions.SendMsgException;
import com.liashenko.app.controller.utils.exceptions.ValidationException;
import com.liashenko.app.service.AuthorizationService;
import com.liashenko.app.service.dto.PrinciplesDto;
import com.liashenko.app.service.dto.UserSessionProfileDto;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.implementation.AuthorizationServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.liashenko.app.controller.utils.Asserts.assertIsNull;

public class SignInCommand implements ICommand {

    private static final Logger classLogger = LogManager.getLogger(SignInCommand.class);
    private static final Gson GSON = new Gson();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);

        try {
            String currentLocaleStr = HttpParser.getStringSessionAttr(SessionAttrInitializer.USER_LOCALE, session);
            ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(currentLocaleStr);
            AuthorizationService authorizationService = new AuthorizationServiceImpl(localeQueries);
            String jsonData = HttpParser.getJsonDataFromRequest(request);
            Optional<UserSessionProfileDto> userProfileOpt = authorizationService.logIn(getValidPrinciples(jsonData));

            if (userProfileOpt.isPresent()) {
                request.getSession().setAttribute(SessionAttrInitializer.USER_ID, userProfileOpt.get().getUserId());
                request.getSession().setAttribute(SessionAttrInitializer.USER_CURRENT_ROLE, userProfileOpt.get().getUserRoleId());
                request.getSession().setAttribute(SessionAttrInitializer.USER_LOCALE, userProfileOpt.get().getLanguage());
                MsgSender.sendJsonMsg(response, "You logged in successful", true);
            } else {
                MsgSender.sendJsonMsg(response, "There is no user with such password or e-mail", false);
            }

        } catch (ControllerException | ServiceException e) {
            classLogger.error(e);
            MsgSender.sendJsonMsg(response, "Sorry, we got an error", false);
        }
        return PageManagerConf.getInstance().getProperty(PageManagerConf.EMPTY_RESULT);
    }

    private PrinciplesDto getValidPrinciples(String jsonString) {
        PrinciplesDto principlesDto;
        try {
            principlesDto = GSON.fromJson(jsonString, PrinciplesDto.class);
        } catch (ClassCastException ex) {
            classLogger.error(ex);
            throw new ValidationException(ex.getMessage());
        }
        if (assertIsNull(principlesDto.getPassword())) {
            throw new ValidationException("Object is not valid");
        }
        return new PrinciplesDto(Validator.validateEmail(principlesDto.getEmail()), principlesDto.getPassword());
    }
}
