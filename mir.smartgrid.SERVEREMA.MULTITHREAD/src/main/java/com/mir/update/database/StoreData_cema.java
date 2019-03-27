package com.mir.update.database;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import com.mir.ems.globalVar.global;

public class StoreData_cema {

	public StoreData_cema() {

		Timer timer = new Timer();

		timer.scheduleAtFixedRate(StoreReportData, 0, 2000);

	}

	public TimerTask StoreReportData = new TimerTask() {

		@Override
		public void run() {

			if (global.REPORTCNT_CEMA > 5) {
				
				// DB저장
				for (Object o : global.saveCEMA.keySet()) {
					// System.out.println("Test");
//					System.err.println(global.saveCEMA.get(o).getAvgValue());
					// System.out.println(global.saveDevice.get(o).getDeviceType().equals("LED"));
					
					String sql = "insert into cema_update_report"
							+ "(`SrcEMA`, `DestEMA`, `reportName`, `reportType`, `requestID`, `time`,"
							+ "`itemunit` , `marketContext` , `createDataTime` , `siSclecode` , `readingType`, "
							+ "`oadrReport` , `duration` , `rid` , `hertz` , `ac` , `oadrMinPeriod` , `oadrMaxPeriod` , `oadrReportDescription` , `energyReal` , "
							+ "`oadrOnChange` ,`reportSpecifierID`,`emaCNT`,`state`,`dimming`,`power`,`generate`,`storage`,`maxValue`,`minValue`,`avgValue`,"
							+ "`maxTime`,`minTime`)"
							+ " values(\"" + global.saveCEMA.get(o).getSrcEMA() 
							+ "\"" + "," + "\"" + "DestEMA" + "\"" + "," + "\"" + global.saveCEMA.get(o).reportName + "\"" + ","
							+"\""+ global.saveCEMA.get(o).reportType+"\"" + ","+  + global.saveCEMA.get(o).getRequestID() +","+"\""
							+global.saveCEMA.get(o).time+"\"" +","+"\""+global.saveCEMA.get(o).getItemunit()+"\""+","+ "\""+global.saveCEMA.get(o).marketContext
							+"\""+","+ "\""+global.saveCEMA.get(o).getCreateDataTime()+"\""+","+global.saveCEMA.get(o).siSclecode+","+global.saveCEMA.get(o).readingType
							+","+ "\""+global.saveCEMA.get(o).getOadrReport()+"\""+","+global.saveCEMA.get(o).duration+","+global.saveCEMA.get(o).getRid()
							+","+global.saveCEMA.get(o).getHertz()+","+global.saveCEMA.get(o).getAc()+","+global.saveCEMA.get(o).getOadrMinPeriod()+","
							+global.saveCEMA.get(o).getOadrMaxPeriod()+","+ "\""+global.saveCEMA.get(o).getOadrReportDescription()+"\""+","+"\""+global.saveCEMA.get(o).getEnergyReal()+"\""+","
							+global.saveCEMA.get(o).getOadrOnChange()+","+global.saveCEMA.get(o).getReportSpecfierID()+","+global.saveCEMA.get(o).getEmaCNT()
							+","+ "\""+global.saveCEMA.get(o).getState()+"\""+","+global.saveCEMA.get(o).getDimming()+","+global.saveCEMA.get(o).getPower()
							+","+global.saveCEMA.get(o).getGenerate()+","+global.saveCEMA.get(o).getStorage()+","+global.saveCEMA.get(o).maxValue
							+","+global.saveCEMA.get(o).getMinValue()+","+global.saveCEMA.get(o).getAvgValue()+","+"\""+global.saveCEMA.get(o).getMaxTime()+"\""
							+","+"\""+global.saveCEMA.get(o).getMinTime()+"\""+");";
						
//						System.err.println("test_succeed"+sql);
					
//					try {
//						global.databaseConnection.stmt.executeUpdate(sql);
//					} catch (SQLException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
				// 저장 후 CLEAR
				global.saveCEMA.clear();
				global.REPORTCNT_CEMA = 0;
				
			}

		}
	};

}
