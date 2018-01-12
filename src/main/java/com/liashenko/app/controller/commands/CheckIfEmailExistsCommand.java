package com.liashenko.app.controller.commands;

import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.MsgSender;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import com.liashenko.app.controller.utils.exceptions.ControllerException;
import com.liashenko.app.service.UserProfileService;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.implementation.UserProfileServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

public class CheckIfEmailExistsCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(CheckIfEmailExistsCommand.class);

    private static final String EMAIL_ATTR =  "email";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
        String currentLocaleStr = HttpParser.getStringSessionAttr(SessionAttrInitializer.USER_LOCALE, request.getSession());
        ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(currentLocaleStr);
        UserProfileService userProfileService = new UserProfileServiceImpl(localeQueries);
        String email = HttpParser.getStringRequestParam(EMAIL_ATTR, request);
            if (userProfileService.isEmailExists(email)) {
                MsgSender.sendJsonMsg(response, "{\"valid\" : false }");
            } else {
                MsgSender.sendJsonMsg(response, "{\"valid\" : true }");
            }
        } catch (ControllerException | ServiceException e) {
            classLogger.error(e);
            MsgSender.sendJsonMsg(response, "{\"valid\" : true }");
        }
        return PageManagerConf.getInstance().getProperty(PageManagerConf.EMPTY_RESULT);
    }
}