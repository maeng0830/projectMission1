package db;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WifiHistory {
	private int id; // id
	private double curLnt; // 경도
    private double curLat; // 위도
    private String viewDttm; // 조회일자
}
