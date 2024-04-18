<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: IDV
  Date: 18.04.2024
  Time: 17:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:if test="${empty sessionScope.user}">
    <form action="${pageContext.request.contextPath}/register" method="get">
        <button type="submit">Зарегистрироваться</button>
    </form>
</c:if>
