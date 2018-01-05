<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" language="java"%>
<fmt:setLocale value = "${sessionScope.USER_LOCALE}"/>
<fmt:setBundle basename="views_locales.error" var="lang"/>


<!doctype html>
<html lang="ua">

        <head>
            <title><fmt:message key="page.title" bundle="${lang}"/></title>
        </head>
        <body>
            <h3><fmt:message key="page.header" bundle="${lang}"/></h3>
           <hr/>
           <jsp:expression>
     (request.getAttribute("errorMessage") != null)
     ? (String) request.getAttribute("errorMessage")
               : "unknown error"</jsp:expression>
           <hr/>
           <a href="<c:url value='/'/>"><fmt:message key="index.page.ref" bundle="${lang}"/></a>
        </body>
    </html>
