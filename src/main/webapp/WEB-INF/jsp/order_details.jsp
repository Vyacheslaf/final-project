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
    <c:if test="${requestScope.order.state == 'NEW'}">
      <div class="modal-content-orderinfo">
        <div class="container">
          <table class="order-table">
            <tr class="order-info">
              <td class="order-right">ISBN:</td>
              <td class="order">${requestScope.order.book.isbn}</td>
            </tr>
            <tr class="order-info">
              <td class="order-right">Book:</td>
              <td class="order">${requestScope.order.book.author}, ${requestScope.order.book.title}, 
          						${requestScope.order.book.publication}, ${requestScope.order.book.publicationYear}</td>
            </tr>
            <tr class="order-info">
              <td class="order-right">Reader Name:</td>
              <td class="order">${requestScope.order.user.firstName} ${requestScope.order.user.lastName}</td>
            </tr>
            <tr class="order-info">
              <td class="order-right">Reader Document:</td>
              <td class="order">${requestScope.order.user.passportNumber}</td>
            </tr>
            <tr class="order-info">
              <td class="order-right">Reader Phone:</td>
              <td class="order">${requestScope.order.user.phoneNumber}</td>
            </tr>
          </table>
          <c:if test="${requestScope.order.user.blocked == false}"> 
            <c:if test="${requestScope.order.book.available > 0}">
	          <form action="givebook" method="post">
	            <div class="container-button">
	              <input type="hidden" name="orderid" value="${requestScope.order.id}" >
	              <button type="submit" class="givebook-active">Give the book to the reading room</button>
	            </div>
	          </form>
	          <form action="givebook" method="post">
	            <div class="container-button">
	              <input type="hidden" name="orderid" value="${requestScope.order.id}" >
	              <input type="hidden" name="onsubscription" value="true" >
	              <button type="submit" class="givebook-active">Give the book on a subscription</button>
	            </div>
	          </form>
            </c:if>
            <c:if test="${requestScope.order.book.available == 0}">
	          <form action="givebooktoroom" method="post">
	            <div class="container-button">
	              <button type="submit" disabled>The book is not available now</button>
	            </div>
	          </form>
	          <form action="givebook" method="post">
	            <div class="container-button">
	              <button type="submit" disabled>The book is not available now</button>
	            </div>
	          </form>
            </c:if>
          </c:if>
          <c:if test="${requestScope.order.user.blocked == true}">  
            <form action="givebook" method="post">
              <div class="container-button">
                <button type="submit" disabled>Reader is blocked</button>
              </div>
            </form>
            <form action="givebook" method="post">
              <div class="container-button">
                <button type="submit" disabled>Reader is blocked</button>
              </div>
            </form>
          </c:if>
          <form action="cancelorder" method="post">
            <div class="container-button">
              <input type="hidden" name="orderid" value="${requestScope.order.id}" >
              <button type="submit" class="cancelorder">Cancel an order</button>
            </div>
          </form>
        </div>
      </div>
    </c:if>

    <c:if test="${requestScope.order.state == 'PROCESSED'}">
      <div class="modal-content-orderinfo">
        <div class="container">
          <table class="order-table">
            <tr class="order-info">
              <td class="order-right">ISBN:</td>
              <td class="order">${requestScope.order.book.isbn}</td>
            </tr>
            <tr class="order-info">
              <td class="order-right">Book:</td>
              <td class="order">${requestScope.order.book.author}, ${requestScope.order.book.title}, 
          						${requestScope.order.book.publication}, ${requestScope.order.book.publicationYear}</td>
            </tr>
            <tr class="order-info">
              <td class="order-right">Reader Name:</td>
              <td class="order">${requestScope.order.user.firstName} ${requestScope.order.user.lastName}</td>
            </tr>
            <tr class="order-info">
              <td class="order-right">Reader Document:</td>
              <td class="order">${requestScope.order.user.passportNumber}</td>
            </tr>
            <tr class="order-info">
              <td class="order-right">Reader Phone:</td>
              <td class="order">${requestScope.order.user.phoneNumber}</td>
            </tr>
            <tr class="order-info">
              <td class="order-right">Return Date:</td>
              <td class="order"><mt:date time="${requestScope.order.returnTime}"></mt:date></td>
            </tr>
            <c:if test="${requestScope.order.fine != 0}">
            <tr class="order-info-fined">
              <td class="order-right">Fine:</td>
              <td class="order">${requestScope.order.fine} UAH</td>
            </tr>
            </c:if>
            <c:if test="${requestScope.order.fine == 0}">
            <tr class="order-info">
              <td class="order-right">Fine:</td>
              <td class="order">${requestScope.order.fine} UAH</td>
            </tr>
            </c:if>
          </table>
          <form action="completeorder" method="post">
            <div class="container-button">
              <input type="hidden" name="orderid" value="${requestScope.order.id}" >
              <input type="hidden" name="readerid" value="${requestScope.order.user.id}" >
              <c:if test="${requestScope.order.fine != 0}">
                <button type="submit" class="cancelorder">Return the book and fine the reader</button>
              </c:if>
              <c:if test="${requestScope.order.fine == 0}">
                <button type="submit" class="givebook-active">Return the book</button>
              </c:if>
            </div>
          </form>
        </div>
      </div>
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
