<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://example.org/taglib" prefix="mtl" %>
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
        <a class="active" onclick="document.getElementById('id01').style.display='block'"><fmt:message key="login" /></a>
      </header>
      <div class="main">
        <table class="book-table">
          <tr class="books-header">
            <td class="author"><fmt:message key="author"/></td>
            <td class="title"><fmt:message key="title"/></td>
            <td class="publication"><fmt:message key="publication"/></td>
            <td class="year"><fmt:message key="year"/></td>
            <td class="button"></td>
          </tr>
          <c:forEach items="${books}" var="book">
            <tr class="books">
              <td class="center">${book.author}</td>
              <td class="center">${book.title}</td>
              <td class="center">${book.publication}</td>
              <td class="center">${book.publicationYear}</td>
              <td class="center"><fmt:message key="need.to.login"/></td>
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
          <a href="?lang=en">english</a>
          | 
          <a href="?lang=uk">українська</a>
        </div>
        <div></div>
      </footer>
    </div> 
    <div id="id01" class="modal">
      <form class="modal-content" action="login" method="post">
        <div class="container">
          <input type="email" placeholder="<fmt:message key='enter.email'/>" name="email" required>
          <input type="password" placeholder="<fmt:message key='enter.password'/>" name="password" required>
          <button type="submit"><fmt:message key='login'/></button>
        </div>
        <div class="container" style="background-color:#f1f1f1">
          <button type="button" onclick="document.getElementById('id01').style.display='none'" class="cancelbtn">
            <fmt:message key='cancel'/>
          </button>
          <span class="psw"><a class="pointer" onclick='register();'><fmt:message key='register'/></a></span>
        </div>
      </form>
    </div> 
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
          <input type="hidden" name="role" value="reader" >
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