package com.liashenko.app.controller.commands;

import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.MessageManagerConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.SessionParamsInitializer;
import com.liashenko.app.controller.utils.exceptions.HttpParserException;
import com.liashenko.app.controller.utils.exceptions.SendMsgException;
import com.liashenko.app.controller.utils.exceptions.ValidationException;
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

import static com.liashenko.app.controller.commands.UsersCommand.ERROR_MSG_ATTR;

public class SearchTrainsCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(SearchTrainsCommand.class);
//    private static final Gson GSON = new Gson();

    private static final String FROM_ID_ATTR = "fromId";
    private static final String TO_ID_ATTR = "toId";
    private static final String FROM_ATTR = "from";
    private static final String TO_ATTR = "to";
    private static final String DATE_ATTR = "date";
    private static final String TRAINS_ATTR = "trains";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String page;

        String currentLocaleStr =  HttpParser.getStringSessionAttr(SessionParamsInitializer.USER_LOCALE, request.getSession());
        ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(currentLocaleStr);
        TrainSearchingService trainSearchingService = new TrainSearchingServiceImpl(localeQueries);
        HttpSession session = request.getSession();
        try {
            Long fromStationId = HttpParser.getLongRequestParam(FROM_ID_ATTR, request).orElseThrow(HttpParserException::new);
            Long toStationId = HttpParser.getLongRequestParam(TO_ID_ATTR, request).orElseThrow(HttpParserException::new);
//            Long fromStationId = 1L;
//            Long toStationId = 14L;

            String fromStationName = HttpParser.getStringRequestParam(FROM_ATTR, request);//
            String toStationName = HttpParser.getStringRequestParam(TO_ATTR, request);//
            LocalDate dateTime = HttpParser.getDateTimeRequestParam(DATE_ATTR, request).orElseThrow(HttpParserException::new);

            Optional<List<TrainDto>> trainsOpt = trainSearchingService.getTrainsForTheRouteOnDate(fromStationId,
                    fromStationName,  toStationId, toStationName, dateTime);

            trainsOpt.ifPresent(trainDtos -> request.setAttribute(TRAINS_ATTR, trainDtos));

            session.setAttribute(SessionParamsInitializer.FROM_STATION_ID_ATTR, fromStationId);
            session.setAttribute(SessionParamsInitializer.FROM_STATION_NAME_ATTR, fromStationName);
            session.setAttribute(SessionParamsInitializer.TO_STATION_ID_ATTR, toStationId);
            session.setAttribute(SessionParamsInitializer.TO_STATION_NAME_ATTR, toStationName);
            session.setAttribute(SessionParamsInitializer.DATE_ATTR, HttpParser.convertDateToString(dateTime));

            page = PageManagerConf.getInstance().getProperty(PageManagerConf.INDEX_PAGE_PATH);
        } catch (ValidationException | SendMsgException | ServiceException e) {
            classLogger.error(e.getMessage());
            request.setAttribute(ERROR_MSG_ATTR,
                    MessageManagerConf.getInstance().getProperty(MessageManagerConf.LOGIN_ERROR_MESSAGE));
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
