package com.liashenko.app.controller.utils;

import com.liashenko.app.controller.RequestHelper;
import com.liashenko.app.persistance.domain.Role;
import com.liashenko.app.utils.AppProperties;

import javax.servlet.http.HttpSession;

import static com.liashenko.app.controller.utils.Asserts.assertStringIsNullOrEmpty;

public abstract class SessionAttrInitializer {

    public static final String USER_ID = "USER_ID";

    public static final String USER_CURRENT_ROLE = "USER_CURRENT_ROLE";
    public static final String USER_LOCALE = "USER_LOCALE";
    public static final String USER_LAST_PAGE = "USER_LAST_PAGE";
    public static final String ADMIN_ROLE_ATTR = "ADMIN_ROLE_ATTR";
    public static final String USER_ROLE_ATTR = "USER_ROLE_ATTR";
    public static final String GUEST_ROLE_ATTR = "GUEST_ROLE_ATTR";
    public static final String USERS_ON_PAGE_ATTR = "USERS_ON_PAGE_ATTR";

    public static final String FROM_STATION_ID_ATTR = "FROM_STATION_ID_ATTR";
    public static final String TO_STATION_ID_ATTR = "TO_STATION_ID_ATTR";
    public static final String FROM_STATION_NAME_ATTR = "FROM_STATION_NAME_ATTR";
    public static final String TO_STATION_NAME_ATTR = "TO_STATION_NAME_ATTR";
    public static final String DATE_ATTR = "DATE_ATTR";

    public static final String TRAIN_NAME_ATTR = "TRAIN_NAME_ATTR";
    public static final String ROUTE_ID_ATTR = "ROUTE_ID_ATTR";
    public static final String TRAIN_ID_ATTR = "TRAIN_ID_ATTR";

    public static final String VAGON_TYPE_ID_ATTR = "vagonTypeId";
    public static final String FIRST_NAME_ATTR = "firstName";
    public static final String LAST_NAME_ATTR = "lastName";

    public static void newSessionInit(HttpSession session, String userLocale) {
        if (assertStringIsNullOrEmpty(userLocale)) {
            newSessionInit(session);
        } else {
            session.setAttribute(USER_LOCALE, userLocale);
            defaultAttributesSetter(session);
        }
    }

    public static void newSessionInit(HttpSession session) {
        session.setAttribute(USER_LOCALE, AppProperties.getDefaultLocaleStr());
        defaultAttributesSetter(session);
    }

    private static void defaultAttributesSetter(HttpSession session) {
        session.setAttribute(USER_LAST_PAGE, RequestHelper.INDEX_PAGE_URL_ATTR);

        session.setAttribute(USER_CURRENT_ROLE, Role.GUEST_ROLE_ID);
        session.setAttribute(ADMIN_ROLE_ATTR, Role.ADMIN_ROLE_ID);
        session.setAttribute(USER_ROLE_ATTR, Role.USER_ROLE_ID);
        session.setAttribute(GUEST_ROLE_ATTR, Role.GUEST_ROLE_ID);
    }
}
