<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 08.04.2024
  Time: 11:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Новая позиция</title>
</head>
<body>
<jsp:include page="/logininfo.jsp"></jsp:include>
<jsp:include page="/profilelink.jsp"></jsp:include>
<jsp:include page="/logoutlink.jsp"></jsp:include>
<form method="post">
    <label>
        Название
        <input type="text" required name="positionName">
    </label>
    <br>
    <label>
        Цена
        <input type="number" required name="price">
    </label>
    <br>
    <label>
        Вес
        <input type="number" required name="weight">
    </label>
    <br>
    <label>
        Белки
        <input type="number" required name="protein">
    </label>
    <br>
    <label>
        Жиры
        <input type="number" required name="fat">
    </label>
    <br>
    <label>
        Углеводы
        <input type="number" required name="carbohydrate">
    </label>
    <br>
    <label>
        Веганское
        <input type="checkbox" name="isVegan">
    </label>
    <br>
    <label>
        Ингредиенты
        <input type="text" name="ingredients" style="width: 300px; height: 300px;">
    </label>
    <br>
    <button type="submit">Сохранить</button>
</form>
<jsp:include page="/error.jsp"></jsp:include>
<jsp:include page="/backtomainlink.jsp"></jsp:include>
</body>
</html>
