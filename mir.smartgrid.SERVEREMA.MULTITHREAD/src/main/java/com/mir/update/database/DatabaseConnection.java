package com.mir.update.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {


	public String url = "jdbc:mysql://166.104.143.225:3306/mir_smart?user=mir&password=1129&useSSL=false&characterEncoding=UTF-8&serverTimezone=UTC";
	public Connection conn = null;

	public Statement stmt = null;

	public ResultSet rs = null;
	
	
	
	public DatabaseConnection() {

	}
	
	public void start()throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url);
		System.out.println("Connection OK");
		stmt = conn.createStatement();
	}
	
//	public static void DBInsert() {
//
//
//		// 드라이버 로드
//		try {
//			System.out.println("test");
//			Class.forName("com.mysql.jdbc.Driver");
//			conn = DriverManager.getConnection(url);
//			System.out.println("Connection OK");
//
//			// 쿼리 준비 단계
//			stmt = conn.createStatement();
//
//			Iterator<String> it = aa.keySet().iterator();
//
//			while (it.hasNext()) {
//
//				String key = it.next();
//
//				String sql = "insert into testds22 values(\"" + key + "\"" + "," + "\"" + aa.get(key).toString() + "\""
//						+ ");";
//
//				stmt.executeUpdate(sql);
//			}
//
//			String selectSql = "select * from testds22;";
//
//			rs = stmt.executeQuery(selectSql);
//
//			while (rs.next()) {
//				System.out.println("=======================================");
//				System.out.println(rs.getString(1) + "\t" + rs.getString(2));
//			}
//
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		} finally {
//			if (conn != null)
//				try {
//					conn.close();
//				} catch (SQLException conne) {
//				}
//		}
//
//	}
	
	
}
