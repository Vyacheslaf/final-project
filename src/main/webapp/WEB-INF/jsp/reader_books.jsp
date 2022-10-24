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
	      <input type="text" name="text" placeholder="<fmt:message key='reader.book.search'/>">
	    </form>
        <%@include file="/WEB-INF/jspf/reader_menu.jspf" %>  
      </header>
      <div class="main">
        <c:if test="${requestScope.orders.size() == 0}">
          <div class="modal-content-orderinfo">
            <div class="container">
              <h4 class="pass-center"><fmt:message key='books.not.found'/></h4>
            </div>
          </div>
        </c:if>
        <c:if test="${requestScope.orders.size() != 0}">
          <table class="book-table">
            <tr class="books-header">
              <td class="center"><fmt:message key='order'/></td>
		      <td class="center"><fmt:message key='book'/></td>
              <td class="center"><fmt:message key='return.date'/></td>
              <td class="center"><fmt:message key='fine'/></td>
            </tr>
            <c:forEach items="${orders}" var="order">
              <c:if test="${order.state == 'PROCESSED'}">
                <c:if test="${order.fine == 0}">
                  <tr class="orders-active">
                    <td class="center">${order.id}</td>
                    <td>
                      <div class="left">
                        ${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}
                      </div>
                    </td>
                    <td class="center"><mt:date time="${order.returnTime}" /></td>
                    <td class="center">${order.fine} <fmt:message key='currency.uah'/></td>
                  </tr>
                </c:if>
                <c:if test="${order.fine != 0}">
                  <tr class="orders-fined">
                    <td class="center">${order.id}</td>
                    <td>
                      <div class="left">
                        ${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}
                      </div>
                    </td>
                    <td class="center"><mt:date time="${order.returnTime}" /></td>
                    <td class="center">${order.fine} <fmt:message key='currency.uah'/></td>
                  </tr>
                </c:if>
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
      <%@include file="/WEB-INF/jspf/register_data.jspf" %>  
      <%@include file="/WEB-INF/jspf/error.jspf" %>
      <%@include file="/WEB-INF/jspf/info.jspf" %>
    </div>
  </body>
</html>
