<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 14.04.2024
  Time: 18:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>История заказов</title>
</head>
<body>
<c:forEach var="order" items="${requestScope.orders}">
    <a href="${pageContext.request.contextPath}/user/order?orderId=${order.id}">
        <section>Заказ на дату ${order.orderDate}</section>
    </a>
</c:forEach>
</body>
</html>
