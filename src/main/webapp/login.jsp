<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 01.04.2024
  Time: 9:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post">
    <label>
        login
        <input type="text" name="login">
    </label>
    <br>
    <label>
        password
        <input type="text" name="password">
    </label>
    <br>
    <button type="submit">Вход</button>
</form>
<jsp:include page="error.jsp"></jsp:include>
<jsp:include page="backtomainlink.jsp"></jsp:include>
</body>
</html>
