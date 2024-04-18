<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<jsp:include page="/logininfo.jsp"></jsp:include>
<jsp:include page="/profile-link.jsp"></jsp:include>
<jsp:include page="/logout-link.jsp"></jsp:include>
<jsp:include page="/error.jsp"></jsp:include>
<c:forEach var="order" items="${requestScope.orders}">
    <a href="${pageContext.request.contextPath}/user/order?orderId=${order.id}">
        <section>Заказ на дату ${order.orderDate}</section>
    </a>
</c:forEach>
<jsp:include page="/error.jsp"></jsp:include>
<jsp:include page="/back-to-main-link.jsp"></jsp:include>
</body>
</html>
