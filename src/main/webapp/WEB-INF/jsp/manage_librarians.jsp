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
        <a class="register" onclick='register();'><fmt:message key='register.librarian'/></a>
        <%@include file="/WEB-INF/jspf/admin_menu.jspf" %>  
      </header>
      <div class="main">
        <c:if test="${requestScope.librarians.size() == 0}">
          <div class="modal-content-orderinfo">
            <div class="container">
              <h4 class="pass-center"><fmt:message key='librarians.not.found'/></h4>
            </div>
          </div>
        </c:if>
        <c:if test="${requestScope.librarians.size() != 0}">
          <table class="book-table">
            <tr class="books-header">
              <td class="center"><fmt:message key='librarian.id'/></td>
		      <td class="title"><fmt:message key='librarian.name'/></td>
              <td class="center"><fmt:message key='librarian.phone'/></td>
              <td class="center"><fmt:message key='email'/></td>
              <td></td>
            </tr>
            <c:forEach items="${librarians}" var="librarian">
              <tr class="orders-active">
                <td class="center">${librarian.id}</td>
		        <td class="center">${librarian.firstName} ${librarian.lastName}</td>
                <td class="center">${librarian.phoneNumber}</td>
                <td class="center">${librarian.email}</td>
                <td class="center">
                  <form action="deleteuser" method="post">
	                <input type="hidden" name="userid" value="${librarian.id}" >
                    <button type="submit" class="deleteuser"><fmt:message key='delete'/></button>
                  </form>
                </td>
              </tr>
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
    </div>
    <div id="id01" class="modal"></div> 
    <div id="id02" class="modal">
      <form class="modal-content-reg" action="register" method="post">
        <div class="container">
          <input type="email" placeholder="<fmt:message key='enter.email'/>" name="email" id="email" onkeyup='checkRegData();' required>
          <input type="password" placeholder="<fmt:message key='enter.password'/>" name="password" id="password" onkeyup='checkRegData();' required>
          <input type="password" placeholder="<fmt:message key='confirm.password'/>" name="confirm" id="confirm" onkeyup='checkRegData();' required>
          <input type="text" placeholder="<fmt:message key='enter.firstname'/>" name="firstname" id="firstname" onkeyup='checkRegData();' required>
          <input type="text" placeholder="<fmt:message key='enter.lastname'/>" name="lastname" id="lastname" onkeyup='checkRegData();' required>
          <input type="text" placeholder="<fmt:message key='enter.phone'/>" name="phone" id="phone" onkeyup='checkRegData();' required>
          <input type="text" placeholder="<fmt:message key='enter.document'/>" name="passport" id="passport" onkeyup='checkRegData();' required>
          <input type="hidden" name="role" value="librarian" >
          <button id='regbtn' type="submit"><fmt:message key='register'/></button>
        </div>
      </form>
    </div> 
    <%@include file="/WEB-INF/jspf/error.jspf" %>
    <%@include file="/WEB-INF/jspf/info.jspf" %>
    <script src="js/modal_close.js"></script>
    <script src="js/input_checks.js"></script>
    <script src="js/register_modal.js"></script>
    <script>checkRegData();</script>
  </body>
</html>
