<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://example.org/taglib" prefix="mtl" %>
<%@ taglib tagdir="/WEB-INF/tags" prefix="mt" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
        <c:if test="${orders.size() == 0}">
          <div class="modal-content-orderinfo">
            <div class="container">
              <h4 class="pass-center"><fmt:message key='orders.not.found'/></h4>
            </div>
          </div>
        </c:if>
        <c:if test="${orders.size() != 0}">
          <table class="book-table">
            <tr class="books-header">
              <td class="center"><fmt:message key='order'/></td>
		      <td class="title"><fmt:message key='book'/></td>
              <td class="center"><fmt:message key='status'/></td>
              <td class="center"><fmt:message key='create.date'/></td>
              <td class="center"><fmt:message key='return.date'/></td>
              <td class="button"></td>
            </tr>
            <c:forEach items="${orders}" var="order">
              <c:choose>
                <c:when test="${order.state == 'PROCESSED'}">
                  <c:if test="${order.fine != 0}">
                    <tr class="orders-fined">
                      <td class="center">${order.id}</td>
                      <td>
                        <div class="left">
                          ${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}
                        </div>
                      </td>
                      <td class="center"><fmt:message key='${fn:toLowerCase(order.state)}'/></td>
                      <td class="center"><mt:time time="${order.createTime}" /></td>
                      <td class="center"><mt:date time="${order.returnTime}" /></td>
                      <td class="center">
                        <form action="cancelorder" method="post">
                          <input type="hidden" name="orderid" value="${order.id}" >
                          <button type="submit" class="cancelorderbtn" <mt:cancelbutton state="${order.state}" />>
                            <fmt:message key='cancel'/>
                          </button>
                        </form>
                      </td>
                    </tr>
                  </c:if>
                  <c:if test="${order.fine == 0}">
                    <tr class="orders-active">
                      <td class="center">${order.id}</td>
                      <td>
                        <div class="left">
                          ${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}
                        </div>
                      </td>
                      <td class="center"><fmt:message key='${fn:toLowerCase(order.state)}'/></td>
                      <td class="center"><mt:time time="${order.createTime}" /></td>
                      <td class="center"><mt:date time="${order.returnTime}" /></td>
                      <td class="center">
                        <form action="cancelorder" method="post">
                          <input type="hidden" name="orderid" value="${order.id}" >
                          <button type="submit" class="cancelorderbtn" <mt:cancelbutton state="${order.state}" />>
                            <fmt:message key='cancel'/>
                          </button>
                        </form>
                      </td>
                    </tr>
                  </c:if>
                </c:when>
                <c:when test="${order.state == 'NEW'}">
                  <tr class="orders">
                    <td class="center">${order.id}</td>
                    <td>
                      <div class="left">
                        ${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}
                      </div>
                    </td>
                    <td class="center"><fmt:message key='${fn:toLowerCase(order.state)}'/></td>
                    <td class="center"><mt:time time="${order.createTime}" /></td>
                    <td class="center"><mt:date time="${order.returnTime}" /></td>
                    <td class="center">
                      <form action="cancelorder" method="post">
                        <input type="hidden" name="orderid" value="${order.id}" >
                        <button type="submit" class="cancelorderbtn" <mt:cancelbutton state="${order.state}" />>
                          <fmt:message key='cancel'/>
                        </button>
                      </form>
                    </td>
                  </tr>
                </c:when>
                <c:otherwise>
                  <tr class="orders-inactive">
                    <td class="center">${order.id}</td>
                    <td>
                      <div class="left">
                        ${order.book.author}, ${order.book.title}, ${order.book.publication}, ${order.book.publicationYear}
                      </div>
                    </td>
                    <td class="center"><fmt:message key='${fn:toLowerCase(order.state)}'/></td>
                    <td class="center"><mt:time time="${order.createTime}" /></td>
                    <td class="center"><mt:date time="${order.returnTime}" /></td>
                    <td class="center">
                      <form action="cancelorder" method="post">
                        <input type="hidden" name="orderid" value="${order.id}" >
                        <button type="submit" class="cancelorderbtn" <mt:cancelbutton state="${order.state}" />>
                          <fmt:message key='cancel'/>
                        </button>
                      </form>
                    </td>
                  </tr>
                </c:otherwise>
              </c:choose>
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
