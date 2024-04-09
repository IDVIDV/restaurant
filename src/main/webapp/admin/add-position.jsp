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
<form method="post">
    <label>
        Название
        <input type="text" name="positionName">
    </label>
    <br>
    <label>
        Цена
        <input type="number" name="price">
    </label>
    <br>
    <label>
        Вес
        <input type="number" name="weight">
    </label>
    <br>
    <label>
        Белки
        <input type="number" name="protein">
    </label>
    <br>
    <label>
        Жиры
        <input type="number" name="fat">
    </label>
    <br>
    <label>
        Углеводы
        <input type="number" name="carbohydrate">
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
</body>
</html>
