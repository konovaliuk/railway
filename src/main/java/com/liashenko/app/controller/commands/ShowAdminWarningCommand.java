package com.liashenko.app.controller.commands;

import com.liashenko.app.authorization.Authorization;
import com.liashenko.app.controller.RequestHelper;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.service.dto.RoleDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Authorization.Allowed(roles = {RoleDto.ADMIN_ROLE_ID}, defAction = RequestHelper.INDEX_PAGE_URL_ATTR)
public class ShowAdminWarningCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(ShowAdminWarningCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return PageManagerConf.getInstance().getProperty(PageManagerConf.ADMIN_WARNING_PAGE_PATH);
    }
}
