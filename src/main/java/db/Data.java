package db;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.*;


@Getter
@Setter
public class Data {
    @com.google.gson.annotations.SerializedName("TbPublicWifiInfo")
    private Tbpublicwifiinfo tbpublicwifiinfo;

    @Getter
    @Setter
    public static class Tbpublicwifiinfo {
        @com.google.gson.annotations.SerializedName("row")
        private List<Row> row;
        @com.google.gson.annotations.SerializedName("RESULT")
        private Result result;
        @com.google.gson.annotations.SerializedName("list_total_count")
        private int listTotalCount;
    }

    @Getter
    @Setter
    public static class Row {
        @com.google.gson.annotations.SerializedName("WORK_DTTM")
        private String workDttm; // 작업일자
        @com.google.gson.annotations.SerializedName("LNT")
        private double lnt; // 경도
        @com.google.gson.annotations.SerializedName("LAT")
        private double lat; // 위도
        @com.google.gson.annotations.SerializedName("X_SWIFI_REMARS3")
        private String xSwifiRemars3; // wifi접속환경
        @com.google.gson.annotations.SerializedName("X_SWIFI_INOUT_DOOR")
        private String xSwifiInoutDoor; // 실내외구분
        @com.google.gson.annotations.SerializedName("X_SWIFI_CNSTC_YEAR")
        private String xSwifiCnstcYear; // 설치년도
        @com.google.gson.annotations.SerializedName("X_SWIFI_CMCWR")
        private String xSwifiCmcwr; // 망종류
        @com.google.gson.annotations.SerializedName("X_SWIFI_SVC_SE")
        private String xSwifiSvcSe; // 서비스구분
        @com.google.gson.annotations.SerializedName("X_SWIFI_INSTL_MBY")
        private String xSwifiInstlMby; // 설치기관
        @com.google.gson.annotations.SerializedName("X_SWIFI_INSTL_TY")
        private String xSwifiInstlTy; // 설치유형
        @com.google.gson.annotations.SerializedName("X_SWIFI_INSTL_FLOOR")
        private String xSwifiInstlFloor; // 설치위치(층)
        @com.google.gson.annotations.SerializedName("X_SWIFI_ADRES2")
        private String xSwifiAdres2; // 상세주소
        @com.google.gson.annotations.SerializedName("X_SWIFI_ADRES1")
        private String xSwifiAdres1; // 도로명주소
        @com.google.gson.annotations.SerializedName("X_SWIFI_MAIN_NM")
        private String xSwifiMainNm; // 와이파이명
        @com.google.gson.annotations.SerializedName("X_SWIFI_WRDOFC")
        private String xSwifiWrdofc; // 자치구
        @com.google.gson.annotations.SerializedName("X_SWIFI_MGR_NO")
        private String xSwifiMgrNo; // 관리번호
        @com.google.gson.annotations.SerializedName("DISTANCE")
        private double distance; // 거리
    }

    @Getter
    @Setter
    public static class Result {
        @com.google.gson.annotations.SerializedName("MESSAGE")
        private String message;
        @com.google.gson.annotations.SerializedName("CODE")
        private String code;
    }
}
