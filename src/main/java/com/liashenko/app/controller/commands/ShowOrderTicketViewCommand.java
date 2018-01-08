package com.liashenko.app.controller.commands;

import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import com.liashenko.app.controller.utils.exceptions.ControllerException;
import com.liashenko.app.service.OrderService;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.implementation.OrderServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

public class ShowOrderTicketViewCommand implements ICommand {
    private static final String ERROR_MSG_ATTR = "errorMessage";
    private static final String ROUTE_ID_ATTR = "routeId";
    private static final String TRAIN_ID_ATTR = "trainId";
    private static final String TRAIN_ATTR = "train";
    private static final String PRICES_LIST_SIZE_ATTR = "list_size";
    private static final String FROM_STATION_NAME_ATTR = "from_station_name";
    private static final String TO_STATION_NAME_ATTR = "to_station_name";
    private static final Logger classLogger = LogManager.getLogger(ShowOrderTicketViewCommand.class);
    private static final String ROUTE_ATTR = "route";
    private static final String PRICES_ATTR = "prices";

    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String page = null;
        HttpSession session = request.getSession();

        try {
            String localStr = HttpParser.getStringSessionAttr(SessionAttrInitializer.USER_LOCALE, session);
            ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(localStr);
            OrderService orderService = new OrderServiceImpl(localeQueries);

            Long routeId = HttpParser.getLongAttrFromRequestOrSessionOrDefaultAndSetToSession(request, ROUTE_ID_ATTR,
                    SessionAttrInitializer.ROUTE_ID_ATTR, 0L);
            HttpParser.getLongAttrFromRequestOrSessionOrDefaultAndSetToSession(request, TRAIN_ID_ATTR,
                    SessionAttrInitializer.TRAIN_ID_ATTR, 0L);
            HttpParser.getStrAttrFromRequestAndSetToSession(request, TRAIN_ATTR, SessionAttrInitializer.TRAIN_NAME_ATTR);

            Long fromStationId = HttpParser.getLongSessionAttr(SessionAttrInitializer.FROM_STATION_ID_ATTR, session).orElse(0L);
            Long toStationId = HttpParser.getLongSessionAttr(SessionAttrInitializer.TO_STATION_ID_ATTR, session).orElse(0L);

            request.setAttribute(FROM_STATION_NAME_ATTR, orderService.getStationNameById(fromStationId).orElse(""));
            request.setAttribute(TO_STATION_NAME_ATTR, orderService.getStationNameById(toStationId).orElse(""));

            orderService.getFullTrainRoute(routeId).ifPresent(route -> request.setAttribute(ROUTE_ATTR, route));
            orderService.getPricesForVagons(fromStationId, toStationId, routeId).ifPresent(prices -> {
                request.setAttribute(PRICES_ATTR, prices);
                request.setAttribute(PRICES_LIST_SIZE_ATTR, prices.size());
            });

            page = PageManagerConf.getInstance().getProperty(PageManagerConf.ORDER_TICKET_PAGE_PATH);
        } catch (ControllerException | ServiceException e) {
            classLogger.error(e);
            page = PageManagerConf.getInstance().getProperty(PageManagerConf.ERROR_PAGE_PATH);
        }
        return page;
    }
}
