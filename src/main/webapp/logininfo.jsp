<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 10.04.2024
  Time: 9:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:choose>
    <c:when test="${empty sessionScope.user}">
        <form action="${pageContext.request.contextPath}/login" method="get">
            <button type="submit">Войти</button>
        </form>
    </c:when>
    <c:otherwise>
        <div>Вы вошли как ${sessionScope.user.login}</div>
    </c:otherwise>
</c:choose>