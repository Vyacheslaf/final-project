<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://example.org/taglib" prefix="mtl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="lang" value="${not empty param.lang ? param.lang : not empty lang ? lang : 'en'}" scope="session" />
<fmt:setLocale value="${lang}" />
<fmt:setBundle basename="i18n.text" />

<!DOCTYPE html>
<html>
  <head>
    <title><fmt:message key='library'/></title>
    <link rel="stylesheet" href="css/style.css" />
  </head>
  <body>
    <div class="f-container fixed-hf">
      <header>
        <a class="home" href="start">
          <img alt="Library" src="img/logo.svg">
	    </a>
	    <form action="findbook" method="get">
	      <input type="text" name="isbn" placeholder="<fmt:message key='find.by.isbn'/>">
	    </form>
        <%@include file="/WEB-INF/jspf/admin_menu.jspf" %>  
      </header>
      <div class="main">
        <div class="modal-content-bookinfo">
          <form action="changebook" method="post">
            <div class="container">
              <table class="bookinfo-table">
                <tr>
                  <td class="right-field">ISBN:</td>
                  <td><input type="text" name="isbn" id="isbn" value="${requestScope.book.isbn}" required></td>
                </tr>
                <tr>
                  <td class="right"><fmt:message key='author'/>:</td>
                  <td>
                    <input list="author-list" name="author" id="author" class="datalist-field" value="${requestScope.book.author}" required>
                    <datalist id="author-list">
                      <c:forEach items="${requestScope.authors}" var="author">
                        <option value="${author.fullName}">
                      </c:forEach>
                    </datalist>
                  </td>
                </tr>
                <tr>
                  <td class="right"><fmt:message key='title'/>:</td>
                  <td><input type="text" name="title" id="title" value="${requestScope.book.title}" required></td>
                </tr>
                <tr>
                  <td class="right"><fmt:message key='publication'/>:</td>
                  <td>
                    <input list="publications-list" name="publication" id="publication" class="datalist-field" value="${requestScope.book.publication}" required>
                    <datalist id="publications-list">
                      <c:forEach items="${requestScope.publications}" var="publisher">
                        <option value="${publisher.publicationName}">
                      </c:forEach>
                    </datalist>
                  </td>
                </tr>
                <tr>
                  <td class="right"><fmt:message key='publication.year'/>:</td>
                  <td>
                    <input type="number" name="year" id="year" class="number-field" value="${requestScope.book.publicationYear}" required>
                  </td>
                </tr>
                <tr>
                  <td class="right"><fmt:message key='quantity'/>:</td>
                  <td>
                    <input type="number" name="quantity" id="quantity" class="number-field" value="${requestScope.book.quantity}" required>
                  </td>
                </tr>
              </table>
              <br>
              <br>
              <input type="hidden" name="bookid" value="${requestScope.book.id}" >
              <button type="submit" id="changebookbtn" class="changebook"><fmt:message key='save.book'/></button>
            </div>
          </form>
          <form action="deletebook" method="post">
            <div class="container">
              <input type="hidden" name="bookid" value="${requestScope.book.id}" >
              <button type="submit" id="deletebookbtn" class="deletebook"><fmt:message key='delete.book'/></button>
            </div>
          </form>
        </div>
      </div>
      <footer>
        <div></div>
        <div>
          <a href="?isbn=${requestScope.book.isbn}&lang=en">${applicationScope.locales.en}</a>
          | 
          <a href="?isbn=${requestScope.book.isbn}&lang=uk">${applicationScope.locales.uk}</a>
        </div>
        <div></div>
      </footer>
      <%@include file="/WEB-INF/jspf/error.jspf" %>
      <%@include file="/WEB-INF/jspf/info.jspf" %>
    </div>
  </body>
</html>
