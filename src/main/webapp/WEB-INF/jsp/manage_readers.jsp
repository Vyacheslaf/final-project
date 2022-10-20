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
        <%@include file="/WEB-INF/jspf/admin_menu.jspf" %>  
      </header>
      <div class="main">
        <c:if test="${requestScope.readers.size() == 0}">
          <div class="modal-content-orderinfo">
            <div class="container">
              <h4 class="pass-center"><fmt:message key='readers.not.found'/></h4>
            </div>
          </div>
        </c:if>
        <c:if test="${requestScope.readers.size() != 0}">
          <table class="book-table">
            <tr class="books-header">
              <td class="center"><fmt:message key='reader.id'/></td>
		      <td class="center"><fmt:message key='reader.name'/></td>
              <td class="center"><fmt:message key='manage.reader.phone'/></td>
              <td class="center"><fmt:message key='email'/></td>
              <td class="center"><fmt:message key='fine'/></td>
              <td class="center"><fmt:message key='status'/></td>
              <td></td>
            </tr>
            <c:forEach items="${requestScope.readers}" var="reader">
              <c:if test="${!reader.blocked}">
                <c:if test="${reader.fine > 0}">
                  <tr class="orders-fined">
                    <td class="center">${reader.id}</td>
                    <td class="center">${reader.firstName} ${reader.lastName}</td>
                    <td class="center">${reader.phoneNumber}</td>
                    <td class="center">${reader.email}</td>
                    <td class="center">${reader.fine} <fmt:message key='currency.uah'/></td>
                    <td class="center"><fmt:message key='unblocked'/></td>
                    <td class="center">
                      <form action="blockreader" method="post">
	                    <input type="hidden" name="id" value="${reader.id}" >
	                    <input type="hidden" name="blocked" value="true" >
                        <button type="submit" class="deleteuser"><fmt:message key='block'/></button>
                      </form>
                    </td>
                  </tr>
                </c:if>
                <c:if test="${reader.fine == 0}">
                  <tr class="orders-active">
                    <td class="center">${reader.id}</td>
	                <td class="center">${reader.firstName} ${reader.lastName}</td>
                    <td class="center">${reader.phoneNumber}</td>
                    <td class="center">${reader.email}</td>
                    <td class="center">${reader.fine} <fmt:message key='currency.uah'/></td>
                    <td class="center"><fmt:message key='unblocked'/></td>
                    <td class="center">
                      <form action="blockreader" method="post">
	                    <input type="hidden" name="id" value="${reader.id}" >
	                    <input type="hidden" name="blocked" value="true" >
                        <button type="submit" class="deleteuser"><fmt:message key='block'/></button>
                      </form>
                    </td>
                  </tr>
                </c:if>
              </c:if>
              <c:if test="${reader.blocked}">
                <tr class="orders-inactive">
                  <td class="center">${reader.id}</td>
	              <td class="center">${reader.firstName} ${reader.lastName}</td>
                  <td class="center">${reader.phoneNumber}</td>
                  <td class="center">${reader.email}</td>
                  <td class="center">${reader.fine} <fmt:message key='currency.uah'/></td>
                  <td class="center"><fmt:message key='blocked'/></td>
                  <td class="center">
                    <form action="blockreader" method="post">
	                  <input type="hidden" name="id" value="${reader.id}" >
                      <button type="submit" class="unblockuser"><fmt:message key='unblock'/></button>
                    </form>
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
          <a href="?phonenumber=${phonenumber}&lang=en">english</a>
          | 
          <a href="?phonenumber=${phonenumber}&lang=uk">українська</a>
        </div>
        <div></div>
      </footer>
    </div>
    <%@include file="/WEB-INF/jspf/error.jspf" %>
    <%@include file="/WEB-INF/jspf/info.jspf" %>
  </body>
</html>
