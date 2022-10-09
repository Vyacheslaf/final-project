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
          <td class="center"><a href="reader?text=${text}&sortBy=author">Author</a></td>
          <td class="center"><a href="reader?text=${text}&sortBy=title">Title</a></td>
          <td class="center"><a href="reader?text=${text}&sortBy=publication">Publication</a></td>
          <td class="center"><a href="reader?text=${text}&sortBy=year">Year</a></td>
          <td></td>
      <c:forEach items="${books}" var="book">
        <tr class="books">
          <td class="center">${book.author}</td>
          <td class="center">${book.title}</td>
          <td class="center">${book.publication}</td>
          <td class="center">${book.publicationYear}</td>
          <td class="center">
            <form action="neworder" method="post">
	          <input type="hidden" name="bookid" value="${book.id}" >
	          <c:if test="${!sessionScope.user.blocked}">
                <button type="submit" class="orderbtn">Order a book</button>
              </c:if>
	          <c:if test="${sessionScope.user.blocked}">
                <div class="blocked-account"> Your account is blocked!</div>
              </c:if>
            </form>
          </td>
        </tr>
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
