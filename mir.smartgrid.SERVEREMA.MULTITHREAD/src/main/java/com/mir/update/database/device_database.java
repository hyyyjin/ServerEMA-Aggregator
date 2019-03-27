//package com.mir.update.database;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Timer;
//import java.util.TreeMap;
//import java.util.concurrent.ConcurrentHashMap;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import com.mir.ems.database.item.DeviceClass_temp;
//import com.mir.ems.database.item.EMAClass_temp;
//import com.mir.ems.database.item.EMAP_CoAP_EMA_DR;
//import com.mir.ems.database.item.EMAP_CoAP_Schedule;
//import com.mir.ems.database.item.OpenADRClass_temp;
//import com.mir.ems.database.item.ScheduleClass_temp;
//import com.mir.ems.mqtt.EventInitiater;
//
//import java.util.Iterator;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class device_database {
//
//	device_total_database device_total_database;
//
//	public static ConcurrentHashMap<String, device_total_database> db_device = new ConcurrentHashMap<String, device_total_database>();
//
//	private String emaID, deviceEMAID, deviceType, state;
//	private int dimming, mode, priority;
//	private double power, capacity, volt, hz, chargedEnergy, soc;
//	Date timeStamp;
//
//	private JSONObject jsonObj;
//
//	public String UpdateReport(String requestText) {
//		JSONObject drmsg = new JSONObject();
//		String srcEMA = null;
//
//		JSONObject jsonobj2 = new JSONObject(jsonObj.getString("EMAupdatedMgnInfo"));
//		JSONObject sub1JsonObj;
//
//		try {
//
//			if (jsonobj2.getInt("emCNT") > 0) {
//				if (!jsonobj2.isNull("topology")) {
//					JSONArray topologyinfo = new JSONArray(jsonobj2.getString("topology"));
//
//					for (int i = 0; i < topologyinfo.length(); i++) {
//						sub1JsonObj = new JSONObject(topologyinfo.get(i).toString());
//
//						if (sub1JsonObj.getString("devieType").equals("LED")) {
//							device_total_database = new device_total_database(jsonObj.getString("SrcEMA"),
//									sub1JsonObj.getString("deviceEMAID"), sub1JsonObj.getString("deviceType"),
//									sub1JsonObj.getString("state"), sub1JsonObj.getInt("dimming"), 0,
//									sub1JsonObj.getInt("priority"), sub1JsonObj.getDouble("power"), 0.0, 0.0, 0.0, 0.0,
//									0.0, timeStamp);
//						}
//						device_total_database.put(sub1JsonObj.getString("deviceEMAID"), device_total_database);
//
//					}
//
//				}
//			}
//
//			jsonObj = new JSONObject(requestText);
//			srcEMA = jsonObj.getString("SrcEMA");
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//
//	}
//
//
//	public device_database() {
//		Timer timer = new Timer();
//		timer.scheduleAtFixedRate(chartUpdaterTask, 0, 2000);
//
//		device_total_database db = new device_total_database();
//
//		String DeviceEMAID = db.getDeviceEMAID();
//		String DeviceType = db.getDeviceType();
//		String state = db.getState();
//		int dimming = db.getDimming();
//
//		System.out.println(db.toString());
//
//	}
//	
//
//	public static int cnt = 0;
//	public static int idVal = 0;
//	
//	public static TimerTask chartUpdaterTask = new TimerTask() {
//
//		@Override
//		public void run() {
//
//			if (cnt <= 20) {
//				cnt++;
//				putMap(idVal++);
//			}
//
//			if (cnt == 20) {
//				System.out.println(aa.size());
//				DBInsert();
//
//				// Iterator는 for문 대신 사용
//				// Iterator<String> it = aa.keySet().iterator();
//				//
//				// while (it.hasNext()) {
//				// String key = it.next();
//				//
//				// String bb = "insert into testds22 values(\"" + key + "\"" +
//				// "," + "\"" + aa.get(key).toString()
//				// + "\"" + ");";
//				//
//				// System.out.println("====");
//				// System.out.println(bb);
//				// }
//				try {
//					aa.clear();
//					Thread.sleep(500);
//					cnt = 0;
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//	
//
//	
//	
//	if(sub1JsonObj.getString("devieType").equals("LED")) {
//		device_total_database = new device_total_database(jsonObj.getString("SrcEMA"),
//				sub1JsonObj.getString("deviceEMAID"), sub1JsonObj.getString("deviceType"),
//				sub1JsonObj.getString("state"), sub1JsonObj.getInt("dimming"), 0,
//				sub1JsonObj.getInt("priority"), sub1JsonObj.getDouble("power"), 0.0, 0.0, 0.0, 0.0,
//				0.0, timeStamp);
//	}
//	
//	
//}
