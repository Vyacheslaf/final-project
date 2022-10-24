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
        <form action="reader" method="get">
	      <input type="text" name="search" placeholder="<fmt:message key='reader.book.search'/>">
	    </form>
        <%@include file="/WEB-INF/jspf/reader_menu.jspf" %>  
      </header>
      <div class="main">
        <table class="book-table">
          <tr class="books-header">
            <c:if test="${not empty search}">
              <c:if test="${sortBy == 'author' && sortType == 'asc'}">
                <td class="author">
                  <a href="reader?search=${search}&sortBy=author&sortType=desc"><fmt:message key="author"/></a>
                </td>
              </c:if>
              <c:if test="${sortBy != 'author' || sortType != 'asc'}">
                <td class="author">
                  <a href="reader?search=${search}&sortBy=author&sortType=asc"><fmt:message key="author"/></a>
                </td>
              </c:if>
              <c:if test="${sortBy == 'title' && sortType == 'asc'}">
                <td class="title">
                  <a href="reader?search=${search}&sortBy=title&sortType=desc"><fmt:message key="title"/></a>
                </td>
              </c:if>
              <c:if test="${sortBy != 'title' || sortType != 'asc'}">
                <td class="title">
                  <a href="reader?search=${search}&sortBy=title&sortType=asc"><fmt:message key="title"/></a>
                </td>
              </c:if>
              <c:if test="${sortBy == 'publication' && sortType == 'asc'}">
                <td class="publication">
                  <a href="reader?search=${search}&sortBy=publication&sortType=desc"><fmt:message key="publication"/></a>
                </td>
              </c:if>
              <c:if test="${sortBy != 'publication' || sortType != 'asc'}">
                <td class="publication">
                  <a href="reader?search=${search}&sortBy=publication&sortType=asc"><fmt:message key="publication"/></a>
                </td>
              </c:if>
              <c:if test="${sortBy == 'year' && sortType == 'asc'}">
                <td class="year">
                  <a href="reader?search=${search}&sortBy=year&sortType=desc"><fmt:message key="year"/></a>
                </td>
              </c:if>
              <c:if test="${sortBy != 'year' || sortType != 'asc'}">
                <td class="year">
                  <a href="reader?search=${search}&sortBy=year&sortType=asc"><fmt:message key="year"/></a>
                </td>
              </c:if>
              <td class="button"></td>
            </c:if>
            <c:if test="${empty search}">
              <c:if test="${sortBy == 'author' && sortType == 'asc'}">
                <td class="author"><a href="reader?sortBy=author&sortType=desc"><fmt:message key="author"/></a></td>
              </c:if>
              <c:if test="${sortBy != 'author' || sortType != 'asc'}">
                <td class="author"><a href="reader?sortBy=author&sortType=asc"><fmt:message key="author"/></a></td>
              </c:if>
              <c:if test="${sortBy == 'title' && sortType == 'asc'}">
                <td class="title"><a href="reader?sortBy=title&sortType=desc"><fmt:message key="title"/></a></td>
              </c:if>
              <c:if test="${sortBy != 'title' || sortType != 'asc'}">
                <td class="title"><a href="reader?sortBy=title&sortType=asc"><fmt:message key="title"/></a></td>
              </c:if>
              <c:if test="${sortBy == 'publication' && sortType == 'asc'}">
                <td class="publication">
                  <a href="reader?sortBy=publication&sortType=desc"><fmt:message key="publication"/></a>
                </td>
              </c:if>
              <c:if test="${sortBy != 'publication' || sortType != 'asc'}">
                <td class="publication">
                  <a href="reader?sortBy=publication&sortType=asc"><fmt:message key="publication"/></a>
                </td>
              </c:if>
              <c:if test="${sortBy == 'year' && sortType == 'asc'}">
                <td class="year"><a href="reader?sortBy=year&sortType=desc"><fmt:message key="year"/></a></td>
              </c:if>
              <c:if test="${sortBy != 'year' || sortType != 'asc'}">
                <td class="year"><a href="reader?sortBy=year&sortType=asc"><fmt:message key="year"/></a></td>
              </c:if>
              <td class="button"></td>
            </c:if>
          </tr>
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
                    <button type="submit" class="orderbtn"><fmt:message key="order.book"/></button>
                  </c:if>
	              <c:if test="${book.available == 0}">
                    <div><fmt:message key="not.available"/></div>
                  </c:if>
                  <c:if test="${sessionScope.user.blocked}">
                    <div class="blocked-account"><fmt:message key="account.is.blocked"/></div>
                  </c:if>
                </form>
              </td>
            </tr>
          </c:forEach>
        </table>
        <fmt:message key="next.page" var="nextButton" scope="request"/>
        <fmt:message key="previous.page" var="previousButton" scope="request"/>
        <mtl:pagination nextPage="${nextPage}" previousPage="${prevPage}" currentPage="${page}" 
    	   			    searchText="${search}" sortBy="${sortBy}" sortType="${sortType}"
    				    nextButtonText="${nextButton}" previousButtonText="${previousButton}"/>
      </div>
      <footer>
        <div></div>
        <div>
          <a href="?lang=en">${applicationScope.locales.en}</a>
          | 
          <a href="?lang=uk">${applicationScope.locales.uk}</a>
        </div>
        <div></div>
      </footer>
      <%@include file="/WEB-INF/jspf/register_data.jspf" %>  
      <%@include file="/WEB-INF/jspf/error.jspf" %>
      <%@include file="/WEB-INF/jspf/info.jspf" %>
    </div>
  </body>
</html>
