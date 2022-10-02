<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>Library</title>
  <link rel="stylesheet" href="css/style.css" />
<!-- href="${pageContext.request.contextPath}/css/style.css" /> -->

</head>
<body>
<!-- Top navigation -->

<div class="f-container fixed-hf">
  <header>
    <img alt="Library" src="img/logo.svg">
	  <form action="search" method="get">
	    <input type="text" name="text" placeholder="Search...">
<!-- 	    <input type="submit" value="Find a book"> -->
	  </form>
    <a class="active" onclick="document.getElementById('id01').style.display='block'">Login</a>
  </header>
  <div class="main">
    <p>add or remove "fixed-hf" to have fixed header and footer and scrollable main section</p>
    a<br/>
    b<br/>
    c<br/>
    <div style="display:block">
      <p>change none/block to test shorter content</p>
    d<br/>
    e<br/>
    f<br/>
    g<br/>
    h<br/>
    i<br/>
    j<br/>
    k<br/>
    l<br/>
    m<br/>
    n<br/>
    o<br/>
    p<br/>
    q<br/>
    r<br/>
    s<br/>
    t<br/>
    u<br/>
    v<br/>
    w<br/>
    x<br/>
    y<br/>
    z<br/>
    </div>
  </div>
  <footer>
  <div></div>
  <div>
    <a href="">english</a>
    | 
    <a href="">українська</a>
    </div>
    <div></div>
  </footer>
  
  
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