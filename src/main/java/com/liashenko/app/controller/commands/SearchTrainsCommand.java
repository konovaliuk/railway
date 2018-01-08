package com.liashenko.app.controller.commands;

import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import com.liashenko.app.controller.utils.exceptions.ControllerException;
import com.liashenko.app.controller.utils.exceptions.HttpParserException;
import com.liashenko.app.service.TrainSearchingService;
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

public class SearchTrainsCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(SearchTrainsCommand.class);

    private static final String FROM_ID_ATTR = "fromId";
    private static final String TO_ID_ATTR = "toId";
    private static final String FROM_ATTR = "from";
    private static final String TO_ATTR = "to";
    private static final String DATE_ATTR = "date";
    private static final String TRAINS_ATTR = "trains";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page;
        HttpSession session = request.getSession(true);

        try {
            String currentLocaleStr = HttpParser.getStringSessionAttr(SessionAttrInitializer.USER_LOCALE, session);
            Long fromStationId = HttpParser.getLongRequestParam(FROM_ID_ATTR, request).orElse(0L);
            Long toStationId = HttpParser.getLongRequestParam(TO_ID_ATTR, request).orElse(0L);
            String fromStationName = HttpParser.getStringRequestParam(FROM_ATTR, request);
            String toStationName = HttpParser.getStringRequestParam(TO_ATTR, request);

            LocalDate dateTime = HttpParser.getDateTimeRequestParam(DATE_ATTR, request).orElseThrow(HttpParserException::new);//???

            ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(currentLocaleStr);
            TrainSearchingService trainSearchingService = new TrainSearchingServiceImpl(localeQueries);
            Optional<List<TrainDto>> trainsOpt = trainSearchingService.getTrainsForTheRouteOnDate(fromStationId,
                    fromStationName, toStationId, toStationName, dateTime);

            trainsOpt.ifPresent(trainDtos -> request.setAttribute(TRAINS_ATTR, trainDtos));

            session.setAttribute(SessionAttrInitializer.FROM_STATION_ID_ATTR, fromStationId);
            session.setAttribute(SessionAttrInitializer.FROM_STATION_NAME_ATTR, fromStationName);
            session.setAttribute(SessionAttrInitializer.TO_STATION_ID_ATTR, toStationId);
            session.setAttribute(SessionAttrInitializer.TO_STATION_NAME_ATTR, toStationName);

            session.setAttribute(SessionAttrInitializer.DATE_ATTR, HttpParser.convertDateToHumanReadableString(dateTime));//??

            page = PageManagerConf.getInstance().getProperty(PageManagerConf.INDEX_PAGE_PATH);
        } catch (ControllerException | ServiceException e) {
            classLogger.error(e);
            page = PageManagerConf.getInstance().getProperty(PageManagerConf.ERROR_PAGE_PATH);
        }
        return page;
    }

//    private User getValidUserFromJsonString(String jsonString){
//        User user;
//        try {
//            user = GSON.fromJson(jsonString, User.class);
//        } catch (ClassCastException ex){
//            throw new ValidationException(ex.getMessage());
//        }
//        if (assertIsNull(user.getRoleId()) || assertIsNull(user.getBanned())) {
//            throw new ValidationException("Object is not valid") ;
//        }
//        return user;
//    }
}
