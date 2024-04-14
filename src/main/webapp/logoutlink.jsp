<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 12.04.2024
  Time: 18:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:if test="${not empty sessionScope.user}">
    <form action="${pageContext.request.contextPath}/logout" method="get">
        <button type="submit">Выйти</button>
    </form>
</c:if>