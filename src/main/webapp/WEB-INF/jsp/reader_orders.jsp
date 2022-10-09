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
    <table class="book-table">
        <tr class="books-header">
          <td class="center">Order</td>
		  <td class="center">Book</td>
          <td class="center">Status</td>
          <td class="center">Create date</td>
          <td class="center">Return date</td>
          <td></td>
      <c:forEach items="${orders}" var="order">
        <c:choose>
        <c:when test="${order.state == 'PROCESSED'}">
        <tr class="orders-active">
          <td class="center">${order.id}</td>
          <td><div class="left">${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}</div></td>
          <td class="center">${order.state}</td>
          <td class="center"><mt:time time="${order.createTime}" /></td>
          <td class="center"><mt:date time="${order.returnTime}" /></td>
          <td class="center">
            <form action="cancelorder" method="post">
	          <input type="hidden" name="orderid" value="${order.id}" >
              <button type="submit" class="cancelorderbtn" <mt:cancelbutton state="${order.state}" />>Cancel</button>
            </form>
          </td>
        </tr>
        </c:when>
        <c:when test="${order.state == 'NEW'}">
        <tr class="orders">
          <td class="center">${order.id}</td>
          <td><div class="left">${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}</div></td>
          <td class="center">${order.state}</td>
          <td class="center"><mt:time time="${order.createTime}" /></td>
          <td class="center"><mt:date time="${order.returnTime}" /></td>
          <td class="center">
            <form action="cancelorder" method="post">
	          <input type="hidden" name="orderid" value="${order.id}" >
              <button type="submit" class="cancelorderbtn" <mt:cancelbutton state="${order.state}" />>Cancel</button>
            </form>
          </td>
        </tr>
        </c:when>
        <c:otherwise>
        <tr class="orders-inactive">
          <td class="center">${order.id}</td>
          <td><div class="left">${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}</div></td>
          <td class="center">${order.state}</td>
          <td class="center"><mt:time time="${order.createTime}" /></td>
          <td class="center"><mt:date time="${order.returnTime}" /></td>
          <td class="center">
            <form action="cancelorder" method="post">
	          <input type="hidden" name="orderid" value="${order.id}" >
              <button type="submit" class="cancelorderbtn" <mt:cancelbutton state="${order.state}" />>Cancel</button>
            </form>
          </td>
        </tr>
        </c:otherwise>
        </c:choose>
      </c:forEach>
    </table>
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
  
</div>
</body>
</html>
