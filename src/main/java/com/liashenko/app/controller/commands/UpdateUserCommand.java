package com.liashenko.app.controller.commands;

import com.google.gson.Gson;
import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.MsgSender;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import com.liashenko.app.controller.utils.exceptions.ControllerException;
import com.liashenko.app.controller.utils.exceptions.SendMsgException;
import com.liashenko.app.controller.utils.exceptions.ValidationException;
import com.liashenko.app.persistance.domain.User;
import com.liashenko.app.service.AdminService;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.implementation.AdminServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

import static com.liashenko.app.controller.utils.Asserts.assertIsNull;


public class UpdateUserCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(UpdateUserCommand.class);
    private static final Gson GSON = new Gson();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        try {
            String currentLocaleStr = HttpParser.getStringSessionAttr(SessionAttrInitializer.USER_LOCALE, session);
            String jsonData = HttpParser.getJsonDataFromRequest(request);
            ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(currentLocaleStr);
            AdminService adminService = new AdminServiceImpl(localeQueries);
            adminService.updateUserInfo(getValidUserFromJsonString(jsonData));
            MsgSender.sendJsonMsg(response, "User is updated", true);
        } catch (ControllerException | ServiceException e) {
            classLogger.error(e);
            MsgSender.sendJsonMsg(response, "User is not updated", false);
        }
        return PageManagerConf.getInstance().getProperty(PageManagerConf.EMPTY_RESULT);
    }

    private User getValidUserFromJsonString(String jsonString) {
        User user;
        try {
            user = GSON.fromJson(jsonString, User.class);
        } catch (ClassCastException ex) {
            throw new ValidationException(ex.getMessage());
        }
        if (assertIsNull(user.getRoleId()) || assertIsNull(user.getBanned())) {
            throw new ValidationException("Object is not valid");
        }
        return user;
    }
}
