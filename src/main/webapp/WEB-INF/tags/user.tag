<%@tag import="org.example.controller.Constants"%>
<%@tag import="org.example.entity.User"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

Hi, <%= ((User)session.getAttribute(Constants.SESSION_ATTRIBUTE_USER)).getFirstName() %>!
