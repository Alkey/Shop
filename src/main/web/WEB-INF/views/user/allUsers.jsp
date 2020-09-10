<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>All users</title>
</head>
<body>
<table border="1">
    <tr>
        <th>ID</th>
        <th>Login</th>
    </tr>
    <c:forEach var="user" items="${users}">
        <tr>
        <td>
            <c:out value="${user.id}"/>
        </td>
        <td>
            <c:out value="${user.login}"/>
        </td>
            <td>
                <form method="post" action="${pageContext.request.contextPath}/user/delete" >
                    <input type="hidden" name="id" value="${user.id}">
                    <button type="submit">Delete</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
