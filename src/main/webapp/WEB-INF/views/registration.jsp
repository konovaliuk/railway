<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/fmt" prefix = "fmt" %>
<%@ page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" language="java"%>
<fmt:setLocale value = "${sessionScope.USER_LOCALE}"/>
<fmt:setBundle basename="views_locales.registration" var="lang"/>

<!doctype html>
<html lang="ua">
<head>
    <c:import url="../colon/css_dependencies.jsp" charEncoding="utf-8"/>
    <title>
        <c:if test="${sessionScope.USER_CURRENT_ROLE eq sessionScope.USER_ROLE_ATTR || sessionScope.USER_CURRENT_ROLE eq sessionScope.ADMIN_ROLE_ATTR}">
            <fmt:message key="page.profile.title" bundle="${lang}"/>
        </c:if>
        <c:if test="${sessionScope.USER_CURRENT_ROLE eq sessionScope.GUEST_ROLE_ATTR}">
            <fmt:message key="page.registration.title" bundle="${lang}"/>
        </c:if>
    </title>
</head>
<body>

<c:import url="../colon/navbar.jsp" charEncoding="utf-8"/>
<div class="container" >
    <div class="page-header">
        <h1>
            <c:if test="${sessionScope.USER_CURRENT_ROLE eq sessionScope.USER_ROLE_ATTR || sessionScope.USER_CURRENT_ROLE eq sessionScope.ADMIN_ROLE_ATTR}">
                <fmt:message key="page.profile.header" bundle="${lang}"/>
            </c:if>
            <c:if test="${sessionScope.USER_CURRENT_ROLE eq sessionScope.GUEST_ROLE_ATTR}">
                <fmt:message key="page.registration.header" bundle="${lang}"/>
            </c:if>
        </h1>
    </div>
</div>


<div class="container">

    <p id="msg_container" class="b-container"> <!--Text in Popup--></p>

        <form class="form-horizontal" method="post" action="#">

            <input type="hidden" id="id" value=""/>

            <div class="col-xl-4 col-lg-4 col-md-4 col-sm-6 col-6">

                <div class="form-group">
                    <label for="firstName" class="cols-sm-2 control-label"><fmt:message key="first_name.input.label" bundle="${lang}"/></label>
                        <div class="input-group input-group-lg">
                            <input type="text" class="form-control" id="firstName" name="firstName" required=""  placeholder="<fmt:message key="first_name.input.placeholder" bundle="${lang}"/>"/>
                            <span class="input-group-addon"><i class="fa fa-user fa-fw" aria-hidden="true"></i></span>
                        </div>
                </div>

                <div class="form-group">
                    <label for="lastName" class="cols-sm-2 control-label"><fmt:message key="last_name.input.label" bundle="${lang}"/></label>
                        <div class="input-group input-group-lg">
                            <input type="text" class="form-control" id="lastName" name="lastName" required="" placeholder="<fmt:message key="last_name.input.placeholder" bundle="${lang}"/>"/>
                            <span class="input-group-addon"><i class="fa fa-users fa-fw" aria-hidden="true"></i></span>
                        </div>
                </div>

                <div class="form-group">
                    <label for="email" class="cols-sm-2 control-label"><fmt:message key="email.input.label" bundle="${lang}"/></label>
                    <div class="cols-sm-4">
                        <div class="input-group input-group-lg">
                            <input type="text" required="" placeholder="<fmt:message key="email.input.placeholder" bundle="${lang}"/>" class="form-control" id="email" name="email" />
                            <span class="input-group-addon"><i class="fa fa-envelope-o fa-fw" aria-hidden="true"></i></span>
                        </div>
                    </div>
                </div>

                <c:if test="${sessionScope.USER_CURRENT_ROLE eq sessionScope.USER_ROLE_ATTR}">
                    <div class="form-group">
                        <label for="oldPass" class="cols-sm-2 control-label"><fmt:message key="old_password.input.label" bundle="${lang}"/></label>
                        <div class="cols-sm-4">
                            <div class="input-group input-group-lg">
                                <input type="password" class="form-control" id="oldPass" name="oldPass" required=""  placeholder="<fmt:message key="old_password.input.placeholder" bundle="${lang}"/>"/>
                                <span class="input-group-addon"><i class="fa fa-key fa-fw" aria-hidden="true"></i></span>
                            </div>
                        </div>
                    </div>
                </c:if>

                <div class="form-group">
                    <label for="pass" class="cols-sm-2 control-label"><fmt:message key="password.input.label" bundle="${lang}"/></label>
                    <div class="cols-sm-4">
                        <div class="input-group input-group-lg">
                            <input type="password" class="form-control" id="pass" name="pass" required="" placeholder="<fmt:message key="password.input.placeholder" bundle="${lang}"/>"/>
                            <span class="input-group-addon"><i class="fa fa-key fa-fw" aria-hidden="true"></i></span>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="repeatedPass" class="cols-sm-2 control-label"><fmt:message key="repeated_password.input.label" bundle="${lang}"/></label>
                    <div class="cols-sm-4">
                        <div class="input-group input-group-lg">
                            <input type="password" class="form-control" id="repeatedPass" name="repeatedPass" required="" placeholder="<fmt:message key="repeated_password.input.placeholder" bundle="${lang}"/>"/>
                            <span class="input-group-addon"><i class="fa fa-key fa-fw" aria-hidden="true"></i></span>
                        </div>
                    </div>
                </div>

                <!-- Button -->
                <div class="form-group btn-group-lg">
                    <button id="submit_button" type="button" onclick="saveUserProfile();" name="submit_button" class="btn btn-primary">
                        <c:if test="${sessionScope.USER_CURRENT_ROLE eq sessionScope.USER_ROLE_ATTR || sessionScope.USER_CURRENT_ROLE eq sessionScope.ADMIN_ROLE_ATTR}">
                            <fmt:message key="submit.profile.button.name" bundle="${lang}"/>
                        </c:if>
                        <c:if test="${sessionScope.USER_CURRENT_ROLE eq sessionScope.GUEST_ROLE_ATTR}">
                            <fmt:message key="submit.register.button.name" bundle="${lang}"/>
                        </c:if>
                    </button>
                </div>
            </div>
        </form>
</div>

<br><br>

<c:import url="../colon/footer.jsp" charEncoding="utf-8"/>
</body>
<c:import url="../colon/js_dependencies.jsp" charEncoding="utf-8"/>
</html>

<script>
    function saveUserProfile(){

        var jsonArg = {
            firstName : $('#firstName').val(),
            lastName : $('#lastName').val(),
            email : $('#email').val(),
            pass : $('#pass').val().split(''),
            repeatedPass : $('#repeatedPass').val().split(''),
            language : $('#language').val()
        };

        $.ajax({
            url:"ajax/new_user_profile",
            dataType:"json",
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify(jsonArg),
            success : function(data, textStatus) {
                var msg = document.getElementById("msg_container");
                if (textStatus === 'success') {
                    msg.innerHTML = data.message;
                    msg.className = (data.success) ? msg.className = 'b-container' : msg.className = 'a-container';
                } else {
                    msg.className = 'a-container';
                    msg.innerHTML = 'Operation wasn\'t successful';
                }

                msg.style.display = 'block';
                setTimeout(function () {
                        msg.style.display = 'none';
                    },
                    1000);

                if (data.success){
                    location.href = "<c:url value='/'/>login";
                }
            }
        });
    }

    function updateUserProfile(){

        var jsonArg = {
            id : id,
            firstName : $('#firstName').val(),
            lastName : $('#lastName').val(),
            email : $('#email').val().split(''),
            pass : $('#pass').val().split(''),
            repeatedPass : $('#repeatedPass').val().split(''),
            oldPass : $('#oldPass').val(),
            language : $('#language').val()
        };

        $.ajax({
            url:"ajax/update_user_profile",
            dataType:"json",
            type: 'POST',
            contentType: "application/json",
            data: JSON.stringify(jsonArg),
            success : function(data, textStatus) {
                var msg = document.getElementById("msg_container");
                if (textStatus === 'success') {
                    msg.innerHTML = data.message;
                    msg.className = (data.success) ? msg.className = 'b-container' : msg.className = 'a-container';
                } else {
                    msg.className = 'a-container';
                    msg.innerHTML = 'Operation wasn\'t successful';
                }

                msg.style.display = 'block';
                setTimeout(function () {
                        msg.style.display = 'none';
                    },
                    2000);
            }
        });
    }

</script>