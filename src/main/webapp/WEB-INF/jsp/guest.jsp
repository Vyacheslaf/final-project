<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://example.org/taglib" prefix="mtl" %>

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
	  <form action="guest" method="get">
	    <input type="text" name="text" placeholder="Search...">
<!-- 	    <input type="submit" value="Find a book"> -->
	  </form>
    <a class="active" onclick="document.getElementById('id01').style.display='block'">Sign in</a>
  </header>
  <div class="main">
    <table class="book-table">
        <tr class="books-header">
          <td class="center">Author</td>
          <td class="center">Title</td>
          <td class="center">Publication</td>
          <td class="center">Year</td>
          <td></td>
      <c:forEach items="${books}" var="book">
        <tr class="books">
          <td class="center">${book.author}</td>
          <td class="center">${book.title}</td>
          <td class="center">${book.publication}</td>
          <td class="center">${book.publicationYear}</td>
          <td class="center">
          	To order a book<br>
          	you need to login
<!--             <button class="noHover" disabled>Order a book</button> -->
          </td>
        </tr>
      </c:forEach>
    </table>
    <mtl:pagination nextPage="${nextPage}" servletName="guest" previousPage="${prevPage}" currentPage="${page}" searchText="${text}"/>
  
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
        <button type="submit">Sign in</button>
      </div>
      <div class="container" style="background-color:#f1f1f1">
        <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">Cancel</button>
        <span class="psw"><a class="pointer" onclick='register();'>Register</a></span>
      </div>
    </form>
  </div> 

  <div id="id02" class="modal">
    <form class="modal-content-reg" action="register" method="post">
      <div class="container">
        <input type="email" placeholder="Enter Email" name="email" required>
        <input type="password" placeholder="Enter Password" name="password" id="password" onkeyup='check();' required>
        <input type="password" placeholder="Confirm Password" name="confirm" id="confirm" onkeyup='check();' required>
        <span id='message'></span>
        <input type="text" placeholder="Enter First Name" name="firstname" required>
        <input type="text" placeholder="Enter Last Name" name="lastname" required>
        <input type="text" placeholder="Enter Phone Number" name="phone" required>
        <input type="text" placeholder="Enter Document Number" name="passport" required>
        <input type="hidden" name="role" value="reader" >
        <button id='regbtn' type="submit">Register</button>
      </div>
    </form>
  </div> 

<script src="js/modal_close.js"></script>
<script src="js/confirm_password.js"></script>
<script src="js/register_modal.js"></script>

</body>
</html>