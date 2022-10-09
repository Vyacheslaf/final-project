<%@tag import="org.example.entity.OrderState"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="state" required="true" type="java.lang.String" %>

<% 
if (OrderState.valueOf(state).equals(OrderState.NEW)) {
	out.print("");
} else {
	out.print("disabled");
}
%>