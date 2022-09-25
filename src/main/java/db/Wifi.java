package db;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Wifi {
     private String workDttm; // 작업일자
     private double lnt; // 경도
     private double lat; // 위도
     private String xSwifiRemars3; // wifi접속환경
     private String xSwifiInoutDoor; // 실내외구분
     private String xSwifiCnstcYear; // 설치년도
     private String xSwifiCmcwr; // 망종류
     private String xSwifiSvcSe; // 서비스구분
     private String xSwifiInstlMby; // 설치기관
     private String xSwifiInstlTy; // 설치유형
     private String xSwifiInstlFloor; // 설치위치(층)
     private String xSwifiAdres2; // 상세주소
     private String xSwifiAdres1; // 도로명주소
     private String xSwifiMainNm; // 와이파이명
     private String xSwifiWrdofc; // 자치구
     private String xSwifiMgrNo; // 관리번호
     private double distance; // 거리

}
