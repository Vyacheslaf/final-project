<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="time" required="true" type="java.time.LocalDateTime" %>

<% 
if (time == null) {
	out.print("");
} else {
	out.print(time.toString().replaceAll("T", " "));
}
%>