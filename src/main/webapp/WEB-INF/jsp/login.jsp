<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<title>Final project</title>
	<link rel="stylesheet" 
					href="${pageContext.request.contextPath}/css/login.css" />
</head>
<body>

<div class="form-center">
	<form class="form" action="login" method="post">
	  <input class="input" name="email" type="email" placeholder="email">
	  <input class="input" name="password" type="password" placeholder="password">
	  <button class="btn" type="submit">Login</button>
	</form>
</div>


</body>
</html>