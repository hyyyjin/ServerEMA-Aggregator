package com.mir.update.database;

import java.sql.SQLException;

import com.mir.ems.globalVar.global;

public class Update_test {

	public static DatabaseConnection databaseConnection = new DatabaseConnection();

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		databaseConnection.start();
		
		String sql = "insert into device_update_pv(`seq_num`, `deviceEMAID`, `deviceType`, `state`, `power`, `priority`, `time`,`C_EMA_ID`)"
				+ "values("+"\""+123 + "\"" + "," + "\""
				+ 123 + "\"" + ","+ 123
				+ 20.0 + "," 
				+ 23 + ","
				+"\"" +123.22+"\""+","+"\""+123+"\""+
				");";
		System.out.println(sql);
		try {
			global.databaseConnection.stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		////
		System.out.println(sql);
		System.out.println("aa");
		//

		databaseConnection.stmt.executeUpdate(sql);

	}

}