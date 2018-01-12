package com.liashenko.app.controller.commands;

import com.liashenko.app.controller.manager.PageManagerConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShowRegistrationViewCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(ShowRegistrationViewCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return PageManagerConf.getInstance().getProperty(PageManagerConf.REGISTRATION_PAGE_PATH);
    }
}
