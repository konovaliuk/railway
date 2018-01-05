package com.liashenko.app.controller.commands;

import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.MessageManagerConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.SessionParamsInitializer;
import com.liashenko.app.controller.utils.exceptions.ValidationException;
import com.liashenko.app.service.OrderService;
import com.liashenko.app.service.dto.FullRouteDto;
import com.liashenko.app.service.dto.PriceForVagonDto;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.implementation.OrderServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.taglibs.standard.extra.spath.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.liashenko.app.controller.commands.UsersCommand.ERROR_MSG_ATTR;

public class OrderTicketCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(OrderTicketCommand.class);

    public static final String ROUTE_ID_ATTR = "routeId";
    public static final String TRAIN_ID_ATTR = "trainId";

    private static final String ROUTE_ATTR = "route";
    private static final String PRICES_ATTR = "prices";
    public static final String TRAIN_ATTR = "train";
    public static final String PRICES_LIST_SIZE_ATTR = "list_size";
    public static final String FROM_STATION_NAME_ATTR = "from_station_name";
    public static final String TO_STATION_NAME_ATTR = "to_station_name";

    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String page = null;
        HttpSession session = request.getSession();

        String localStr =  HttpParser.getStringSessionAttr(SessionParamsInitializer.USER_LOCALE, session);
        ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(localStr);
        OrderService orderService = new OrderServiceImpl(localeQueries);

        try {
            //validation method
            Long routeId = HttpParser.getLongRequestParam(ROUTE_ID_ATTR, request).orElse(0L);
            if (routeId == 0L) {
                routeId =  HttpParser.getLongSessionAttr(SessionParamsInitializer.ROUTE_ID_ATTR, session)
                                 .orElseThrow(() -> new ValidationException("routeId is absent"));
            }

            Long trainId = HttpParser.getLongRequestParam(TRAIN_ID_ATTR, request).orElse(0L);
            if (trainId == 0L) {
               trainId = HttpParser.getLongSessionAttr(SessionParamsInitializer.TRAIN_ID_ATTR, session)
                        .orElseThrow(() -> new ValidationException("trainId is absent"));
            }

            String trainName = HttpParser.getStringRequestParam(TRAIN_ATTR, request);
            if (!trainName.isEmpty()) {
                session.setAttribute(SessionParamsInitializer.TRAIN_NAME_ATTR, trainName);
            }

//            String trainName = orderService.getTrainNameById(trainId).orElse("");
            Long fromStationId = HttpParser.getLongSessionAttr(SessionParamsInitializer.FROM_STATION_ID_ATTR, session)
                    .orElseThrow(() -> new ValidationException("fromStationId is absent"));
            Long toStationId = HttpParser.getLongSessionAttr(SessionParamsInitializer.TO_STATION_ID_ATTR, session)
                    .orElseThrow(() -> new ValidationException("toStationId is absent"));

            Optional<FullRouteDto> fullRouteOpt = orderService.getFullTrainRoute(routeId);
            Optional<List<PriceForVagonDto>> pricesForVagonsOpt = orderService.getPrices(fromStationId, toStationId, trainId);

            String fromStationName = orderService.getStationNameById(fromStationId).orElse("");
            String toStationName = orderService.getStationNameById(toStationId).orElse("");
            request.setAttribute(FROM_STATION_NAME_ATTR, fromStationName);
            request.setAttribute(TO_STATION_NAME_ATTR, toStationName);

            fullRouteOpt.ifPresent(route -> request.setAttribute(ROUTE_ATTR, route));
            pricesForVagonsOpt.ifPresent(prices -> {
                request.setAttribute(PRICES_ATTR, prices);
                request.setAttribute(PRICES_LIST_SIZE_ATTR, prices.size());
            });

            session.setAttribute(SessionParamsInitializer.ROUTE_ID_ATTR, routeId);
            session.setAttribute(SessionParamsInitializer.TRAIN_ID_ATTR, trainId);

            page = PageManagerConf.getInstance().getProperty(PageManagerConf.ORDER_TICKET_PAGE_PATH);
        } catch (ValidationException | ServiceException e) {
            classLogger.error(e.getMessage());
            request.setAttribute(ERROR_MSG_ATTR,
                    MessageManagerConf.getInstance().getProperty(MessageManagerConf.LOGIN_ERROR_MESSAGE));
            page = PageManagerConf.getInstance().getProperty(PageManagerConf.ERROR_PAGE_PATH);
        }
        return page;
    }
}
