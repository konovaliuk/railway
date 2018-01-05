package com.liashenko.app.controller.utils;

import com.liashenko.app.controller.utils.exceptions.HttpParserException;
import com.liashenko.app.utils.AppProperties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.DateFormatter;
import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;

public abstract class HttpParser {


    public static Locale parseLocaleFromRequest(HttpServletRequest request){
        String localStr =  HttpParser.getStringSessionAttr(SessionParamsInitializer.USER_LOCALE, request.getSession());
        return parseLocaleFromString(localStr);
    }

    public static Locale parseLocaleFromString(String str){
        if (str != null && !str.isEmpty()) {
            String[] localStrArr = str.trim().split("_");
            return  (localStrArr.length == 2)
                    ? new Locale(localStrArr[0], localStrArr[1]) : new Locale(AppProperties.getDefaultLocaleStr());
        }
        return new Locale(AppProperties.getDefaultLocaleStr());
    }

    public static String getStringRequestParam(String param, HttpServletRequest request){
        if (param == null) throw new HttpParserException("Wrong request parameter");
        String extractedParam = request.getParameter(param);
        return (extractedParam == null) ?  "" : extractedParam.trim();
    }

    public static Optional<Integer> getIntRequestParam(String param, HttpServletRequest request){
        try {
           return Optional.ofNullable(Integer.valueOf(getStringRequestParam(param,request)));
        } catch (NumberFormatException ignore) {
            return Optional.empty();
        }
    }

    public static Optional<LocalDate> getDateTimeRequestParam(String param, HttpServletRequest request){
        String timeStr = getStringRequestParam(param, request);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            return Optional.ofNullable(LocalDate.parse(timeStr, formatter));
        } catch (DateTimeParseException ignore) {
            return Optional.empty();
        }
    }

    public static String convertDateTimeToString(LocalDateTime dateTime){
        if (dateTime == null) return "";
        String timeStr = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm");
        try {
            return dateTime.format(formatter);
        } catch (DateTimeParseException ignore) {
            return timeStr;
        }
    }

    public static String convertDateToString(LocalDate date){
        if (date == null) return "";
        String timeStr = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        try {
            return date.format(formatter);
        } catch (DateTimeParseException ignore) {
            return timeStr;
        }
    }

    public static String convertDateToForDbString(LocalDate date){
        if (date == null) return "";
        StringBuilder timeStr = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return timeStr.append("\"").append(date.format(formatter)).append("\"").toString();
        } catch (DateTimeParseException ignore) {
            return "";
        }
    }

    public static Optional<Long> getLongRequestParam(String param, HttpServletRequest request){
        try {
            return Optional.ofNullable(Long.valueOf(getStringRequestParam(param, request)));
        } catch (NumberFormatException ignore) {
            return Optional.empty();
        }
    }

    public static String getStringSessionAttr(String sessionAttribute, HttpSession session) {
        if (sessionAttribute == null || session == null) throw new HttpParserException("Wrong method parameters");
        if (session.getAttribute(sessionAttribute) != null) {
        return session.getAttribute(sessionAttribute).toString().trim();

        }
        return "";
    }

    public static Optional<Long> getLongSessionAttr(String sessionAttribute, HttpSession session) {
        if (sessionAttribute == null || session == null) throw new HttpParserException("Wrong method parameters");
        try {
            if (session.getAttribute(sessionAttribute) != null) {
                return Optional.ofNullable((Long) session.getAttribute(sessionAttribute));
            }
        } catch (NumberFormatException ex){
            throw new HttpParserException(ex.getMessage());
        }
        return Optional.empty();
    }

    public static Optional<Integer> getIntSessionAttr(String sessionAttribute, HttpSession session) {
        if (sessionAttribute == null || session == null) throw new HttpParserException("Wrong method parameters");
        try {
            if (session.getAttribute(sessionAttribute) != null) {
                return Optional.ofNullable((Integer) session.getAttribute(sessionAttribute));
            }
        } catch (NumberFormatException ex){
            throw new HttpParserException(ex.getMessage());
        }
        return Optional.empty();
    }

    public static String getJsonDataFromRequest(HttpServletRequest request) throws IOException{
        String jsonData;
        StringBuilder sb = new StringBuilder();
        while ((jsonData = request.getReader().readLine()) != null) {
            sb.append(jsonData);
        }
        return sb.toString();
    }

//    public static boolean getBoolSessionAttribute(String sessionAttribute, HttpSession session) {
//        if (session.getAttribute(sessionAttribute) != null) {
//            return (boolean)session.getAttribute(sessionAttribute);
//        }
//        return false;
//    }


}
