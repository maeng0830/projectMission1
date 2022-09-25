<%@page import="db.WifiMethod"%>
<%@page import="db.WifiHistory"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>위치 히스토리 목록</title>
	<style>
		form {
			margin-top: 20px;
			margin-bottom: 5px;
		}
		table {
			width: 100%;
			text-align: center;
		}		
		th {
			background-color: #8ca7e7;
			color: white;
			font-size: 15px;
			padding-top: 10px;
			padding-bottom: 10px;
		}
		tbody > tr > td {
			font-size: 13px;
			padding-top: 8px;
			padding-bottom: 8px;
		}
		tbody > tr:nth-child(even) {
			background-color: #f3f3f3;			
		}		
		.noInfo td {	
			font-size: x-large;
			padding-top: 15px;
			padding-bottom: 15px;
		}
	</style>
	<script>
		function remove(id) {
			location.href="history-remove.jsp?id="+id;
		}
	</script>
</head>
<body>
	<%
		WifiMethod wifiMethod = new WifiMethod();
		List<WifiHistory> wifiHistoryList = wifiMethod.viewHistory();
	%>
	<h1>위치 히스토리 목록</h1>
	<div>
		<a href="index.jsp">홈</a> | <a href="history.jsp">위치 히스토리 목록</a> | <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
	</div>
	<table>
		<thead>
			<tr>
				<th>ID</th>
				<th>위도</th>
				<th>경도</th>
				<th>조회 일자</th>
				<th>비고</th>
			</tr>
		</thead>	
		<tbody>	
			<%	
			if (wifiHistoryList.size() != 0) {
				for (WifiHistory wifiHistory: wifiHistoryList) {
			%>		
				<tr>
					<td><%=wifiHistory.getId()%></td>
					<td><%=wifiHistory.getCurLat()%></td>
					<td><%=wifiHistory.getCurLnt()%></td>
					<td><%=wifiHistory.getViewDttm()%></td>
					<td><input type="button" value="삭제" onclick="remove('<%=wifiHistory.getId()%>')"></td>
				</tr>			
			<%					
				}
			} else {
			%>
				<tr class="noInfo">
					<td colspan="5">위치 조회 이력이 없습니다.</td>
				</tr>
			<%
			}
			%>
		</tbody>	
	</table>
</body>
</html>