package com.mir.update.database;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import com.mir.ems.globalVar.global;

public class StoreData_device {

	public StoreData_device() {

		Timer timer = new Timer();

		timer.scheduleAtFixedRate(StoreReportData, 0, 2000);

	}

	public TimerTask StoreReportData = new TimerTask() {

		@Override
		public void run() {

			if (global.REPORTCNT > 50) {

				// DB저장
				for (Object o : global.saveDevice.keySet()) {
					// System.out.println("Test");
//					System.err.println(global.saveDevice.get(o).getDeviceType());
					// System.out.println(global.saveDevice.get(o).getDeviceType().equals("LED"));

					// LED 저장
//					if (global.saveDevice.get(o).getDeviceType().contains("LED")) {

						String sql = "insert into device_update_led(C_EMA_ID, deviceEMAID, deviceType, power, state, dimming, priority, time, seq_num)"
								+ "values(\"" + global.saveDevice.get(o).getEmaID() + "\"" + "," + "\""
								+ global.saveDevice.get(o).getDeviceEMAID() + "\"" + "," + "\""
								+ global.saveDevice.get(o).getDeviceType() + "\"" + ","
								+ global.saveDevice.get(o).getPower() + "," + "\"" + global.saveDevice.get(o).getState()
								+ "\"" + "," + global.saveDevice.get(o).getDimming() + ","
								+ global.saveDevice.get(o).getPriority() + ","
								+"\"" +global.saveDevice.get(o).getDate()+"\""+","
								+Integer.parseInt(o.toString())
								+");";

//						try {
//							global.databaseConnection.stmt.executeUpdate(sql);
//						} catch (SQLException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
					
					// PV 저장
					if (global.saveDevice.get(o).getDeviceType().contains("PV")) {
						// System.out.println("Test");
						System.err.println("PV!!!!!!!"+global.saveDevice.get(o).getDeviceType());
						sql = "insert into device_update_pv(`C_EMA_ID`, `deviceEMAID`, `deviceType`, `state`, `power`, `priority`, `time`)"
								+ "values(\"" + global.saveDevice.get(o).getEmaID() + "\"" + "," + "\""
								+ global.saveDevice.get(o).getDeviceEMAID() + "\"" + "," + "\""
								+ global.saveDevice.get(o).getDeviceType() + "\"" + ","+ global.saveDevice.get(o).getState()
								+ global.saveDevice.get(o).getPower() + "," 
								+ global.saveDevice.get(o).getPriority() + ","
								+"\"" +global.saveDevice.get(o).getDate()+"\""+
								");";

						try {
							global.databaseConnection.stmt.executeUpdate(sql);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}

					// RECLOSER 저장
					if (global.saveDevice.get(o).getDeviceType().contains("RECLOSER")) {

					}

					// ESS 저장
					if (global.saveDevice.get(o).getDeviceType().contains("ESS")) {

					}

				}

				// 저장 후 CLEAR
				global.saveDevice.clear();
				global.REPORTCNT = 0;

			} else {

//				System.out.println(global.saveDevice.keySet().toString());

			}
		}
	};

}
