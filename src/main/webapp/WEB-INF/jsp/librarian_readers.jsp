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
	    <form action="findreader" method="get">
	      <input type="text" name="phonenumber" placeholder="<fmt:message key='find.reader.by.phone'/>">
	    </form>
        <%@include file="/WEB-INF/jspf/librarian_menu.jspf" %>  
      </header>
      <div class="main">
        <c:if test="${requestScope.orders.size() == 0}">
          <div class="modal-content-orderinfo">
            <div class="container">
              <h4 class="pass-center"><fmt:message key='issued.books.not.found'/></h4>
            </div>
          </div>
        </c:if>
        <c:if test="${requestScope.orders.size() != 0}">
          <table class="book-table">
            <tr class="books-header">
              <td class="center"><fmt:message key='reader.id'/></td>
              <td class="title"><fmt:message key='reader.name'/></td>
              <td class="center"><fmt:message key='fine'/></td>
              <td></td>
            </tr>
            <c:forEach items="${requestScope.orders}" var="order">
              <c:if test="${order.fine != 0}">
                <tr class="orders-fined">
                  <td class="center">${order.user.id}</td>
		          <td class="center">${order.user.firstName} ${order.user.lastName}</td>
                  <td class="center">${order.fine} <fmt:message key='currency.uah'/></td>
                  <td class="button">
                    <a href="readerdetails?readerid=${order.user.id}"><fmt:message key='orders.of.reader'/></a>
                  </td>
                </tr>
              </c:if>
              <c:if test="${order.fine == 0}">
                <tr class="orders-active">
                  <td class="center">${order.user.id}</td>
		          <td class="center">${order.user.firstName} ${order.user.lastName}</td>
                  <td class="center">${order.fine} <fmt:message key='currency.uah'/></td>
                  <td class="button">
                    <a href="readerdetails?readerid=${order.user.id}"><fmt:message key='orders.of.reader'/></a>
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
          <a href="?lang=en">${applicationScope.locales.en}</a>
          | 
          <a href="?lang=uk">${applicationScope.locales.uk}</a>
        </div>
        <div></div>
      </footer>
      <%@include file="/WEB-INF/jspf/error.jspf" %>
      <%@include file="/WEB-INF/jspf/info.jspf" %>
    </div>
  </body>
</html>
