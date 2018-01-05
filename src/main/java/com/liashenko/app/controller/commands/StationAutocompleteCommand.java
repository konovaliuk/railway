package com.liashenko.app.controller.commands;

import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.MsgSender;
import com.liashenko.app.controller.utils.SessionParamsInitializer;
import com.liashenko.app.controller.utils.exceptions.SendMsgException;
import com.liashenko.app.service.TrainSearchingService;
import com.liashenko.app.service.dto.AutocompleteDto;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.implementation.TrainSearchingServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class StationAutocompleteCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(SignInCommand.class);

    private static final String AUTOCOMPLETE_ATTR  = "autocomplete";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String currentLocaleStr =  HttpParser.getStringSessionAttr(SessionParamsInitializer.USER_LOCALE, request.getSession());
        ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(currentLocaleStr);

        TrainSearchingService trainSearchingService = new TrainSearchingServiceImpl(localeQueries);
        try {
//            System.out.println("request");
            String stationLike = HttpParser.getStringRequestParam(AUTOCOMPLETE_ATTR, request);
//            System.out.println(">>>>>> " + stationLike);
            List<AutocompleteDto> stations = trainSearchingService.getStationAutocomplete(stationLike).orElse(Collections.emptyList());
//            System.out.println(">>>>>>>> " + stations.toString());
            MsgSender.sendJsonMsg(response, stations);
        } catch (SendMsgException | ServiceException e) {
            classLogger.error(e.getMessage());
            MsgSender.sendJsonMsg(response, null);
        }
        return PageManagerConf.getInstance().getProperty(PageManagerConf.EMPTY_RESULT);
    }
}
