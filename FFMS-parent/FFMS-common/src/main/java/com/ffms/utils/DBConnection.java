package com.ffms.utils;
import java.sql.*;

/**
 * @author zc
 * 数据库连接
 *  *DBConnection.java
 * 2019年10月17日 下午2:52:48
 */
public class DBConnection {
	
	private static Connection conn = null;
	public  Connection getConnection() {
		if(conn==null){
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");// 1
				conn = DriverManager.getConnection(//2
						"jdbc:mysql://localhost:3306/ffms?serverTimezone=UTC&characterEncoding=utf-8",
						"root", 
						"root");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	public static void closeall() {//6
		try {
			if (null != conn)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
