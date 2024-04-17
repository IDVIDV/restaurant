<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 01.04.2024
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Профиль</title>
</head>
<body>
<form method="post">
    <label>
        Логин
        <input type="text" name="login" required value="${sessionScope.user.login}">
    </label>
    <br>
    <label>
        Телефон
        <input type="phone" name="phoneNumber" value="${sessionScope.user.phoneNumber}">
    </label>
    <br>
    <button type="submit">Сохранить</button>
</form>
<jsp:include page="/error.jsp"></jsp:include>
<jsp:include page="/orders-link.jsp"></jsp:include>
<jsp:include page="/opened-order-link.jsp"></jsp:include>
<jsp:include page="/logout-link.jsp"></jsp:include>
<jsp:include page="/back-to-main-link.jsp"></jsp:include>
</body>
</html>
