<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 08.04.2024
  Time: 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Блюдо <c:out value="${position.positionName}"></c:out></title>
</head>
<body>
<jsp:include page="logininfo.jsp"></jsp:include>
<jsp:include page="register-link.jsp"></jsp:include>
<jsp:include page="profile-link.jsp"></jsp:include>
<jsp:include page="orders-link.jsp"></jsp:include>
<jsp:include page="opened-order-link.jsp"></jsp:include>
<jsp:include page="logout-link.jsp"></jsp:include>
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
<jsp:include page="add-to-order-link.jsp">
    <jsp:param name="positionId" value="${position.id}"/>
</jsp:include>
<jsp:include page="error.jsp"></jsp:include>
<jsp:include page="back-to-main-link.jsp"></jsp:include>
</body>
</html>
