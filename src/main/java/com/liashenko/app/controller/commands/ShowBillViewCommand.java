package com.liashenko.app.controller.commands;

import com.liashenko.app.controller.manager.LocaleQueryConf;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.utils.HttpParser;
import com.liashenko.app.controller.utils.SessionAttrInitializer;
import com.liashenko.app.controller.utils.exceptions.ControllerException;
import com.liashenko.app.service.BillService;
import com.liashenko.app.service.exceptions.ServiceException;
import com.liashenko.app.service.implementation.BillServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

public class ShowBillViewCommand implements ICommand {
    private static final Logger classLogger = LogManager.getLogger(ShowBillViewCommand.class);

    private static final String FIRST_NAME_ATTR = "firstName";
    private static final String LAST_NAME_ATTR = "lastName";
    private static final String VAGON_TYPE_ID_ATTR = "vagonTypeId";
    private static final String BILL_ATTR = "bill";

    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String page = null;
        HttpSession session = request.getSession(true);

        try {
            //validation method
            String localStr = HttpParser.getStringSessionAttr(SessionAttrInitializer.USER_LOCALE, session);

            Long routeId = HttpParser.getLongSessionAttr(SessionAttrInitializer.ROUTE_ID_ATTR, session).orElse(0L);
            Long trainId = HttpParser.getLongSessionAttr(SessionAttrInitializer.TRAIN_ID_ATTR, session).orElse(0L);
            Long fromStationId = HttpParser.getLongSessionAttr(SessionAttrInitializer.FROM_STATION_ID_ATTR, session).orElse(0L);
            Long toStationId = HttpParser.getLongSessionAttr(SessionAttrInitializer.TO_STATION_ID_ATTR, session).orElse(0L);
            String trainName = HttpParser.getStringSessionAttr(SessionAttrInitializer.TRAIN_NAME_ATTR, session);
            String date = HttpParser.getStringSessionAttr(SessionAttrInitializer.DATE_ATTR, session);

            String firstName = HttpParser.getStrAttrFromRequestAndSetToSession(request, FIRST_NAME_ATTR,
                    SessionAttrInitializer.FIRST_NAME_ATTR);
            String lastName = HttpParser.getStrAttrFromRequestAndSetToSession(request, LAST_NAME_ATTR,
                    SessionAttrInitializer.LAST_NAME_ATTR);

            Integer vagonTypeId = HttpParser.getIntAttrFromRequestOrSessionOrDefaultAndSetToSession(request, VAGON_TYPE_ID_ATTR,
                    SessionAttrInitializer.VAGON_TYPE_ID_ATTR, 0);

            ResourceBundle localeQueries = LocaleQueryConf.getInstance().getLocalQueries(localStr);
            BillService billService = new BillServiceImpl(localeQueries);
            billService.showBill(routeId, trainId, fromStationId, toStationId,
                    trainName, firstName, lastName, vagonTypeId, date)
                    .ifPresent(billDto -> request.setAttribute(BILL_ATTR, billDto));

            page = PageManagerConf.getInstance().getProperty(PageManagerConf.BILL_PAGE_PATH);
        } catch (ControllerException | ServiceException e) {
            classLogger.error(e);
            page = PageManagerConf.getInstance().getProperty(PageManagerConf.ERROR_PAGE_PATH);
        }
        return page;
    }
}
