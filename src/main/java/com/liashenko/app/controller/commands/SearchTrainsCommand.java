package com.liashenko.app.controller.commands;

import com.google.gson.Gson;
import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.MsgSender;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import com.liashenko.app.controller.utils.exceptions.ControllerException;
import com.liashenko.app.controller.utils.exceptions.HttpParserException;
import com.liashenko.app.controller.utils.exceptions.ValidationException;
import com.liashenko.app.service.TrainSearchingService;
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

import static com.liashenko.app.controller.utils.Asserts.assertIsNull;
import static com.liashenko.app.controller.utils.Asserts.assertStringIsNullOrEmpty;

public class SearchTrainsCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(SearchTrainsCommand.class);

//    private static final Gson GSON = new Gson();
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
//            String jsonData = HttpParser.getJsonDataFromRequest(request);
//            RouteDto routeDto = getValidRouteFromJsonString(jsonData);
            Long fromStationId = HttpParser.getLongRequestParam(FROM_ID_ATTR, request).orElse(0L);
            Long toStationId = HttpParser.getLongRequestParam(TO_ID_ATTR, request).orElse(0L);
            String fromStationName = HttpParser.getStringRequestParam(FROM_ATTR, request);
            String toStationName = HttpParser.getStringRequestParam(TO_ATTR, request);
            String dateString = HttpParser.getStringRequestParam(DATE_ATTR, request);
            LocalDate dateTime = HttpParser.convertStringToDate(dateString);

//            Long fromStationId = routeDto.getFromId();
//            Long toStationId = routeDto.getToId();
//            String fromStationName = routeDto.getFrom();
//            String toStationName = routeDto.getTo();
//            LocalDate dateTime = HttpParser.convertStringToDate(routeDto.getDate());

            ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(currentLocaleStr);
            TrainSearchingService trainSearchingService = new TrainSearchingServiceImpl(localeQueries);
            Optional<List<TrainDto>> trainsOpt = trainSearchingService.getTrainsForTheRouteOnDate(fromStationId,
                    fromStationName, toStationId, toStationName, dateTime);

            request.setAttribute(FROM_ID_ATTR, fromStationId);
            request.setAttribute(FROM_ATTR, fromStationName);
            request.setAttribute(TO_ID_ATTR, toStationId);
            request.setAttribute(TO_ATTR, toStationName);
            request.setAttribute(DATE_ATTR, dateString);

            session.setAttribute(SessionAttrInitializer.FROM_STATION_ID_ATTR, fromStationId);
            session.setAttribute(SessionAttrInitializer.FROM_STATION_NAME_ATTR, fromStationName);
            session.setAttribute(SessionAttrInitializer.TO_STATION_ID_ATTR, toStationId);
            session.setAttribute(SessionAttrInitializer.TO_STATION_NAME_ATTR, toStationName);
            session.setAttribute(SessionAttrInitializer.DATE_ATTR, dateString);

//            session.setAttribute(SessionAttrInitializer.DATE_ATTR, routeDto.getDate());

            trainsOpt.ifPresent(trainDtos -> request.setAttribute(TRAINS_ATTR, trainDtos));

            if (trainsOpt.isPresent() && !trainsOpt.get().isEmpty()){
                request.setAttribute(TRAINS_ATTR, trainsOpt.get());
            } else {
                request.setAttribute(TRAINS_ATTR, null);
                request.setAttribute(IS_LIST_EMPTY, Boolean.TRUE);
            }

//            if (trainsOpt.isPresent()){
//                List<TrainDto> trains = trainsOpt.get();
//                if (!trains.isEmpty()) {
//                    MsgSender.sendJsonMsg(response, "", true);
//                } else {
//                    MsgSender.sendJsonMsg(response, "There isn't any trains on specified direction on required date", false);
//                }
//            }
            session.setAttribute(SessionAttrInitializer.DATE_ATTR, HttpParser.convertDateToHumanReadableString(dateTime));//??
            page = PageManagerConf.getInstance().getProperty(PageManagerConf.INDEX_PAGE_PATH);
        } catch (ControllerException | ServiceException e) {
            classLogger.error(e);
//            MsgSender.sendJsonMsg(response, "Error", false);
            page = PageManagerConf.getInstance().getProperty(PageManagerConf.ERROR_PAGE_PATH);
        }
        return page;
//        return PageManagerConf.getInstance().getProperty(PageManagerConf.EMPTY_RESULT);
    }

//    private RouteDto getValidRouteFromJsonString(String jsonString){
//        RouteDto routeDto;
//        try {
//            routeDto = GSON.fromJson(jsonString, RouteDto.class);
//        } catch (ClassCastException ex){
//            throw new ValidationException(ex.getMessage());
//        }
//        if (assertStringIsNullOrEmpty(routeDto.getFrom())
//                || assertStringIsNullOrEmpty(routeDto.getTo())
//                || assertIsNull(routeDto.getFromId())
//                || assertIsNull(routeDto.getToId())
//                || assertStringIsNullOrEmpty(routeDto.getDate())) {
//            throw new ValidationException("Object is not valid") ;
//        }
//
//        return routeDto;
//    }
}
