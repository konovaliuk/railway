package com.liashenko.app.controller;


import com.liashenko.app.controller.commands.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class RequestHelper {

    public static final String USERS_PAGE_URL_ATTR = "/users";
    public static final String LOGIN_PAGE_URL_ATTR = "/login";
    public static final String BILL_PAGE_URL_ATTR = "/bill";
    public static final String REGISTRATION_PAGE_URL_ATTR = "/registration";
    public static final String PROFILE_PAGE_URL_ATTR = "/profile";
    public static final String ORDER_TICKET_PAGE_URL_ATTR = "/order_ticket";
    public static final String INDEX_PAGE_URL_ATTR = "/index.jsp";
    public static final String SIGN_OUT_BUTTON_URL_ATTR = "/sign_out";
    public static final String NEW_USER_PROFILE_BUTTON_AJAX_ATTR = "/ajax/new_user_profile";
    public static final String UPDATE_USER_BUTTON_AJAX_ATTR = "/ajax/update_user";
    public static final String SIGN_IN_BUTTON_AJAX_ATTR = "/ajax/sign_in";
    public static final String STATION_AUTOCOMPLETE_AJAX_ATTR = "/ajax/station_advice";
    public static final String SEARCH_TRAINS_URL_ATTR = "/search_trains";


     private static RequestHelper instance = null;
     private HashMap<String, ICommand> commands = new HashMap<>();

     private RequestHelper(){
         //заполнение таблицы командами
         commands.put(USERS_PAGE_URL_ATTR, new UsersCommand());//+
         commands.put(LOGIN_PAGE_URL_ATTR, new LoginCommand());//+
         commands.put(BILL_PAGE_URL_ATTR, new BillCommand());//-
//         commands.put("error", new UsersCommand());
         commands.put(REGISTRATION_PAGE_URL_ATTR, new RegistrationCommand());//+
         commands.put(PROFILE_PAGE_URL_ATTR, new RegistrationCommand());//-
         commands.put(ORDER_TICKET_PAGE_URL_ATTR, new OrderTicketCommand());//+
         commands.put(INDEX_PAGE_URL_ATTR, new IndexCommand());//+
         commands.put(SIGN_OUT_BUTTON_URL_ATTR, new SignOutCommand());//+
         commands.put(NEW_USER_PROFILE_BUTTON_AJAX_ATTR, new NewUserCommand());//+
         commands.put(UPDATE_USER_BUTTON_AJAX_ATTR, new UpdateUserCommand());//+
         commands.put(SIGN_IN_BUTTON_AJAX_ATTR, new SignInCommand());//+
         commands.put(STATION_AUTOCOMPLETE_AJAX_ATTR, new StationAutocompleteCommand());//+
         commands.put(SEARCH_TRAINS_URL_ATTR, new SearchTrainsCommand());
     }

    //создание единственного объекта по шаблону Singleton
    public static RequestHelper getInstance(){
        if (instance == null){
            instance = new RequestHelper();
        }
        return instance;
    }

     public ICommand getCommand(HttpServletRequest request){
         //извлечение команды из запроса
         String action = request.getServletPath();
         //получение объекта, соответствующего команде
         ICommand command = commands.get(action);
         if (command == null){
             //если команды не существует в текущем объекте
             command = new NoCommand();
         }
         return command;
     }
}
