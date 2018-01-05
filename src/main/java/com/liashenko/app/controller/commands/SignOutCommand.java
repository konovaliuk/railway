package com.liashenko.app.controller.commands;

import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.SessionParamsInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SignOutCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(SignOutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        if (!session.isNew()){
            String userLocale =  HttpParser.getStringSessionAttr(SessionParamsInitializer.USER_LOCALE, session);
            session.invalidate();
            SessionParamsInitializer.newSessionInit(request.getSession(true), userLocale);
        } else {
            SessionParamsInitializer.newSessionInit(session);
        }

        return PageManagerConf.getInstance().getProperty(PageManagerConf.INDEX_PAGE_PATH);
    }
}