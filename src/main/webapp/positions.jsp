<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 03.04.2024
  Time: 16:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Блюда</title>
</head>
<body>
<c:if test="${empty sessionScope.user}">
    <form action="${pageContext.request.contextPath}/login" method="get">
        <button type="submit">Войти</button>
    </form>
</c:if>
<c:if test="${not empty sessionScope.user}">
    <div>Вы вошли как ${sessionScope.user.login}</div>
</c:if>
<form method="get">
    <label>
        Название позиции
        <input type="text" name="positionName">
    </label>
    <button type="submit">Поиск</button>
</form>
<div>
    <c:forEach var="position" items="${requestScope.positions}">
        <a href="${pageContext.request.contextPath}/position?positionId=${position.id}">
            <section>${position.positionName}</section>
        </a>
    </c:forEach>
    <c:if test="${sessionScope.user.role eq 'admin'}">
        <form action="${pageContext.request.contextPath}/admin/add-position" method="get">
            <button type="submit">Добавить</button>
        </form>
    </c:if>
</div>
</body>
</html>
