<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@page import="it.data.Contatto"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Lista Contatti</title>
</head>
<body>
<h1>Lista di tutti i contatti</h1>
<% 
List<Object[]> contatti = (List<Object[]>) request.getAttribute("contatti");
for(int i=0; i<contatti.size(); i++) { 
%>
<p>Contatto <%=i %></p>
<p>Nome: <%=contatti.get(i)[0] %></p>
<p>Cognome: <%=contatti.get(i)[1] %></p>
<p>Email: <%=contatti.get(i)[2] %></p>
<p>Numeri di telefono: <%=contatti.get(i)[3] %></p>
<br>
<% } %>
<hr>
<a href="http://localhost:8080/ProgettoSettimana7/index.html">HomePage</a>
</body>
</html>