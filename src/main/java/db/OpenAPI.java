
package db;

import java.awt.image.SampleModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Iterator;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.*;


public class OpenAPI {
	int totalList = 0;
	Gson gson = new Gson();
	Data data;
	
	public void checkTotalList() throws IOException {
		StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /* URL */
		urlBuilder.append("/" + URLEncoder.encode("7671567a6d6b6d6b3731766f6e707a", "UTF-8")); /* 인증키(sample사용시에는 호출시 제한된다) */
		urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8")); /* 요청파일타입(xml, xmlf, xls, json) */
		urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo", "UTF-8")); /* 서비스명(대소문자 구분 필수) */
		urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8")); /* 요청시작위치(sample 인증키 사용시 5 이내 숫자) */
		urlBuilder.append("/" + URLEncoder.encode("1", "UTF-8")); /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안됨).*/ 
		// 상위 5개는 필수적으로 순서바꾸지 않고 호출해야함.
		
		// 서비스별 추가 요청 인자이며 자세한 내용은 각 서비스별 '요쳥인자'부분에 자세히 나와있습니다.
//		urlBuilder.append("/" + URLEncoder.encode("20220301", "UTF-8")); 
		
		URL url = new URL(urlBuilder.toString());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("ContentType", "application/json");
		System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인 */
		BufferedReader rd;
		
		if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		} else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
		
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		
		rd.close();
		conn.disconnect();
		
		// Data data = gson.fromJson(sb.toString(), Data.class);
		
		totalList = gson.fromJson(sb.toString(), Data.class).getTbpublicwifiinfo().getListTotalCount();
	}
	
	
	public void callAPI() throws IOException {		
		for (int i = 1; i < totalList; i += 1000) {
			StringBuilder urlBuilder = new StringBuilder("http://openapi.seoul.go.kr:8088"); /* URL */
			urlBuilder.append("/" + URLEncoder.encode("7671567a6d6b6d6b3731766f6e707a", "UTF-8")); /* 인증키(sample사용시에는 호출시 제한된다) */
			urlBuilder.append("/" + URLEncoder.encode("json", "UTF-8")); /* 요청파일타입(xml, xmlf, xls, json) */
			urlBuilder.append("/" + URLEncoder.encode("TbPublicWifiInfo", "UTF-8")); /* 서비스명(대소문자 구분 필수) */
			urlBuilder.append("/" + URLEncoder.encode(String.valueOf(i), "UTF-8")); /* 요청시작위치(sample 인증키 사용시 5 이내 숫자) */
			urlBuilder.append("/" + URLEncoder.encode(String.valueOf(i + 999), "UTF-8")); /*요청종료위치(sample인증키 사용시 5이상 숫자 선택 안됨).*/ 
			// 상위 5개는 필수적으로 순서바꾸지 않고 호출해야함.
						
			URL url = new URL(urlBuilder.toString());
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("ContentType", "application/json");
			System.out.println("Response code: " + conn.getResponseCode()); /* 연결 자체에 대한 확인 */
			BufferedReader rd;
			
			if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
				rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			
			rd.close();
			conn.disconnect();
			
			if (i == 1) {
				data = gson.fromJson(sb.toString(), Data.class);
				continue;
			}
			
			for (int j = 0; j < gson.fromJson(sb.toString(), Data.class).getTbpublicwifiinfo().getRow().size(); j++) {
				data.getTbpublicwifiinfo().getRow().add(gson.fromJson(sb.toString(), Data.class).getTbpublicwifiinfo().getRow().get(j));
			}					
		}
	}
}
