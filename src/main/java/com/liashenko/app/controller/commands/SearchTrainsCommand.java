package com.liashenko.app.controller.commands;

import com.liashenko.app.authorization.Authorization;
import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import com.liashenko.app.controller.utils.exceptions.ControllerException;
import com.liashenko.app.service.TrainSearchingService;
import com.liashenko.app.service.dto.RoleDto;
import com.liashenko.app.service.dto.RouteDto;
import com.liashenko.app.service.dto.TrainDto;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.implementation.TrainSearchingServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

@Authorization.Allowed(roles = {RoleDto.GUEST_ROLE_ID, RoleDto.USER_ROLE_ID, RoleDto.ADMIN_ROLE_ID})
public class SearchTrainsCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(SearchTrainsCommand.class);

//    private static final Gson GSON = new Gson();
    public static final String USER_ROUTE = "userRoute";
    private static final String FROM_ID_ATTR = "fromId";
    private static final String TO_ID_ATTR = "toId";
    private static final String FROM_ATTR = "from";
    private static final String TO_ATTR = "to";
    private static final String DATE_ATTR = "date";
    private static final String TRAINS_ATTR = "trains";
    private static final String IS_LIST_EMPTY = "isListEmpty";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page;
        HttpSession session = request.getSession(true);

        try {
            String currentLocaleStr = HttpParser.getStringSessionAttr(SessionAttrInitializer.USER_LOCALE, session);
            RouteDto routeDto = RouteDto.builder()
                    .fromStationId(HttpParser.getLongRequestParam(FROM_ID_ATTR, request).orElse(0L))
                    .fromStationName(HttpParser.getStringRequestParam(FROM_ATTR, request))
                    .toStationId(HttpParser.getLongRequestParam(TO_ID_ATTR, request).orElse(0L))
                    .toStationName(HttpParser.getStringRequestParam(TO_ATTR, request))
                    .dateString(HttpParser.getStringRequestParam(DATE_ATTR, request))
                    .build();

//            LocalDate dateTime = HttpParser.convertStringToDate(dateString);

            ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(currentLocaleStr);
            TrainSearchingService trainSearchingService = new TrainSearchingServiceImpl(localeQueries);
//            Optional<List<TrainDto>> trainsOpt = trainSearchingService.getTrainsForTheRouteOnDate(fromStationId,
//                    fromStationName, toStationId, toStationName, dateTime);
            Optional<List<TrainDto>> trainsOpt = trainSearchingService.getTrainsForTheRouteOnDate(routeDto);

            request.setAttribute(USER_ROUTE, routeDto);
            session.setAttribute(SessionAttrInitializer.USER_ROUTE, routeDto);
//            request.setAttribute(FROM_ID_ATTR, fromStationId);
//            request.setAttribute(FROM_ATTR, fromStationName);
//            request.setAttribute(TO_ID_ATTR, toStationId);
//            request.setAttribute(TO_ATTR, toStationName);
//            request.setAttribute(DATE_ATTR, dateString);

//            session.setAttribute(SessionAttrInitializer.FROM_STATION_ID_ATTR, fromStationId);
//            session.setAttribute(SessionAttrInitializer.FROM_STATION_NAME_ATTR, fromStationName);
//            session.setAttribute(SessionAttrInitializer.TO_STATION_ID_ATTR, toStationId);
//            session.setAttribute(SessionAttrInitializer.TO_STATION_NAME_ATTR, toStationName);
//            session.setAttribute(SessionAttrInitializer.DATE_ATTR, dateString);

            trainsOpt.ifPresent(trainDtos -> request.setAttribute(TRAINS_ATTR, trainDtos));

            if (trainsOpt.isPresent() && !trainsOpt.get().isEmpty()){
                request.setAttribute(TRAINS_ATTR, trainsOpt.get());
            } else {
                request.setAttribute(TRAINS_ATTR, null);
                request.setAttribute(IS_LIST_EMPTY, Boolean.TRUE);
            }

//            session.setAttribute(SessionAttrInitializer.DATE_ATTR, HttpParser.convertDateToHumanReadableString(dateTime));//??
            page = PageManagerConf.getInstance().getProperty(PageManagerConf.INDEX_PAGE_PATH);
        } catch (ControllerException | ServiceException e) {
            classLogger.error(e);
            page = PageManagerConf.getInstance().getProperty(PageManagerConf.ERROR_PAGE_PATH);
        }
        return page;
    }
}
