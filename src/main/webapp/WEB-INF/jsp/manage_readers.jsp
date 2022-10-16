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
	<form action="findreader" method="get">
	  <input type="text" name="phonenumber" placeholder="find a reader by phone...">
	</form>
<%@include file="/WEB-INF/jspf/admin_menu.jspf" %>  
  </header>
  <div class="main">
    <c:if test="${readers.size() == 0}">
      <div class="modal-content-orderinfo">
        <div class="container">
          <h4 class="pass-center">Readers not found</h4>
        </div>
      </div>
    </c:if>
    <c:if test="${readers.size() != 0}">
    <table class="book-table">
        <tr class="books-header">
          <td class="center">Reader ID</td>
		  <td class="center">Reader Name</td>
          <td class="center">Phone</td>
          <td class="center">Email</td>
          <td class="center">Fine</td>
          <td class="center">Status</td>
          <td></td>
      <c:forEach items="${readers}" var="reader">
        <c:if test="${!reader.blocked}">
          <c:if test="${reader.fine > 0}">
            <tr class="orders-fined">
              <td class="center">${reader.id}</td>
	          <td class="center">${reader.firstName} ${reader.lastName}</td>
              <td class="center">${reader.phoneNumber}</td>
              <td class="center">${reader.email}</td>
              <td class="center">${reader.fine} UAH</td>
              <td class="center">Unblocked</td>
              <td class="center">
                <form action="blockreader" method="post">
	              <input type="hidden" name="id" value="${reader.id}" >
	              <input type="hidden" name="blocked" value="true" >
                  <button type="submit" class="deleteuser">Block</button>
                </form>
              </td>
            </tr>
          </c:if>
          <c:if test="${reader.fine == 0}">
            <tr class="orders-active">
              <td class="center">${reader.id}</td>
	          <td class="center">${reader.firstName} ${reader.lastName}</td>
              <td class="center">${reader.phoneNumber}</td>
              <td class="center">${reader.email}</td>
              <td class="center">${reader.fine} UAH</td>
              <td class="center">Unblocked</td>
              <td class="center">
                <form action="blockreader" method="post">
	              <input type="hidden" name="id" value="${reader.id}" >
	              <input type="hidden" name="blocked" value="true" >
                  <button type="submit" class="deleteuser">Block</button>
                </form>
              </td>
            </tr>
          </c:if>
        </c:if>
        <c:if test="${reader.blocked}">
          <tr class="orders-inactive">
            <td class="center">${reader.id}</td>
	        <td class="center">${reader.firstName} ${reader.lastName}</td>
            <td class="center">${reader.phoneNumber}</td>
            <td class="center">${reader.email}</td>
            <td class="center">${reader.fine} UAH</td>
            <td class="center">Blocked</td>
            <td class="center">
              <form action="blockreader" method="post">
	            <input type="hidden" name="id" value="${reader.id}" >
                <button type="submit" class="unblockuser">Unblock</button>
              </form>
            </td>
          </tr>
        </c:if>
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

<%@include file="/WEB-INF/jspf/error.jspf" %>
<%@include file="/WEB-INF/jspf/info.jspf" %>

</body>
</html>
