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
<%@include file="/WEB-INF/jspf/librarian_menu.jspf" %>  
  </header>
  <div class="main">
    <c:if test="${orders.size() == 0}">
      <div class="modal-content-orderinfo">
        <div class="container">
          <h4 class="pass-center">Not found actual orders for reader</h4>
        </div>
      </div>
    </c:if>
    <c:if test="${orders.size() != 0}">
       <div class="modal-content-readerinfo">
        <div class="container">
          <h4 class="pass-center">
            List of actual orders of ${requestScope.orders[0].user.firstName} ${requestScope.orders[0].user.lastName}
          </h4>
        </div>
      </div>
    <table class="book-table">
        <tr class="books-header">
          <td class="center">Order</td>
		  <td class="center">Book</td>
          <td class="center">Return date</td>
          <td class="center">Fine</td>
          <td></td>
      <c:forEach items="${orders}" var="order">
        <c:if test="${order.fine == 0}">
          <c:if test="${order.state == 'PROCESSED'}">
            <tr class="orders-active">
              <td class="center">${order.id}</td>
              <td class="center">${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}</td>
              <td class="center"><mt:date time="${order.returnTime}" /></td>
              <td class="center">${order.fine} UAH</td>
              <td class="button">
                <a href="orderdetails?orderid=${order.id}">Order Details</a>
              </td>
            </tr>
          </c:if>
          <c:if test="${order.state == 'NEW'}">
            <tr class="orders">
              <td class="center">${order.id}</td>
              <td class="center">${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}</td>
              <td class="center">Not issued yet</td>
              <td class="center">${order.fine} UAH</td>
              <td class="button">
                <a href="orderdetails?orderid=${order.id}">Order Details</a>
              </td>
            </tr>
          </c:if>
        </c:if>
        <c:if test="${order.fine != 0}">
        <tr class="orders-fined">
          <td class="center">${order.id}</td>
          <td class="center">${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}</td>
          <td class="center"><mt:date time="${order.returnTime}" /></td>
          <td class="center">${order.fine} UAH</td>
          <td class="button">
            <a href="orderdetails?orderid=${order.id}">Order Details</a>
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
  
<%@include file="/WEB-INF/jspf/error.jspf" %>
<%@include file="/WEB-INF/jspf/info.jspf" %>
  
</div>
</body>
</html>
