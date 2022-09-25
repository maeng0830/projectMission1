<%@page import="db.WifiMethod"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%
		int id = Integer.parseInt(request.getParameter("id"));
		WifiMethod wifiMethod = new WifiMethod();
		wifiMethod.removeHistory(id);
		wifiMethod.readjustHistory();
		response.sendRedirect("history.jsp");
	%>
</body>
</html>