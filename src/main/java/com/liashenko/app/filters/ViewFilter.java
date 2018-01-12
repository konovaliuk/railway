package com.liashenko.app.filters;

import com.liashenko.app.controller.RequestHelper;
import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import com.liashenko.app.persistance.domain.Role;
import com.liashenko.app.service.UserProfileService;
import com.liashenko.app.service.dto.RoleDto;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.implementation.UserProfileServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

@WebFilter(urlPatterns = {
        RequestHelper.USERS_PAGE_URL_ATTR,
        RequestHelper.LOGIN_PAGE_URL_ATTR,
        RequestHelper.BILL_PAGE_URL_ATTR,
        RequestHelper.REGISTRATION_PAGE_URL_ATTR,
        RequestHelper.PROFILE_PAGE_URL_ATTR,
        RequestHelper.ORDER_TICKET_PAGE_URL_ATTR,
        RequestHelper.INDEX_PAGE_URL_ATTR,
        RequestHelper.SEARCH_TRAINS_URL_ATTR,
        RequestHelper.ORDERS_PAGE_URL_ATTR
},
        filterName = "ViewFilter",
        description = "Filter for all views")
public class ViewFilter implements Filter {

    private static final Logger classLogger = LogManager.getLogger(ViewFilter.class);
    private static final String LANGUAGE_ATTR = "lang";

//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(true);
//        System.out.println("servletPath : "+ req.getServletPath());

        if (session.isNew()) {//check it!!!
            chain.doFilter(req, response);
        } else {
            localeProcessing(req, resp, chain, session);
        }
    }

    private static void localeProcessing(HttpServletRequest req, HttpServletResponse resp, FilterChain chain, HttpSession session)
            throws IOException, ServletException {
        String page = req.getServletPath();
        String lang = HttpParser.getStringRequestParam(LANGUAGE_ATTR, req);

        if (!lang.isEmpty()) {
            session.setAttribute(SessionAttrInitializer.USER_LOCALE, lang);
            Long userRoleId = HttpParser.getLongSessionAttr(SessionAttrInitializer.USER_CURRENT_ROLE, session).orElse(RoleDto.GUEST_ROLE_ID);
            if (userRoleId != RoleDto.GUEST_ROLE_ID) {
                Long userId = HttpParser.getLongSessionAttr(SessionAttrInitializer.USER_ID, session).orElse(0L);
                try {
                    ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(lang);
                    UserProfileService userProfileService = new UserProfileServiceImpl(localeQueries);
                    userProfileService.changeLanguage(userId, lang);
                } catch (ServiceException ex) {
                    classLogger.error(ex);
                }
            }

            //вызов страницы ответа на запрос
            req.getServletContext()
                    .getRequestDispatcher(HttpParser.getStringSessionAttr(SessionAttrInitializer.USER_LAST_PAGE, session))
                    .forward(req, resp);
        } else {
//            System.out.println("ViewFilter.localeProcessing: " + page);
            session.setAttribute(SessionAttrInitializer.USER_LAST_PAGE, page);
            chain.doFilter(req, resp);
        }
    }
}
