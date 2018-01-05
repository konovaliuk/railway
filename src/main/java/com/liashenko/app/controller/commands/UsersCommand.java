package com.liashenko.app.controller.commands;


import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.manager.MessageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.SessionParamsInitializer;
import com.liashenko.app.persistance.domain.Role;
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
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UsersCommand implements ICommand {

    private static final Logger classLogger = LogManager.getLogger(UsersCommand.class);
    public static final String OFFSET_ATTR = "offset";
    public static final String COUNT_ATTR = "count";
    public static final String USERS_ON_PAGE_ATTR = "usersOnPage";
    public static final String ERROR_MSG_ATTR = "errorMessage";
    public static final String USERS_LIST_ATTR = "users";
    public static final String ROLES_LIST_ATTR = "roles";
    private static final int USERS_ON_PAGE_DEFAULT = AppProperties.getDefaultUsersOnPageCount();


    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String page = null;

        String localStr =  HttpParser.getStringSessionAttr(SessionParamsInitializer.USER_LOCALE, request.getSession());
        ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(localStr);

        AdminService adminService = new AdminServiceImpl(localeQueries);

        Optional<List<User>> usersListOpt;
        Optional<List<Role>> rolesListOpt;
        Integer offset  = HttpParser.getIntRequestParam(OFFSET_ATTR, request).orElse(0);
        Integer usersOnPage = HttpParser.getIntRequestParam(USERS_ON_PAGE_ATTR, request)
                .orElse(HttpParser.getIntSessionAttr(SessionParamsInitializer.USERS_ON_PAGE_ATTR, request.getSession())
                        .orElse(USERS_ON_PAGE_DEFAULT));

        try {
            Integer count = adminService.getUsersCount();
            usersListOpt = adminService.showUsers(usersOnPage, offset);
            usersListOpt.ifPresent(users -> request.setAttribute(USERS_LIST_ATTR, users));

            rolesListOpt = adminService.showRoles();
            rolesListOpt.ifPresent(roles -> request.setAttribute(ROLES_LIST_ATTR, roles));

            request.getSession().setAttribute(SessionParamsInitializer.USERS_ON_PAGE_ATTR, usersOnPage);
            request.setAttribute(OFFSET_ATTR, offset);
            request.setAttribute(COUNT_ATTR, count);
            request.setAttribute(USERS_ON_PAGE_ATTR, usersOnPage);

            page = PageManagerConf.getInstance().getProperty(PageManagerConf.USERS_PAGE_PATH);
        } catch (ServiceException e) {
            classLogger.error(e.getMessage());
            request.setAttribute(ERROR_MSG_ATTR,
                    MessageManagerConf.getInstance().getProperty(MessageManagerConf.LOGIN_ERROR_MESSAGE));
            page = PageManagerConf.getInstance().getProperty(PageManagerConf.ERROR_PAGE_PATH);
        }
        return page;
    }
}
