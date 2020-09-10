<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<table border="1">
    <tr>
        <th>Name</th>
        <th>Price</th>
    </tr>
    <c:forEach var="product" items="${products}">
        <tr>
            <td>
                <c:out value="${product.name}"/>
            </td>
            <td>
                <c:out value="${product.price}"/>
            </td>
            <td>
                <form method="post" action="${pageContext.request.contextPath}/shopping-carts/products/add" >
                   <input type="hidden" name="id" value="${product.id}">
                    <button type="submit">Buy</button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
