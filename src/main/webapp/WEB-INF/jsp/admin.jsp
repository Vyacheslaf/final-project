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
	<form action="findbook" method="get">
	  <input type="text" name="isbn" placeholder="find a book by ISBN...">
	</form>
<%@include file="/WEB-INF/jspf/admin_menu.jspf" %>  
  </header>
  <div class="main">
  
  <div class="modal-content-bookinfo">
    <form action="addbook" method="post">
      <div class="container">
      <table class="bookinfo-table">
        <tr>
          <td class="right-field">ISBN:</td>
          <td><input type="text" name="isbn" id="isbn" required></td>
        </tr>
        <tr>
          <td class="right">Author:</td>
          <td>
            <input list="author-list" name="author" id="author" class="datalist-field" required>
            <datalist id="author-list">
              <c:forEach items="${authors}" var="author">
                <option value="${author.fullName}">
              </c:forEach>
            </datalist>
          </td>
        </tr>
        <tr>
          <td class="right">Title:</td>
          <td><input type="text" name="title" id="title" required></td>
        </tr>
        <tr>
          <td class="right">Publication:</td>
          <td>
            <input list="publications-list" name="publication" id="publication" class="datalist-field" required>
            <datalist id="publications-list">
              <c:forEach items="${publications}" var="publisher">
                <option value="${publisher.publicationName}">
              </c:forEach>
            </datalist>
          </td>
        </tr>
        <tr>
          <td class="right">Publication Year:</td>
          <td><input type="number" name="year" id="year" class="number-field" required></td>
        </tr>
        <tr>
          <td class="right">Quantity:</td>
          <td><input type="number" name="quantity" id="quantity" class="number-field" required></td>
        </tr>
      </table>
      <br>
      <br>
        <button type="submit" id="addbookbtn" class="book">Add the book to the catalog</button>
      </div>
    </form>
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
  
<%@include file="/WEB-INF/jspf/error.jspf" %>
<%@include file="/WEB-INF/jspf/info.jspf" %>
  
</div>
</body>
</html>
