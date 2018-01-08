<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.USER_LOCALE}"/>
<fmt:setBundle basename="views_locales.order_ticket" var="lang"/>

<!doctype html>
<html lang="ua">
<head>
    <c:import url="../colon/css_dependencies.jsp" charEncoding="utf-8"/>
    <title><fmt:message key="page.title" bundle="${lang}"/></title>

</head>
<body>

<c:import url="../colon/navbar.jsp" charEncoding="utf-8"/>
<div class="container">
    <div class="page-header">
        <br>
        <h2><fmt:message key="header.part1" bundle="${lang}"/> <c:out value="${sessionScope.TRAIN_NAME_ATTR}"/> <c:out
                value="${route.firstTerminalStation}"/>-<c:out value="${route.lastTerminalStation}"/> <fmt:message
                key="header.part2" bundle="${lang}"/> <c:out value="${sessionScope.DATE_ATTR}"/></h2>
    </div>
</div>

<div class="container">
    <div class="row">
        <form class="form-horizontal col-md-12" action="#" method="POST" id="order_form" name="order_form">
            <fieldset>
                <div class="row">
                    <div class="table-responsive col-md-12">
                        <table class="table table-bordered table-striped" id="train_table">
                            <thead>
                            <tr>
                                <th><fmt:message key="route.table.col" bundle="${lang}"/></th>
                                <th><fmt:message key="vagon.type.table.col" bundle="${lang}"/></th>
                                <th><fmt:message key="price.table.col" bundle="${lang}"/></th>
                                <th><fmt:message key="choose.table.col" bundle="${lang}"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${prices}" var="priceForVagon" varStatus="loopCount">
                                <tr><c:if test="${loopCount.index eq 0}">
                                    <td rowspan="${list_size}" align="left"><c:out value="${from_station_name}"
                                                                                   default="" escapeXml="true"/>-<c:out
                                            value="${to_station_name}" default="" escapeXml="true"/></td>
                                </c:if>
                                    <td align="left"><c:out value="${priceForVagon.vagonTypeName}" default=""
                                                            escapeXml="true"/></td>
                                    <td align="left"><c:out value="${priceForVagon.ticketPrice}" default=""
                                                            escapeXml="true"/></td>
                                    <td align="left"><input type="radio" name="radio_btn"
                                                            value="${priceForVagon.vagonTypeId}" <c:if
                                            test="${loopCount.index eq 0}"> checked </c:if> /></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </fieldset>
            <div class="container">
                <div class="row">
                    <div class="form-group col-lg-2 col-md-2 col-sm-2">
                        <input type="userName" class="form-control input-lg" name="first_name" id="first_name" value=""
                               placeholder="<fmt:message key="firstname.input.placeholder" bundle = "${lang}"/>">
                    </div>

                    <div class="form-group col-lg-2 col-md-2 col-sm-2">
                        <input type="userName" class="form-control input-lg" name="last_name" id="last_name" value=""
                               placeholder="<fmt:message key="lastname.input.placeholder" bundle = "${lang}"/>">
                    </div>
                </div>
            </div>
            <div class="text-right form-group btn-group-lg">
                <a href="#" role="button" onclick="goToBill();" class="btn btn-success pull-right"><fmt:message
                        key="order.button" bundle="${lang}"/></a>
            </div>

        </form>
    </div>
</div>
<br><br>

<c:import url="../colon/footer.jsp" charEncoding="utf-8"/>
</body>
<c:import url="../colon/js_dependencies.jsp" charEncoding="utf-8"/>
</html>
<script>

    //  function chooseVagonType(id) {
    //       $('#choosenVagon').val(id);
    //       alert(id);
    //
    //      $("#submit").click(function() {
    //          var val = $('input[name=radio_btn]:checked').val();
    //          alert(val);
    //      });
    //  }

    function goToBill() {
        var vagonTypeId = $('input[name=radio_btn]:checked').val();
        var firstName = $('#first_name').val();
        var lastname = $('#last_name').val();

        location.href = "<c:url value='/'/>bill?vagonTypeId=" + vagonTypeId + "&firstName=" + firstName + "&lastName=" + lastname;
    }

</script>

