<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" language="java"%>
<fmt:setLocale value = "${sessionScope.USER_LOCALE}"/>
<fmt:setBundle basename="views_locales.index" var="lang"/>

<!doctype html>
<html lang="ua">
<head>

<link rel="stylesheet" href="./res/css/pikaday.css">
    <c:import url="colon/css_dependencies.jsp" charEncoding="utf-8"/>

    <script src="./res/js/moment.js"></script>

    <title><fmt:message key="page.title" bundle="${lang}"/></title>
</head>
<body>
<c:import url="colon/navbar.jsp" charEncoding="utf-8"/>

<br>

<div class="container">
    <div class="jumbotron">
        <h2><fmt:message key="wishing.of.good.travelling.inscription" bundle = "${lang}"/></h2>
        <p><fmt:message key="choose.stations.inscription" bundle = "${lang}"/></p>

        <%--action="<c:url value='/'/>"--%>
        <form class="form-inline" action="search_trains" name="search_trains" method="POST">
            <div class="container">
                <div class="row">
                    <div class="row">
                    <%--<div class="container">--%>
                        <div class="form-group col-lg-4 col-md-4 col-sm-4">
                            <div class="input-group">
                                <input aria-describedby="sizing-addon1" type="text" class="form-control input-lg"
                                       name="from" id="from" value="${sessionScope.FROM_STATION_NAME_ATTR}" autocomplete="off" rel="autocomp1"
                                       placeholder="<fmt:message key="from.input.placeholder" bundle = "${lang}"/>"
                                       required>
                                <span class="input-group-addon" id="sizing-addon1"><i class="fa fa-train"></i></span>
                                <input type="hidden" id="fromId" name="fromId" value="${sessionScope.FROM_STATION_ID_ATTR}"/>
                            </div>
                        </div>

                        <div class="form-group col-lg-4 col-md-4 col-sm-4">
                            <div class="input-group">
                                <input aria-describedby="sizing-addon2" type="text" class="form-control input-lg" name="to" value="${sessionScope.TO_STATION_NAME_ATTR}"
                                       id="to" autocomplete="off" rel="autocomp2" placeholder="<fmt:message key="where.input.placeholder" bundle = "${lang}"/>" required>
                                <span class="input-group-addon" id="sizing-addon2"><i class="fa fa-train"></i></span>
                                <input type="hidden" id="toId" name="toId" value="${sessionScope.TO_STATION_ID_ATTR}"/>
                            </div>
                        </div>

                        <div class="form-group col-lg-3 col-md-3 col-sm-3">
                            <div class="input-group">
                                <input type="text" class="form-control input-lg" name="date" id="date" value="${sessionScope.DATE_ATTR}" autocomplete="off" placeholder="<fmt:message key="when.input.placeholder" bundle = "${lang}"/>" required>
                                <span aria-describedby="sizing-addon3" class="input-group-addon" id="sizing-addon3"><i class="fa fa-calendar"></i></span>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="row">
                    <br>
                    <div class="btn-group  btn-group-lg" role="group">
                        <button type="submit" class="btn btn-primary "><fmt:message key="search.button" bundle = "${lang}"/></button>
                    </div>
                </div>
            </div>

        </form>
    </div>
</div>


    <div class="container">
        <div class="row">
            <div class="table-responsive col-md-12">
                <table class="table table-bordered table-striped" id="train_table" >
                    <thead>
                    <tr>
                        <th ><fmt:message key="train.number.table.col" bundle="${lang}"/></th>
                        <th ><fmt:message key="route.table.col" bundle="${lang}"/></th>
                        <th ><fmt:message key="leaving.time.table.col" bundle="${lang}"/></th>
                        <th ><fmt:message key="arrival.time.table.col" bundle="${lang}"/></th>
                        <th ><fmt:message key="choose.table.col" bundle="${lang}"/></th>
                    </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${trains}" var="train" varStatus="loopCount" >
                            <tr>
                                <td align="left"><c:out value="${train.trainNumber}" default="" escapeXml="true" /></td>
                                <td align="left"><c:out value="${train.fromStation}-${train.toStation}" default="" escapeXml="true" /></td>
                                <td align="left"><c:out value="${train.leavingDate}" default="" escapeXml="true" /></td>
                                <td align="left"><c:out value="${train.arrivalDate}" default="" escapeXml="true" /></td>
                                <td align="left"><a class="btn btn-success" role="button" href='<c:url value="/"/>order_ticket?trainId=${train.trainId}&routeId=${train.routeId}&train=${train.trainNumber}'><fmt:message key="choose.table.col" bundle = "${lang}"/></a></td>
                            <tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <br><br>
<c:import url="colon/footer.jsp" charEncoding="utf-8"/>
</body>
<c:import url="colon/js_dependencies.jsp" charEncoding="utf-8"/>
<script src="./res/js/pikaday.js"></script>
<script src="./res/bootstrap/js/bootstrap3-typeahead.min.js"></script>

</html>

<script>


//     $("input[rel^='autocomp']").typeahead({
//        source: function (query, process) {
//            alert(query);
//            return $.post('/ajax/station_advice', {query: query}, function (data) {
//                return process(data.autocompleteWord);
//            });
//        }
//    ,
//        autoSelect: true
//    });


//working code, not remove as far!
//    $(function t() {
//        $("input[rel^='autocomp']").autocomplete({
//            type: "POST",
//            minLength: 2,
//            source: function(request, response) {
//                $.ajax({
//                    url:"ajax/station_advice",
//                    dataType:"json",
//                    data: {
//                        autocomplete: request.term
//                    },
//                    success : function(data) {
//                        response($.map(data, function(item) {
//                            return {
//                                label:item.autocompleteWord,
//                                id:item.id
//                            }
//                        }));
//                    }
//                });
//            },
//            focus: function (e, t) {
//                $(this).val(t.item.label);
//                return false;
//            },
//            select: function (e, t) {
//                $(this).val(t.item.label);
//                $('#fromId').val(t.item.id);
//                return false;
//            }
//        })
//            .data("ui-autocomplete")._renderItem = function (e, t) {
//            return $("<li></li>").append('<a>' + t.label + '</a>').appendTo(e);
//        };
//    });


$(function t() {
    $('#from').autocomplete({
        type: "POST",
        minLength: 2,
        source: function(request, response) {
            $.ajax({
                url:"ajax/station_advice",
                dataType:"json",
                data: {
                    autocomplete: request.term
                },
                success : function(data) {
                    response($.map(data, function(item) {
                        return {
                            label:item.autocompleteWord,
                            id:item.id
                        }
                    }));
                }
            });
        },
        focus: function (e, t) {
            $(this).val(t.item.label);
            return false;
        },
        select: function (e, t) {
            $(this).val(t.item.label);
            $('#fromId').val(t.item.id);
            return false;
        }
    })
        .data("ui-autocomplete")._renderItem = function (e, t) {
        return $("<li></li>").append('<a>' + t.label + '</a>').appendTo(e);
    };
});

$(function t() {
    $('#to').autocomplete({
        type: "POST",
        minLength: 2,
        source: function(request, response) {
            $.ajax({
                url:"ajax/station_advice",
                dataType:"json",
                data: {
                    autocomplete: request.term
                },
                success : function(data) {
                    response($.map(data, function(item) {
                        return {
                            label:item.autocompleteWord,
                            id:item.id
                        }
                    }));
                }
            });
        },
        focus: function (e, t) {
            $(this).val(t.item.label);
            return false;
        },
        select: function (e, t) {
            $(this).val(t.item.label);
            $('#toId').val(t.item.id);
            return false;
        }
    })
        .data("ui-autocomplete")._renderItem = function (e, t) {
        return $("<li></li>").append('<a>' + t.label + '</a>').appendTo(e);
    };
});

//
//$("input[rel^='autocomp']").typeahead({
//        //источник данных
//        source: function (query, process) {
//            return $.post('ajax/station_advice', {'autocomplete':query},
//                function (response) {
//                    var data = new Array();
//                    //преобразовываем данные из json в массив
////                    $.each(response, function(i, name)
////                    {
////                        data.push(i+'_'+name);
////                    })
//                    alert(data);
//                    return process(data);
//                },
//                'json'
//            );
//        }
//        //источник данных
//        //вывод данных в выпадающем списке
//        , highlighter: function(item) {
//            var parts = item.split('_');
//            parts.shift();
//            return parts.join('_');
//        }
//        //вывод данных в выпадающем списке
//        //действие, выполняемое при выборе елемента из списка
//        , updater: function(item) {
//            var parts = item.split('_');
//            var userId = parts.shift();
//            $.post('getuserdata', {'user_id':userId},
//                function (user) {
//                    $('input[name=email]').val(user.email);
//                    $('input[name=phone]').val(user.phone);
//                },
//                'json'
//            );
//            return parts.join('_');
//        }
//        //действие, выполняемое при выборе елемента из списка
//    }
//);

    var timepicker = new Pikaday({
            field: document.getElementById("date"),
            firstDay: 1,
            minDate: new Date(1989, 0, 1),
            maxDate: new Date(2100, 12, 31),
            yearRange: [2016,2100],
            showTime: false,
            autoClose: false,
            use24hour: false,
            showDaysInNextAndPreviousMonths:true,
            format: 'DD.MM.YYYY'
        });

</script>