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
        <c:if test="${requestScope.order.state == 'NEW'}">
          <div class="modal-content-orderinfo">
            <div class="container">
              <table class="order-table">
                <tr class="order-info">
                  <td class="order-right">ISBN:</td>
                  <td class="order">${requestScope.order.book.isbn}</td>
                </tr>
                <tr class="order-info">
                  <td class="order-right"><fmt:message key='book'/>:</td>
                  <td class="order">
                    ${requestScope.order.book.author}, ${requestScope.order.book.title}, 
          			${requestScope.order.book.publication}, ${requestScope.order.book.publicationYear}
          		  </td>
                </tr>
                <tr class="order-info">
                  <td class="order-right"><fmt:message key='reader.name'/>:</td>
                  <td class="order">${requestScope.order.user.firstName} ${requestScope.order.user.lastName}</td>
                </tr>
                <tr class="order-info">
                  <td class="order-right"><fmt:message key='reader.document'/>:</td>
                  <td class="order">${requestScope.order.user.passportNumber}</td>
                </tr>
                <tr class="order-info">
                  <td class="order-right"><fmt:message key='reader.phone'/>:</td>
                  <td class="order">${requestScope.order.user.phoneNumber}</td>
                </tr>
              </table>
              <c:if test="${requestScope.order.user.blocked == false}"> 
                <c:if test="${requestScope.order.book.available > 0}">
	              <form action="givebook" method="post">
	                <div class="container-button">
	                  <input type="hidden" name="orderid" value="${requestScope.order.id}" >
	                  <button type="submit" class="givebook-active"><fmt:message key='give.book.reading.room'/></button>
	                </div>
	              </form>
	              <form action="givebook" method="post">
	                <div class="container-button">
	                  <input type="hidden" name="orderid" value="${requestScope.order.id}" >
	                  <input type="hidden" name="onsubscription" value="true" >
	                  <button type="submit" class="givebook-active"><fmt:message key='give.book.subscription'/></button>
	                </div>
	              </form>
                </c:if>
                <c:if test="${requestScope.order.book.available == 0}">
	              <form action="givebooktoroom" method="post">
	                <div class="container-button">
	                  <button type="submit" disabled><fmt:message key='book.not.available'/></button>
	                </div>
	              </form>
	              <form action="givebook" method="post">
	                <div class="container-button">
	                  <button type="submit" disabled><fmt:message key='book.not.available'/></button>
	                </div>
	              </form>
                </c:if>
              </c:if>
              <c:if test="${requestScope.order.user.blocked == true}">  
                <form action="givebook" method="post">
                  <div class="container-button">
                    <button type="submit" disabled><fmt:message key='reader.is.blocked'/></button>
                  </div>
                </form>
                <form action="givebook" method="post">
                  <div class="container-button">
                    <button type="submit" disabled><fmt:message key='reader.is.blocked'/></button>
                  </div>
                </form>
              </c:if>
              <form action="cancelorder" method="post">
                <div class="container-button">
                  <input type="hidden" name="orderid" value="${requestScope.order.id}" >
                  <button type="submit" class="cancelorder"><fmt:message key='cancel.order'/></button>
                </div>
              </form>
            </div>
          </div>
        </c:if>
        <c:if test="${requestScope.order.state == 'PROCESSED'}">
          <div class="modal-content-orderinfo">
            <div class="container">
              <table class="order-table">
                <tr class="order-info">
                  <td class="order-right">ISBN:</td>
                  <td class="order">${requestScope.order.book.isbn}</td>
                </tr>
                <tr class="order-info">
                  <td class="order-right"><fmt:message key='book'/>:</td>
                  <td class="order">${requestScope.order.book.author}, ${requestScope.order.book.title}, 
             						${requestScope.order.book.publication}, ${requestScope.order.book.publicationYear}</td>
                </tr>
                <tr class="order-info">
                  <td class="order-right"><fmt:message key='reader.name'/>:</td>
                  <td class="order">${requestScope.order.user.firstName} ${requestScope.order.user.lastName}</td>
                </tr>
                <tr class="order-info">
                  <td class="order-right"><fmt:message key='reader.document'/>:</td>
                  <td class="order">${requestScope.order.user.passportNumber}</td>
                </tr>
                <tr class="order-info">
                  <td class="order-right"><fmt:message key='reader.phone'/>:</td>
                  <td class="order">${requestScope.order.user.phoneNumber}</td>
                </tr>
                <tr class="order-info">
                  <td class="order-right"><fmt:message key='return.date'/>:</td>
                  <td class="order"><mt:date time="${requestScope.order.returnTime}"></mt:date></td>
                </tr>
                <c:if test="${requestScope.order.fine != 0}">
                  <tr class="order-info-fined">
                    <td class="order-right"><fmt:message key='fine'/>:</td>
                    <td class="order">${requestScope.order.fine} <fmt:message key='currency.uah'/></td>
                  </tr>
                </c:if>
                <c:if test="${requestScope.order.fine == 0}">
                  <tr class="order-info">
                    <td class="order-right"><fmt:message key='fine'/>:</td>
                    <td class="order">${requestScope.order.fine} <fmt:message key='currency.uah'/></td>
                  </tr>
                </c:if>
              </table>
              <form action="completeorder" method="post">
                <div class="container-button">
                  <input type="hidden" name="orderid" value="${requestScope.order.id}" >
                  <input type="hidden" name="readerid" value="${requestScope.order.user.id}" >
                  <c:if test="${requestScope.order.fine != 0}">
                    <button type="submit" class="cancelorder"><fmt:message key='return.and.fine'/></button>
                  </c:if>
                  <c:if test="${requestScope.order.fine == 0}">
                    <button type="submit" class="givebook-active"><fmt:message key='return.book'/></button>
                  </c:if>
                </div>
              </form>
            </div>
          </div>
        </c:if>
      </div>
      <footer>
        <div></div>
        <div>
          <a href="?orderid=${requestScope.order.id}&lang=en">english</a>
          | 
          <a href="?orderid=${requestScope.order.id}&lang=uk">українська</a>
    </div>
    <div></div>
  </footer>
  
<%@include file="/WEB-INF/jspf/error.jspf" %>
<%@include file="/WEB-INF/jspf/info.jspf" %>
  
</div>
</body>
</html>
