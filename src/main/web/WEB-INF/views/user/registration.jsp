<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<h1>Hello please provide your user details</h1>

<h4 style="color: red">${message}</h4>

<form method="post" action="${pageContext.request.contextPath}/user/registration">
    Enter your name: <input type="text" name="name">
    Enter your login: <input type="text" name="login">
    Enter your password: <input type="password" name="password">
    Repeat your password: <input type="password" name="password-repeat">

    <button type="submit">Register</button>
</form>
</body>
</html>
