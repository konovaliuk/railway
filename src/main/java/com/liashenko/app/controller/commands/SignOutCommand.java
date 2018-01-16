package com.liashenko.app.controller.commands;

import com.liashenko.app.authorization.Authorization;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import com.liashenko.app.service.dto.RoleDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Authorization.Allowed(roles = {RoleDto.USER_ROLE_ID, RoleDto.ADMIN_ROLE_ID})
public class SignOutCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(SignOutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(Boolean.TRUE);
        if (!session.isNew()) {
            session.invalidate();
        } else {
            String userLocale = HttpParser.getStringSessionAttr(SessionAttrInitializer.USER_LOCALE, session);
            SessionAttrInitializer.newSessionInit(session, userLocale);
            SessionAttrInitializer.newSessionInit(session);
        }
        return PageManagerConf.getInstance().getProperty(PageManagerConf.INDEX_PAGE_PATH);
    }
}
