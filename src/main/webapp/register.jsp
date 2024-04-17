<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 10.04.2024
  Time: 10:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Регистрация</title>
</head>
<body>
<form method="post">
    <label>
        Логин
        <input type="text" required name="login">
    </label>
    <br>
    <label>
        Пароль
        <input type="password" required name="password">
    </label>
    <br>
    <label>
        Телефон
        <input type="text" name="phoneNumber">
    </label>
    <br>
    <button type="submit">Зарегистрироваться</button>
</form>
<jsp:include page="error.jsp"></jsp:include>
<jsp:include page="back-to-main-link.jsp"></jsp:include>
</body>
</html>
