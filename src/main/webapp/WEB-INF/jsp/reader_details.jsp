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
              <h4 class="pass-center"><fmt:message key='actual.orders.not.found'/></h4>
            </div>
          </div>
        </c:if>
        <c:if test="${requestScope.orders.size() != 0}">
          <div class="modal-content-readerinfo">
            <div class="container">
              <h4 class="pass-center">
                <fmt:message key='actual.orders.list'/> 
                ${requestScope.orders[0].user.firstName} ${requestScope.orders[0].user.lastName}
              </h4>
            </div>
          </div>
          <table class="book-table">
            <tr class="books-header">
              <td class="center"><fmt:message key='order'/></td>
		      <td class="center"><fmt:message key='book'/></td>
              <td class="center"><fmt:message key='return.date'/></td>
              <td class="center"><fmt:message key='fine'/></td>
              <td></td>
            </tr>
            <c:forEach items="${requestScope.orders}" var="order">
              <c:if test="${order.fine == 0}">
                <c:if test="${order.state == 'PROCESSED'}">
                  <tr class="orders-active">
                    <td class="center">${order.id}</td>
                    <td class="center">
                      ${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}
                    </td>
                    <td class="center"><mt:date time="${order.returnTime}" /></td>
                    <td class="center">${order.fine} <fmt:message key='currency.uah'/></td>
                    <td class="button">
                      <a href="orderdetails?orderid=${order.id}"><fmt:message key='order.details'/></a>
                    </td>
                  </tr>
                </c:if>
                <c:if test="${order.state == 'NEW'}">
                  <tr class="orders">
                    <td class="center">${order.id}</td>
                    <td class="center">
                      ${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}
                    </td>
                    <td class="center"><fmt:message key='not.issued.yet'/></td>
                    <td class="center">${order.fine} <fmt:message key='currency.uah'/></td>
                    <td class="button">
                      <a href="orderdetails?orderid=${order.id}"><fmt:message key='order.details'/></a>
                    </td>
                  </tr>
                </c:if>
              </c:if>
              <c:if test="${order.fine != 0}">
                <tr class="orders-fined">
                  <td class="center">${order.id}</td>
                  <td class="center">
                    ${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}
                  </td>
                  <td class="center"><mt:date time="${order.returnTime}" /></td>
                  <td class="center">${order.fine} <fmt:message key='currency.uah'/></td>
                  <td class="button">
                    <a href="orderdetails?orderid=${order.id}"><fmt:message key='order.details'/></a>
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
          <a href="?readerid=${requestScope.readerid}&lang=en">english</a>
          | 
          <a href="?readerid=${requestScope.readerid}&lang=uk">українська</a>
        </div>
        <div></div>
      </footer>
      <%@include file="/WEB-INF/jspf/error.jspf" %>
      <%@include file="/WEB-INF/jspf/info.jspf" %>
    </div>
  </body>
</html>
