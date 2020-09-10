<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="post" action="${pageContext.request.contextPath}/products/add">
    Enter product name: <input type="text" name="name">
    Enter product price: <input step=".01" type="number" name="price">

    <button type="submit">Add Product</button>
</form>
</body>
</html>
