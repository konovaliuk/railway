package com.liashenko.app.controller.commands;

import com.google.gson.Gson;
import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.MsgSender;
import com.liashenko.app.controller.utils.SessionParamsInitializer;
import com.liashenko.app.controller.utils.exceptions.SendMsgException;
import com.liashenko.app.controller.utils.exceptions.ValidationException;
import com.liashenko.app.persistance.domain.User;
import com.liashenko.app.service.AdminService;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.implementation.AdminServiceImpl;
import com.liashenko.app.utils.AppProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.liashenko.app.controller.utils.Asserts.assertIsNull;


public class UpdateUserCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(UpdateUserCommand.class);
    private static final Gson GSON = new Gson();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String currentLocaleStr =  HttpParser.getStringSessionAttr(SessionParamsInitializer.USER_LOCALE, request.getSession());
        ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(currentLocaleStr);

        String jsonData = HttpParser.getJsonDataFromRequest(request);
        AdminService adminService = new AdminServiceImpl(localeQueries);

        try {
            adminService.updateUserInfo(getValidUserFromJsonString(jsonData));
            MsgSender.sendJsonMsg(response,"User is updated", true);
        } catch (ValidationException | SendMsgException | ServiceException e) {
            classLogger.error(e.getMessage());
            MsgSender.sendJsonMsg(response,"User is not updated", false);
        }
        return PageManagerConf.getInstance().getProperty(PageManagerConf.EMPTY_RESULT);
    }

    private User getValidUserFromJsonString(String jsonString){
        User user;
        try {
            user = GSON.fromJson(jsonString, User.class);
        } catch (ClassCastException ex){
            throw new ValidationException(ex.getMessage());
        }
        if (assertIsNull(user.getRoleId()) || assertIsNull(user.getBanned())) {
            throw new ValidationException("Object is not valid") ;
        }
        return user;
    }
}
