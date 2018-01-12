package com.liashenko.app.controller.commands;

import com.liashenko.app.authorization.Authorization;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.service.dto.RoleDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Authorization.Allowed(roles = {RoleDto.GUEST_ROLE_ID})
public class ShowLoginViewCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(ShowLoginViewCommand.class);
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return PageManagerConf.getInstance().getProperty(PageManagerConf.LOGIN_PAGE_PATH);
    }
}
