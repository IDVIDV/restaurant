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
    <title>Корзина</title>
</head>
<body>
<jsp:include page="/logininfo.jsp"></jsp:include>
<jsp:include page="/profile-link.jsp"></jsp:include>
<jsp:include page="/logout-link.jsp"></jsp:include>
<jsp:include page="/error.jsp"></jsp:include>
<div>
    <form method="post">
        <input hidden="hidden" name="orderId" value="${order.id}">
        <input hidden="hidden" name="userId" value="${order.userId}">
        <label>
            Дата
            <input type="date" required name="orderDate">
        </label>
        <br>
        <label>
            Номер столика:
            <select required name="tableId">
                c:forEach var="position" items="${requestScope.positions}">
                <c:forEach var="table" items="${requestScope.tables}">
                    <option value="${table.id}">#${table.tableNumber}, Вместимость: ${table.capacity}</option>
                </c:forEach>
            </select>
        </label>
        <br>
        <button type="submit">Закрыть заказ</button>
    </form>
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

            <jsp:include page="/add-to-order-link.jsp">
                <jsp:param name="positionId" value="${positionInOrder.position.id}"/>
            </jsp:include>
            <jsp:include page="/remove-from-order-link.jsp">
                <jsp:param name="positionId" value="${positionInOrder.position.id}"/>
            </jsp:include>
        </c:forEach>
    </div>
</div>
<jsp:include page="/error.jsp"></jsp:include>
<jsp:include page="/back-to-main-link.jsp"></jsp:include>
</body>
</html>
