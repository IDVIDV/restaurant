<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 15.04.2024
  Time: 2:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:if test="${not empty sessionScope.user}">
    <form action="${pageContext.request.contextPath}/user/orders" method="get">
        <button type="submit">Мои заказы</button>
    </form>
</c:if>