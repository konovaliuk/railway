package com.liashenko.app.filters;

import com.liashenko.app.controller.RequestHelper;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {
        RequestHelper.USERS_PAGE_URL_ATTR,
//        RequestHelper.LOGIN_PAGE_URL_ATTR,
        RequestHelper.BILL_PAGE_URL_ATTR,
//        RequestHelper.REGISTRATION_PAGE_URL_ATTR,
        RequestHelper.PROFILE_PAGE_URL_ATTR,
        RequestHelper.ORDER_TICKET_PAGE_URL_ATTR,
        RequestHelper.INDEX_PAGE_URL_ATTR,
        RequestHelper.SEARCH_TRAINS_URL_ATTR,
        RequestHelper.ORDERS_PAGE_URL_ATTR
}, filterName = "PageBeforeLoginFilter", description = "Filter for all views (except login and registration) to memorize last page before entering")
public class PageBeforeLoginFilter implements Filter {
    private static final Logger classLogger = LogManager.getLogger(PageBeforeLoginFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(true);

        if (!session.isNew()) {
            String page = req.getServletPath();
            session.setAttribute(SessionAttrInitializer.USER_PAGE_BEFORE_LOGIN, page);
        }
        chain.doFilter(req, response);
    }
}
