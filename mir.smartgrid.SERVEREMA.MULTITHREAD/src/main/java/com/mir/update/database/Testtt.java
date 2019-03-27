package com.mir.update.database;

import java.sql.SQLException;

public class Testtt {

	public static DatabaseConnection databaseConnection = new DatabaseConnection();

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		// TODO Auto-generated method stub

		databaseConnection.start();

		String sql = "insert into device_update_led(C_EMA_ID, deviceEMAID, deviceType, power, state, dimming, priorty)"
				+ "values(\"" +"DONGSUNGGOON"+"\""+","+"\"" +"LED1" + "\"" + "," + "\"" + "LED" + "\"" + "," + 100 + "," + "\"" + "ON" + "\"" + ","
				+ 9 + "," + 1 + ");";

		////
		System.out.println(sql);
		System.out.println("aa");
		//

		databaseConnection.stmt.executeUpdate(sql);

	}

}
