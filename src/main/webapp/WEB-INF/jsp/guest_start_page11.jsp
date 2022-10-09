<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Library</title>
  <link rel="stylesheet" href="css/style.css" />
<!-- href="${pageContext.request.contextPath}/css/style.css" /> -->

</head>
<body>
  <div class="header">
	<a href="start" class="logo">Library</a>
	<div class="center">
	  <form action="search" method="get">
	    <input type="text" name="text" placeholder="Enter author name or book title for search">
	    <input type="submit" value="Find a book">
	  </form>
	</div>
	<div class="header-right">
 	  <a class="active" onclick="document.getElementById('id01').style.display='block'">Login</a> 
	</div>
  </div>
 
  <div id="id01" class="modal">
    <form class="modal-content" action="login" method="post">
      <div class="container">
        <input type="email" placeholder="Enter Email" name="email" required>
        <input type="password" placeholder="Enter Password" name="password" required>
        <button type="submit">Login</button>
      </div>
      <div class="container" style="background-color:#f1f1f1">
        <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button>
        <span class="psw"><a href="register">Register</a></span>
      </div>
    </form>
  </div> 

<script src="js/modal_close.js"></script>

</body>
</html>