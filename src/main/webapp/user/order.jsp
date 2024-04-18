<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 14.04.2024
  Time: 18:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Заказ</title>
</head>
<body>
<jsp:include page="/logininfo.jsp"></jsp:include>
<jsp:include page="/profile-link.jsp"></jsp:include>
<jsp:include page="/logout-link.jsp"></jsp:include>
<jsp:include page="/error.jsp"></jsp:include>
<div>
    <div>Дата: ${order.orderDate}</div>
    <div>Номер столика: ${order.tableDto.tableNumber}</div>
    <div>
        Блюда:
        <br>
        <c:forEach var="positionInOrder" items="${requestScope.orderPositions}">
            <a href="${pageContext.request.contextPath}/position?positionId=${positionInOrder.position.id}">
                <section>
                    <p>${positionInOrder.position.positionName}</p>
                    <p>Количество: ${positionInOrder.positionCount}</p>
                </section>
            </a>
        </c:forEach>
    </div>
</div>
<jsp:include page="/error.jsp"></jsp:include>
<jsp:include page="/back-to-main-link.jsp"></jsp:include>
</body>
</html>
