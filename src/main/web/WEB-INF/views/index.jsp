<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/user/registration" method="get">
    <input type="submit" value="Registration"/>
</form>
<form action="${pageContext.request.contextPath}/user/all" method="get">
    <input type="submit" value="User List"/>
</form>
<form action="${pageContext.request.contextPath}/products/add" method="get">
    <input type="submit" value="Add Product"/>
</form>
<form action="${pageContext.request.contextPath}/product/all" method="get">
    <input type="submit" value="Product List"/>
</form>
<form action="${pageContext.request.contextPath}/shopping-carts/products" method="get">
    <input type="submit" value="Shopping Cart"/>
</form>
<form action="${pageContext.request.contextPath}/order/all-orders" method="get">
    <input type="submit" value="Orders"/>
</form>
</body>
</html>
