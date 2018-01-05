package com.liashenko.app.controller.commands;

import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.MessageManagerConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.SessionParamsInitializer;
import com.liashenko.app.controller.utils.exceptions.ValidationException;
import com.liashenko.app.service.BillService;
import com.liashenko.app.service.dto.BillDto;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.implementation.BillServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.liashenko.app.controller.commands.UsersCommand.ERROR_MSG_ATTR;

public class BillCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(BillCommand.class);

    private static final String VAGON_TYPE_ID_ATTR = "vagonTypeId";
    private static final String FIRST_NAME_ATTR = "firstName";
    private static final String LAST_NAME_ATTR = "lastName";
    private static final String BILL_ATTR = "bill";

    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String page = null;
        HttpSession session = request.getSession();

        String localStr =  HttpParser.getStringSessionAttr(SessionParamsInitializer.USER_LOCALE, session);
        ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(localStr);
        BillService billService = new BillServiceImpl(localeQueries);

        try {
            //validation method
            Long routeId =  HttpParser.getLongSessionAttr(SessionParamsInitializer.ROUTE_ID_ATTR, session)
                    .orElseThrow(() -> new ValidationException("trainId is absent"));

            Long trainId =  HttpParser.getLongSessionAttr(SessionParamsInitializer.TRAIN_ID_ATTR, session)
                    .orElseThrow(() -> new ValidationException("trainId is absent"));

            Long fromStationId = HttpParser.getLongSessionAttr(SessionParamsInitializer.FROM_STATION_ID_ATTR, session)
                    .orElseThrow(() -> new ValidationException("fromStationId is absent"));

            Long toStationId = HttpParser.getLongSessionAttr(SessionParamsInitializer.TO_STATION_ID_ATTR, session)
                    .orElseThrow(() -> new ValidationException("toStationId is absent"));

            String trainName = HttpParser.getStringSessionAttr(SessionParamsInitializer.TRAIN_NAME_ATTR, session);
//            System.out.println(">>>> train name " + trainName);

            String date = HttpParser.getStringSessionAttr(SessionParamsInitializer.DATE_ATTR, session);

            String firstName = HttpParser.getStringRequestParam(FIRST_NAME_ATTR, request);
            if (!firstName.isEmpty()) {
                session.setAttribute(SessionParamsInitializer.FIRST_NAME_ATTR, firstName);
            }

            String lastName = HttpParser.getStringRequestParam(LAST_NAME_ATTR, request);
            if (!lastName.isEmpty()) {
                session.setAttribute(SessionParamsInitializer.LAST_NAME_ATTR, lastName);
            }

            Integer vagonTypeId = HttpParser.getIntRequestParam(VAGON_TYPE_ID_ATTR, request).orElse(0);
            if (vagonTypeId == 0){
                vagonTypeId = HttpParser.getIntSessionAttr(SessionParamsInitializer.VAGON_TYPE_ID_ATTR, session)
                        .orElseThrow(() -> new ValidationException("vagonTypeId is absent"));
            }

            Optional<BillDto> billDtoOpt =  billService.showBill(routeId, trainId, fromStationId, toStationId,
                    trainName, firstName, lastName, vagonTypeId, date);

            billDtoOpt.ifPresent(billDto -> request.setAttribute(BILL_ATTR, billDto));
            session.setAttribute(SessionParamsInitializer.VAGON_TYPE_ID_ATTR, vagonTypeId);

            page = PageManagerConf.getInstance().getProperty(PageManagerConf.BILL_PAGE_PATH);
        } catch (ValidationException | ServiceException e) {
            classLogger.error(e.getMessage());
            request.setAttribute(ERROR_MSG_ATTR,
                    MessageManagerConf.getInstance().getProperty(MessageManagerConf.LOGIN_ERROR_MESSAGE));
            page = PageManagerConf.getInstance().getProperty(PageManagerConf.ERROR_PAGE_PATH);
        }
        return page;
    }
}
