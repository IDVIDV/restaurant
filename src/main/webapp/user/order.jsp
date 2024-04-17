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
<div>
    <div>Дата: ${order.orderDate}</div>
    <div>Номер столика: ${order.table.tableNumber}</div>
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
</body>
</html>
