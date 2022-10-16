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
	<form action="reader" method="get">
	  <input type="text" name="text" placeholder="Search...">
	</form>
<%@include file="/WEB-INF/jspf/reader_menu.jspf" %>  
  </header>
  <div class="main">
    <c:if test="${orders.size() == 0}">
      <div class="modal-content-orderinfo">
        <div class="container">
          <h4 class="pass-center">Not found books on a subscription</h4>
        </div>
      </div>
    </c:if>
    <c:if test="${orders.size() != 0}">
    <table class="book-table">
        <tr class="books-header">
          <td class="center">Order</td>
		  <td class="center">Book</td>
          <td class="center">Return date</td>
          <td class="center">Fine</td>
      <c:forEach items="${orders}" var="order">
        <c:if test="${order.state == 'PROCESSED'}">
        <c:if test="${order.fine == 0}">
        <tr class="orders-active">
          <td class="center">${order.id}</td>
          <td><div class="left">${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}</div></td>
          <td class="center"><mt:date time="${order.returnTime}" /></td>
          <td class="center">${order.fine} UAH</td>
        </tr>
        </c:if>
        <c:if test="${order.fine != 0}">
        <tr class="orders-fined">
          <td class="center">${order.id}</td>
          <td><div class="left">${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}</div></td>
          <td class="center"><mt:date time="${order.returnTime}" /></td>
          <td class="center">${order.fine} UAH</td>
        </tr>
        </c:if>
        </c:if>
      </c:forEach>
    </table>
    </c:if>
    <mtl:pagination nextPage="${nextPage}" servletName="reader" previousPage="${prevPage}" currentPage="${page}" searchText="${text}"/>
  
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
  
<%@include file="/WEB-INF/jspf/register_data.jspf" %>  
<%@include file="/WEB-INF/jspf/error.jspf" %>
<%@include file="/WEB-INF/jspf/info.jspf" %>
  
  
</div>
</body>
</html>
