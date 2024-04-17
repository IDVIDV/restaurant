<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 15.04.2024
  Time: 1:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form action="${pageContext.request.contextPath}/user/add-to-order" method="post">
    <input hidden="hidden" required name="positionId" value="${param.positionId}">
    <button type="submit">Добавить в заказ</button>
</form>
