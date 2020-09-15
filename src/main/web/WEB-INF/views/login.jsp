<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Page</title>
</head>
<body>
<h1>Login page</h1>
<h4 style="color: red">${errorMsg}</h4>
<form action="${pageContext.request.contextPath}/login" method="post">
    Enter your login: <input type="text" name="login">
    Enter your password: <input type="password" name="password">
    <button type="submit">Login</button>
</form>
<form action="${pageContext.request.contextPath}/user/registration" method="get">
    <input type="submit" value="Registration"/>
</form>
</body>
</html>
