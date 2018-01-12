package com.liashenko.app.controller;

import com.liashenko.app.authorization.RightsChecker;
import com.liashenko.app.controller.commands.*;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import com.liashenko.app.service.dto.RoleDto;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class RequestHelper {
    //common commands
    public static final String DEFAULT_ACTION = "/default";
    public static final String ERROR_ACTION = "/error";
    public static final String INDEX_PAGE_URL_ATTR = "/index.jsp";

    //"index" view commands
    public static final String ORDER_TICKET_PAGE_URL_ATTR = "/order_ticket";
    public static final String SEARCH_TRAINS_URL_ATTR = "/search_trains";
    public static final String STATION_AUTOCOMPLETE_AJAX_ATTR = "/ajax/station_advice";

    //"orders" view commands
    public static final String BILL_PAGE_URL_ATTR = "/bill";

    //"profile" views command
    public static final String UPDATE_PROFILE_AJAX_ATTR = "/ajax/update_user_profile";
    public static final String CHECK_IF_USER_WITH_EMAIL_EXISTS_AJAX_ATTR = "/ajax/check_user_email";

    //"users" view command
    public static final String UPDATE_USER_BUTTON_AJAX_ATTR = "/ajax/update_user";

    //"login" view command
    public static final String SIGN_IN_BUTTON_AJAX_ATTR = "/ajax/sign_in";

    //"registration" view command
    public static final String CHECK_IF_EMAIL_IS_EXISTS_AJAX_ATTR = "/ajax/check_email";
    public static final String NEW_USER_PROFILE_BUTTON_AJAX_ATTR = "/ajax/new_user_profile";

    //"bill" view commands

    //navbar commands
    //guest's commands
    public static final String REGISTRATION_PAGE_URL_ATTR = "/registration";
    public static final String LOGIN_PAGE_URL_ATTR = "/login";

    //user's commands
    public static final String ORDERS_PAGE_URL_ATTR = "/orders";

    //admin's commands
    public static final String USERS_PAGE_URL_ATTR = "/users";

    //user's & admin's commands
    public static final String PROFILE_PAGE_URL_ATTR = "/profile";
    public static final String SIGN_OUT_BUTTON_URL_ATTR = "/sign_out";


    private static volatile RequestHelper instance;
    private static final RightsChecker RIGHTS_CHECKER = RightsChecker.getInstance();
    private HashMap<String, ICommand> commands;

    private RequestHelper() {
        commands = new HashMap<>();
        //заполнение таблицы командами
        commands.put(CHECK_IF_EMAIL_IS_EXISTS_AJAX_ATTR, new CheckIfEmailExistsCommand());//+
        commands.put(USERS_PAGE_URL_ATTR, new ShowUsersViewCommand());//+
        commands.put(LOGIN_PAGE_URL_ATTR, new ShowLoginViewCommand());//+
        commands.put(BILL_PAGE_URL_ATTR, new ShowBillViewCommand());//+
        commands.put(REGISTRATION_PAGE_URL_ATTR, new ShowRegistrationViewCommand());//+
        commands.put(PROFILE_PAGE_URL_ATTR, new ShowProfileViewCommand());//+
        commands.put(ORDERS_PAGE_URL_ATTR, new ShowOrdersViewCommand());//-
        commands.put(ORDER_TICKET_PAGE_URL_ATTR, new ShowOrderTicketViewCommand());//+
        commands.put(INDEX_PAGE_URL_ATTR, new ShowIndexViewCommand());//+
        commands.put(SIGN_OUT_BUTTON_URL_ATTR, new SignOutCommand());//+
        commands.put(NEW_USER_PROFILE_BUTTON_AJAX_ATTR, new RegisterNewUserCommand());//+
        commands.put(UPDATE_USER_BUTTON_AJAX_ATTR, new UpdateUserByAdminCommand());//+
        commands.put(SIGN_IN_BUTTON_AJAX_ATTR, new SignInCommand());//+
        commands.put(STATION_AUTOCOMPLETE_AJAX_ATTR, new StationAutocompleteCommand());//+
        commands.put(SEARCH_TRAINS_URL_ATTR, new SearchTrainsCommand());//+
        commands.put(UPDATE_PROFILE_AJAX_ATTR, new UpdateProfileCommand());//+
        commands.put(CHECK_IF_USER_WITH_EMAIL_EXISTS_AJAX_ATTR, new CheckIfOtherUsersWithEmailExist());//+
        commands.put(DEFAULT_ACTION, new NoCommand());//+
        commands.put(ERROR_ACTION, new ShowErrorViewCommand());//+
    }

    //создание единственного объекта по шаблону Singleton
    public static RequestHelper getInstance() {
        RequestHelper localInstance = instance;
        if (localInstance == null) {
            synchronized (RequestHelper.class){
                localInstance = instance;
                if (localInstance == null){
                    instance = localInstance = new RequestHelper();
                }
            }
        }
        return instance;
    }

    public synchronized ICommand getCommand(HttpServletRequest request) {
        String action = request.getServletPath();
        Long currentRole = HttpParser.getLongSessionAttr(SessionAttrInitializer.USER_CURRENT_ROLE, request.getSession())
                .orElse(RoleDto.GUEST_ROLE_ID);
        return (ICommand) RIGHTS_CHECKER.checkUserRightsAndGetCommand(action, DEFAULT_ACTION, currentRole, commands);
    }
}
