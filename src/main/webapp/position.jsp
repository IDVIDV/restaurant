<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="org.example.restaurant.datalayer.entities.Position" %><%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 08.04.2024
  Time: 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<div>
    <div>Название: <c:out value="${position.positionName}"></c:out></div>
    <div>Цена: ${position.price}</div>
    <div>Вес: ${position.weight}</div>
    <div>Белки: ${position.protein}</div>
    <div>Жиры: ${position.fat}</div>
    <div>Углеводы: ${position.carbohydrate}</div>
    <div>Веганское: ${position.vegan}</div>
    <div>Ингредиенты: <c:out value="${position.ingredients}"></c:out></div>
    <c:if test="${sessionScope.user.role eq 'admin'}">
        <form action="${pageContext.request.contextPath}/admin/update-position" method="get">
            <button type="submit" name="positionId" value="${position.id}">Изменить</button>
        </form>
        <form action="${pageContext.request.contextPath}/admin/delete-position" method="get">
            <button type="submit" name="positionId" value="${position.id}">Удалить</button>
        </form>
    </c:if>
</div>
</body>
</html>
