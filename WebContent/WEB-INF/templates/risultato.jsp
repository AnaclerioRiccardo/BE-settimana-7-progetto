<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Risultato</title>
</head>
<body>
<%
	String messaggio = (String) request.getAttribute("messaggio");
	boolean success = (boolean) request.getAttribute("successo");
%>
<%String colore = (success)?"green":"red"; %>
<p style="padding: 20px; background: <%=colore %>"><%=messaggio %></p>
<hr>
<a href="http://localhost:8080/ProgettoSettimana7/index.html">HomePage</a>
</body>
</html>