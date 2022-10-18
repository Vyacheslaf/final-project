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
	  <input type="text" name="search" placeholder="Search...">
	</form>
<%@include file="/WEB-INF/jspf/reader_menu.jspf" %>  
  </header>
  <div class="main">
    <table class="book-table">
      <tr class="books-header">
        <c:if test="${not empty search}">
          <c:if test="${sortBy == 'author' && sortType == 'asc'}">
            <td class="author"><a href="reader?search=${search}&sortBy=author&sortType=desc">Author</a></td>
          </c:if>
          <c:if test="${sortBy != 'author' || sortType != 'asc'}">
            <td class="author"><a href="reader?search=${search}&sortBy=author&sortType=asc">Author</a></td>
          </c:if>
          <c:if test="${sortBy == 'title' && sortType == 'asc'}">
            <td class="title"><a href="reader?search=${search}&sortBy=title&sortType=desc">Title</a></td>
          </c:if>
          <c:if test="${sortBy != 'title' || sortType != 'asc'}">
            <td class="title"><a href="reader?search=${search}&sortBy=title&sortType=asc">Title</a></td>
          </c:if>
          <c:if test="${sortBy == 'publication' && sortType == 'asc'}">
            <td class="publication"><a href="reader?search=${search}&sortBy=publication&sortType=desc">Publication</a></td>
          </c:if>
          <c:if test="${sortBy != 'publication' || sortType != 'asc'}">
            <td class="publication"><a href="reader?search=${search}&sortBy=publication&sortType=asc">Publication</a></td>
          </c:if>
          <c:if test="${sortBy == 'year' && sortType == 'asc'}">
            <td class="year"><a href="reader?search=${search}&sortBy=year&sortType=desc">Year</a></td>
          </c:if>
          <c:if test="${sortBy != 'year' || sortType != 'asc'}">
            <td class="year"><a href="reader?search=${search}&sortBy=year&sortType=asc">Year</a></td>
          </c:if>
          <td class="button"></td>
        </c:if>
        <c:if test="${empty search}">
          <c:if test="${sortBy == 'author' && sortType == 'asc'}">
            <td class="author"><a href="reader?sortBy=author&sortType=desc">Author</a></td>
          </c:if>
          <c:if test="${sortBy != 'author' || sortType != 'asc'}">
            <td class="author"><a href="reader?sortBy=author&sortType=asc">Author</a></td>
          </c:if>
          <c:if test="${sortBy == 'title' && sortType == 'asc'}">
            <td class="title"><a href="reader?sortBy=title&sortType=desc">Title</a></td>
          </c:if>
          <c:if test="${sortBy != 'title' || sortType != 'asc'}">
            <td class="title"><a href="reader?sortBy=title&sortType=asc">Title</a></td>
          </c:if>
          <c:if test="${sortBy == 'publication' && sortType == 'asc'}">
            <td class="publication"><a href="reader?sortBy=publication&sortType=desc">Publication</a></td>
          </c:if>
          <c:if test="${sortBy != 'publication' || sortType != 'asc'}">
            <td class="publication"><a href="reader?sortBy=publication&sortType=asc">Publication</a></td>
          </c:if>
          <c:if test="${sortBy == 'year' && sortType == 'asc'}">
            <td class="year"><a href="reader?sortBy=year&sortType=desc">Year</a></td>
          </c:if>
          <c:if test="${sortBy != 'year' || sortType != 'asc'}">
            <td class="year"><a href="reader?sortBy=year&sortType=asc">Year</a></td>
          </c:if>
          <td class="button"></td>
        </c:if>
      <c:forEach items="${books}" var="book">
        <tr class="books">
          <td class="center">${book.author}</td>
          <td class="center">${book.title}</td>
          <td class="center">${book.publication}</td>
          <td class="center">${book.publicationYear}</td>
          <td class="center">
            <form action="neworder" method="post">
	          <input type="hidden" name="bookid" value="${book.id}" >
	          <c:if test="${!sessionScope.user.blocked && book.available > 0}">
                <button type="submit" class="orderbtn">Order a book</button>
              </c:if>
	          <c:if test="${book.available == 0}">
                <div>Not available</div>
              </c:if>
	          <c:if test="${sessionScope.user.blocked}">
                <div class="blocked-account"> Your account is blocked!</div>
              </c:if>
            </form>
          </td>
        </tr>
      </c:forEach>
    </table>
    <mtl:pagination nextPage="${nextPage}" previousPage="${prevPage}" currentPage="${page}" 
    				searchText="${search}" sortBy="${sortBy}" sortType="${sortType}"/>

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
