<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main page</title>
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
<form action="${pageContext.request.contextPath}/product/all/admin" method="get">
    <input type="submit" value="Product List for Admin"/>
</form>
<form action="${pageContext.request.contextPath}/shopping-carts/products" method="get">
    <input type="submit" value="Shopping Cart"/>
</form>
<form action="${pageContext.request.contextPath}/order/all-orders" method="get">
    <input type="submit" value="Orders"/>
</form>
<form action="${pageContext.request.contextPath}/order/admin" method="get">
    <input type="submit" value="All orders"/>
</form>
<form action="${pageContext.request.contextPath}/login" method="get">
    <input type="submit" value="Login"/>
</form>
<form action="${pageContext.request.contextPath}/logout" method="get">
    <input type="submit" value="Logout"/>
</form>
<form action="${pageContext.request.contextPath}/inject" method="get">
    <input type="submit" value="Inject Data"/>
</form>
</body>
</html>
