<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://example.org/taglib" prefix="mtl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mt" %>

<!DOCTYPE html>
<html>
<head>
  <title>Library</title>
  <link rel="stylesheet" href="css/style.css" />
</head>
<body>
<div class="f-container fixed-hf">
  <header>
      <a class="home" href="start">
        <img alt="Library" src="img/logo.svg">
	  </a>
      <a class="register" onclick='register();'>Register new librarian</a>
<%@include file="/WEB-INF/jspf/admin_menu.jspf" %>  
  </header>
  <div class="main">
    <c:if test="${librarians.size() == 0}">
      <div class="modal-content-orderinfo">
        <div class="container">
          <h4 class="pass-center">Librarians not found</h4>
        </div>
      </div>
    </c:if>
    <c:if test="${librarians.size() != 0}">
    <table class="book-table">
        <tr class="books-header">
          <td class="center">Librarian ID</td>
		  <td class="title">Librarian Name</td>
          <td class="center">Phone</td>
          <td class="center">Email</td>
          <td></td>
      <c:forEach items="${librarians}" var="librarian">
        <tr class="orders-active">
          <td class="center">${librarian.id}</td>
		  <td class="center">${librarian.firstName} ${librarian.lastName}</td>
          <td class="center">${librarian.phoneNumber}</td>
          <td class="center">${librarian.email}</td>
          <td class="center">
            <form action="deleteuser" method="post">
	          <input type="hidden" name="userid" value="${librarian.id}" >
              <button type="submit" class="deleteuser">Delete</button>
            </form>
          </td>
        </tr>
      </c:forEach>
    </table>
    </c:if>
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

  <div id="id01" class="modal"></div> 
  <div id="id02" class="modal">
    <form class="modal-content-reg" action="register" method="post">
      <div class="container">
        <input type="email" placeholder="Enter Email" name="email" id="email" onkeyup='checkRegData();' required>
        <input type="password" placeholder="Enter Password" name="password" id="password" onkeyup='checkRegData();' required>
        <input type="password" placeholder="Confirm Password" name="confirm" id="confirm" onkeyup='checkRegData();' required>
        <input type="text" placeholder="Enter First Name" name="firstname" id="firstname" onkeyup='checkRegData();' required>
        <input type="text" placeholder="Enter Last Name" name="lastname" id="lastname" onkeyup='checkRegData();' required>
        <input type="text" placeholder="Enter Phone Number" name="phone" id="phone" onkeyup='checkRegData();' required>
        <input type="text" placeholder="Enter Document Number" name="passport" id="passport" onkeyup='checkRegData();' required>
        <input type="hidden" name="role" value="librarian" >
        <button id='regbtn' type="submit">Register</button>
      </div>
    </form>
  </div> 
  
<%@include file="/WEB-INF/jspf/error.jspf" %>
<%@include file="/WEB-INF/jspf/info.jspf" %>

<script src="js/modal_close.js"></script>
<script src="js/input_checks.js"></script>
<script src="js/register_modal.js"></script>
<script>checkRegData();</script>

</body>
</html>
