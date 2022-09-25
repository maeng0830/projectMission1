<%@page import="java.util.ArrayList"%>
<%@page import="java.io.IOException"%>
<%@page import="db.Wifi"%>
<%@page import="db.WifiMethod"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>와이파이 정보 구하기</title>
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
	<script type="text/javascript">
		function userLocation() {
			navigator.geolocation.getCurrentPosition(function(pos) {
				var latitude = pos.coords.latitude;
				var longitude = pos.coords.longitude;
				alert("현재 위치는 : " + latitude + ", " + longitude);
	
				document.getElementById("lat").value = latitude;
				document.getElementById("lnt").value = longitude;
			});
		}
	</script>
</head>
<body>		
		<%
			WifiMethod wifiMethod = new WifiMethod();
			String latStr = request.getParameter("lat");
			String lntStr = request.getParameter("lnt");
			double lat1 = 0; 
			double lnt1 = 0; 
			List<Wifi> wifiList = new ArrayList<>();
			
			if ((latStr != null && latStr.length() != 0) && (lntStr != null && lntStr.length() != 0)) {
				lat1 = Double.parseDouble(latStr);
				lnt1 = Double.parseDouble(lntStr);
				wifiList = wifiMethod.searchWifi(lat1, lnt1);
				wifiMethod.insertHistory(lat1, lnt1);
				wifiMethod.readjustHistory();
			}						
		%>
		<h1>와이파이 정보 구하기</h1>
		<div>
			<a href="index.jsp">홈</a> | <a href="history.jsp">위치 히스토리 목록</a> | <a href="load-wifi.jsp">Open API 와이파이 정보 가져오기</a>
		</div>
		<div>
			<form action="index.jsp" method="get">
				LAT:
				<input id="lat" type="number" step="0.0000000000001" name="lat" value="">
				, LNT:
				<input id="lnt" type="number" step="0.0000000000001" name="lnt" value="">
				<input type="button" onclick="userLocation()" value="내 위치 가져오기">
				<input type="submit" value="근처 와이파이 정보 가져오기">
			</form>
		</div>
		<table>
			<thead>
				<tr>
					<th>거리(Km)</th>
					<th>관리번호</th>
					<th>자치구</th>
					<th>와이파이명</th>
					<th>도로명주소</th>
					<th>상세주소</th>
					<th>설치위치(층)</th>
					<th>설치유형</th>
					<th>설치기관</th>
					<th>서비스구분</th>
					<th>망종류</th>
					<th>설치년도</th>
					<th>실내외구분</th>
					<th>WIFI접속환경</th>
					<th>위도</th>
					<th>경도</th>
					<th>작업일자</th>
				</tr>
			</thead>	
			<tbody>	
				<%	
				if (wifiList.size() != 0) {
					for (Wifi wifi: wifiList) {
				%>		
					<tr>
						<td><%=String.format("%.4f", wifi.getDistance())%></td>
						<td><%=wifi.getXSwifiMgrNo()%></td>
						<td><%=wifi.getXSwifiWrdofc()%></td>
						<td><%=wifi.getXSwifiMainNm()%></td>
						<td><%=wifi.getXSwifiAdres1()%></td>
						<td><%=wifi.getXSwifiAdres2()%></td>
						<td><%=wifi.getXSwifiInstlFloor()%></td>
						<td><%=wifi.getXSwifiInstlTy()%></td>
						<td><%=wifi.getXSwifiInstlMby()%></td>
						<td><%=wifi.getXSwifiSvcSe()%></td>
						<td><%=wifi.getXSwifiCmcwr()%></td>
						<td><%=wifi.getXSwifiCnstcYear()%></td>
						<td><%=wifi.getXSwifiInoutDoor()%></td>
						<td><%=wifi.getXSwifiRemars3()%></td>
						<td><%=wifi.getLat()%></td>
						<td><%=wifi.getLnt()%></td>
						<td><%=wifi.getWorkDttm()%></td>
					</tr>			
				<%					
				}
			} else {
			%>
				<tr class="noInfo">
					<td colspan="17">위치 정보를 입력한 후에 조회해주세요.</td>
				</tr>
			<%
			}
			%>
			</tbody>	
		</table>
</body>
</html>