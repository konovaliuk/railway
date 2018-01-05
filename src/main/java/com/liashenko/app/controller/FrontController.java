package com.liashenko.app.controller;

import com.liashenko.app.controller.commands.ICommand;
import com.liashenko.app.controller.manager.PageManagerConf;
import com.liashenko.app.controller.manager.MessageManagerConf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "FrontController", urlPatterns = {
        RequestHelper.USERS_PAGE_URL_ATTR,
        RequestHelper.LOGIN_PAGE_URL_ATTR,
        RequestHelper.BILL_PAGE_URL_ATTR,
        RequestHelper.REGISTRATION_PAGE_URL_ATTR,
        RequestHelper.PROFILE_PAGE_URL_ATTR,
        RequestHelper.ORDER_TICKET_PAGE_URL_ATTR,
        RequestHelper.INDEX_PAGE_URL_ATTR,
        RequestHelper.SIGN_OUT_BUTTON_URL_ATTR,
        RequestHelper.NEW_USER_PROFILE_BUTTON_AJAX_ATTR,
        RequestHelper.UPDATE_USER_BUTTON_AJAX_ATTR,
        RequestHelper.SIGN_IN_BUTTON_AJAX_ATTR,
        RequestHelper.STATION_AUTOCOMPLETE_AJAX_ATTR,
        RequestHelper.SEARCH_TRAINS_URL_ATTR

})
//@ServletSecurity(value = @HttpConstraint(transportGuarantee = ServletSecurity.TransportGuarantee.CONFIDENTIAL))
public class FrontController extends HttpServlet {

    private static final Logger classLogger = LogManager.getLogger(FrontController.class);
    private static final String ERROR_MESSAGE_ATTR = "errorMessage";


    //объект, содержащий список возможных команд
    private RequestHelper requestHelper = RequestHelper.getInstance();

    public FrontController(){
        super();
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String page = null;
        try {
            //определение команды, пришедшей из JSP
            ICommand ICommand = requestHelper.getCommand(request);
            /*вызов реализованного метода execute() интерфейса ICommand и передача параметров
             классу-обработчику конкретной команды*/
            page = ICommand.execute(request,response);
            //метод возвращает страницу ответа
        } catch (ServletException e) {
            classLogger.error(e);

            //генерация сообщения об ошибке
            request.setAttribute(ERROR_MESSAGE_ATTR,
                    MessageManagerConf.getInstance().getProperty(
                    MessageManagerConf.SERVLET_EXCEPTION_ERROR_MESSAGE));

            //вызов JSP-страницы с сообщением об ошибке
            page = PageManagerConf.getInstance().getProperty(PageManagerConf.ERROR_PAGE_PATH);
        } catch(IOException e) {
            classLogger.error(e);

            request.setAttribute(ERROR_MESSAGE_ATTR,
                    MessageManagerConf.getInstance().getProperty(MessageManagerConf.IO_EXCEPTION_ERROR_MESSAGE));
            page = PageManagerConf.getInstance().getProperty(PageManagerConf.ERROR_PAGE_PATH);
        }

        if (!PageManagerConf.EMPTY_RESULT.equals(page)) {
            //вызов страницы ответа на запрос
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
            dispatcher.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}