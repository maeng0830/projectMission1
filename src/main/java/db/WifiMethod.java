package db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import db.Data.Row;

public class WifiMethod {
	public OpenAPI openAPI;
	public List<Row> rows;
	
	// 완료
	// Open API로부터 공공 와이파이 정보 저장
	public void insertTotalData() throws IOException {
		// 1. openAPI 호출, 데이터(rows) 준비
		openAPI = new OpenAPI();
		openAPI.checkTotalList();
		openAPI.callAPI();	
		rows = openAPI.data.getTbpublicwifiinfo().getRow();
		
		String url = "jdbc:mariadb://localhost:3306/wifi";
        String dbUserId = "maeng0830";
        String dbPassWord = "!@aud221166";
		
		// 2. DB 연결
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			// DB table에 모든 데이터 insert, 중복값 배제
			connection = DriverManager.getConnection(url, dbUserId, dbPassWord);
			String sql = "INSERT IGNORE INTO PUBLIC_WIFI_INFO \r\n"
					+ "	VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			
			preparedStatement = connection.prepareStatement(sql);
			
			for (int i = 0; i < rows.size(); i++) {
				preparedStatement.setString(1, rows.get(i).getXSwifiMgrNo());
				preparedStatement.setString(2, rows.get(i).getXSwifiWrdofc());
				preparedStatement.setString(3, rows.get(i).getXSwifiMainNm());
				preparedStatement.setString(4, rows.get(i).getXSwifiAdres1());
				preparedStatement.setString(5, rows.get(i).getXSwifiAdres2());
				preparedStatement.setString(6, rows.get(i).getXSwifiInstlFloor());
				preparedStatement.setString(7, rows.get(i).getXSwifiInstlTy());
				preparedStatement.setString(8, rows.get(i).getXSwifiInstlMby());
				preparedStatement.setString(9, rows.get(i).getXSwifiSvcSe());
				preparedStatement.setString(10, rows.get(i).getXSwifiCmcwr());
				preparedStatement.setString(11, rows.get(i).getXSwifiCnstcYear());
				preparedStatement.setString(12, rows.get(i).getXSwifiInoutDoor());
				preparedStatement.setString(13, rows.get(i).getXSwifiRemars3());
				preparedStatement.setDouble(14, rows.get(i).getLnt()); // 바꿨음, 기존 API에서 lat과 lnt가 바뀐 상태로 제공됨.
				preparedStatement.setDouble(15, rows.get(i).getLat()); // 바꿨음, 기존 API에서 lat과 lnt가 바뀐 상태로 제공됨.
				preparedStatement.setString(16, rows.get(i).getWorkDttm());
				
				int affectedRows = preparedStatement.executeUpdate();
				
				if (affectedRows > 0) {
	                System.out.println("저장 성공");
	            } else {
	                System.out.println("저장 실패 또는 이미 저장된 데이터");
	            }
			}	
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally {
            // 3. 객체 연결 해제(close)
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }				
	}
	
	// 완료
	// 근처 와이파이 정보 조회
	public List<Wifi> searchWifi(double lat1, double lnt1) {
		List<Wifi> wifiList = new ArrayList<>();
		
		String url = "jdbc:mariadb://localhost:3306/wifi";
        String dbUserId = "maeng0830";
        String dbPassWord = "!@aud221166";
		
		// 1. DB 연결
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		
		try {
			connection = DriverManager.getConnection(url, dbUserId, dbPassWord);
			
			String sql = "SELECT *, (6371*acos(cos(radians(?))*cos(radians(lat))*cos(radians(lnt)-radians(?))+sin(radians(?))*sin(radians(lat)))) AS DISTANCE\r\n"
					+ "FROM public_wifi_info\r\n"
					+ "ORDER BY distance ASC\r\n"
					+ "LIMIT 20;";
					
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, lat1);
			preparedStatement.setDouble(2, lnt1);
			preparedStatement.setDouble(3, lat1);
			
			rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				double distance = rs.getDouble("Distance");
				String XSwifiMgrNo = rs.getString("XSwifiMgrNo");
				String XSwifiWrdofc = rs.getString("XSwifiWrdofc");
				String XSwifiMainNm = rs.getString("XSwifiMainNm");
				String XSwifiAdres1 = rs.getString("XSwifiAdres1");
				String XSwifiAdres2 = rs.getString("XSwifiAdres2");
				String XSwifiInstlFloor = rs.getString("XSwifiInstlFloor");
				String XSwifiInstlTy = rs.getString("XSwifiInstlTy");
				String XSwifiInstlMby = rs.getString("XSwifiInstlMby");
				String XSwifiSvcSe = rs.getString("XSwifiSvcSe");
				String XSwifiCmcwr = rs.getString("XSwifiCmcwr");
				String XSwifiCnstcYear = rs.getString("XSwifiCnstcYear");
				String XSwifiInoutDoor = rs.getString("XSwifiInoutDoor");
				String XSwifiRemars3 = rs.getString("XSwifiRemars3");
				double lat = rs.getDouble("Lat");
				double lnt = rs.getDouble("Lnt");
				String WorkDttm = rs.getString("WorkDttm");
				
				
				Wifi wifi = new Wifi();
				wifi.setDistance(distance);
				wifi.setXSwifiMgrNo(XSwifiMgrNo);
				wifi.setXSwifiWrdofc(XSwifiWrdofc);
				wifi.setXSwifiMainNm(XSwifiMainNm);
				wifi.setXSwifiAdres1(XSwifiAdres1);
				wifi.setXSwifiAdres2(XSwifiAdres2);
				wifi.setXSwifiInstlFloor(XSwifiInstlFloor);
				wifi.setXSwifiInstlTy(XSwifiInstlTy);
				wifi.setXSwifiInstlMby(XSwifiInstlMby);
				wifi.setXSwifiSvcSe(XSwifiSvcSe);
				wifi.setXSwifiCmcwr(XSwifiCmcwr);
				wifi.setXSwifiCnstcYear(XSwifiCnstcYear);
				wifi.setXSwifiInoutDoor(XSwifiInoutDoor);
				wifi.setXSwifiRemars3(XSwifiRemars3);
				wifi.setLat(lat);
				wifi.setLnt(lnt);
				wifi.setWorkDttm(WorkDttm);
				
				wifiList.add(wifi);
			}	
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally {
            // 3. 객체 연결 해제(close)
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
		
		return wifiList;
	}
	
	// 완료
	// wifiHistory 테이블 데이터 저장
	public void insertHistory(double lat1, double lnt1) {
		// searchWifi가 실행된 후 즉시 실행되는 메소드
		String url = "jdbc:mariadb://localhost:3306/wifi";
        String dbUserId = "maeng0830";
        String dbPassWord = "!@aud221166";
		
		// 2. DB 연결
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			// DB table에 모든 데이터 insert, 중복값 배제
			connection = DriverManager.getConnection(url, dbUserId, dbPassWord);
			String sql = " INSERT INTO public_wifi_info_history (cur_lat, cur_lnt, viewDttm)\r\n"
					+ " VALUES (?, ?, ?);";
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setDouble(1, lat1);
            preparedStatement.setDouble(2, lnt1);
            preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(LocalDateTime.now()));
			
			int affectedRows = preparedStatement.executeUpdate();
			
			if (affectedRows > 0) {
                System.out.println("저장 성공");
            } else {
                System.out.println("저장 실패 또는 이미 저장된 데이터");
            }
				
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally {
            // 3. 객체 연결 해제(close)
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }				
	}
	
	// 완료
	// wifiHistory 테이블 조회
	public List<WifiHistory> viewHistory() {
		List<WifiHistory> wifiHistoryList = new ArrayList<>();
		
		String url = "jdbc:mariadb://localhost:3306/wifi";
        String dbUserId = "maeng0830";
        String dbPassWord = "!@aud221166";
		
		// 1. DB 연결
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		
		try {
			connection = DriverManager.getConnection(url, dbUserId, dbPassWord);
			
			String sql = "SELECT *\r\n"
					+ "FROM public_wifi_info_history\r\n"
					+ "ORDER BY ID;";
					
			preparedStatement = connection.prepareStatement(sql);
			
			rs = preparedStatement.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("ID");
				double curLat = rs.getDouble("cur_lat");
				double curLnt = rs.getDouble("cur_lnt");
				Timestamp viewDttm = rs.getTimestamp("viewDttm");
				
				
				WifiHistory wifiHistory = new WifiHistory();
				wifiHistory.setId(id);
				wifiHistory.setCurLat(curLat);
				wifiHistory.setCurLnt(curLnt);
				wifiHistory.setViewDttm(viewDttm + "");
				
				wifiHistoryList.add(wifiHistory);
			}	
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally {
            // 3. 객체 연결 해제(close)
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
		
		return wifiHistoryList;		
	}
	
	// 완료
	// wifiHistory 테이블 특정 행 삭제
	public void removeHistory(int id) {
		String url = "jdbc:mariadb://localhost:3306/wifi";
        String dbUserId = "maeng0830";
        String dbPassWord = "!@aud221166";
        
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;

        try {
            //2. 커넥션 객체 생성
            connection = DriverManager.getConnection(url, dbUserId, dbPassWord);

            //4. 쿼리 실행
            String sql = "DELETE \r\n"
            		+ "FROM public_wifi_info_history\r\n"
            		+ "WHERE ID = ?;";

            //3. 스테이트먼트 객체 생성
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            
            int affectedRows = preparedStatement.executeUpdate();

            //5. 결과 수행
            if (affectedRows > 0) {
                System.out.println("삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            //6. 객체 연결 해제(close)
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
	
	// 완료
	// wifiHistory 테이블 ID값 1 ~ n으로 재조정
	public void readjustHistory() {
		String url = "jdbc:mariadb://localhost:3306/wifi";
        String dbUserId = "maeng0830";
        String dbPassWord = "!@aud221166";
		
		// 1. DB 연결
		try {
			Class.forName("org.mariadb.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		
		try {
			connection = DriverManager.getConnection(url, dbUserId, dbPassWord);
			
			String sql = "ALTER TABLE public_wifi_info_history AUTO_INCREMENT=1;";					
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeQuery();
			
			sql = "SET @COUNT = 0;";					
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeQuery();
			
			sql = "UPDATE public_wifi_info_history SET ID = @COUNT:=@COUNT+1;";					
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeQuery();
		
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}finally {
            // 3. 객체 연결 해제(close)
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (preparedStatement != null && !preparedStatement.isClosed()) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }	
	}
	
}
