package com.mir.ems.mqtt.emap;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.californium.core.server.resources.CoapExchange;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.Publishing;
import com.mir.update.database.cema_database;
import com.mir.update.database.device_total_database;

public class Report extends Thread {

	enum Type {
		REGISTERREPORT, UPDATEREPORT
	}

	MqttClient client;
	String sortTopic;
	String processTopic;
	String payload;

	private String service;
	private String version = null;

	public Report(MqttClient client, String service, JSONObject payload, String version) {

		this.client = client;
		this.service = service;
		this.payload = payload.toString();
		this.version = version;
	}

	public Report(MqttClient client, String sortTopic, String processTopic, JSONObject payload) {
		this.client = client;
		this.sortTopic = sortTopic;
		this.processTopic = processTopic;
		this.payload = payload.toString();

	}

	CoapExchange exchange;
	String incomingType, requestText, setPayload;

	String setTopic;

	Emap_Cema_Profile emaProfile;
	Emap_Device_Profile deviceProfile;
	private JSONObject jsonObj;
	private JSONObject sub1JsonObj;
	private JSONObject sub2JsonObj;

	@Override
	public void run() {

		Type type;

		if (version.equals("EMAP1.0b")) {
			type = Type.valueOf(service.toUpperCase());

		}

		else if (version.equals("OpenADR2.0b")) {
			type = Type.valueOf(service.toUpperCase());

		}

		else {
			type = Type.valueOf(processTopic.toUpperCase());

		}

		switch (type) {
		case UPDATEREPORT:
			this.setPayload = acknowledgeUPDATEREPORT(payload);
			this.setTopic = "UpdatedReport";
			break;
		default:
			this.setPayload = "TYPE WRONG";
			break;
		}

		if (this.setPayload.equals("TYPE WRONG")) {

		} else if (this.setPayload.toUpperCase().equals("NORESPONSE")) {

		} else {
			String[] payloadSet = this.setPayload.split("&&");
			String srcID = payloadSet[2];
			String topic = "CEMA/" + srcID + "/" + "Report" + "/" + payloadSet[0];
			new Publishing().publishThread(client, topic, global.qos, payloadSet[1].getBytes());

		}

	}

	public String acknowledgeUPDATEREPORT(String requestText) {
		JSONObject drmsg = new JSONObject();
		String srcEMA = null;

		if (version.equals("EMAP1.0b")) {
			try {

				jsonObj = new JSONObject(requestText);

				emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(jsonObj.getString("SrcEMA")).setRequestID(jsonObj.getString("requestID"));

				JSONArray reportArr = new JSONArray(jsonObj.getString("report"));

				for (int i = 0; i < reportArr.length(); i++) {

					JSONArray decrArr = new JSONArray(
							new JSONObject(reportArr.get(i).toString()).getString("reportDescription"));

					if (jsonObj.getString("type").equals("Implicit") || 1 == decrArr.length()) {

						JSONObject decr = new JSONObject(decrArr.get(0).toString());

						String qos, registrationID, state, minTime, maxTime;
						double margin, minValue, maxValue, avgValue, power, generate, storage;
						int priority, dimming;
						boolean pullModel;

						qos = decr.getString("qos");
						registrationID = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getRegistrationID();
//						margin = decr.getDouble("margin");
						minValue = decr.getDouble("minValue");
						maxValue = decr.getDouble("maxValue");
						avgValue = decr.getDouble("avgValue");
						minTime = decr.getString("minTime");
						maxTime = decr.getString("maxTime");
						power = decr.getDouble("power");
						generate = decr.getDouble("generate");
						storage = decr.getDouble("storage");
						state = decr.getString("state");
						priority = decr.getInt("priority");
						dimming = decr.getInt("dimming");
						pullModel = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isPullModel();

						boolean realTimetable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA"))
								.isRealTimetableChanged();
						boolean timeTable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isTableChanged();

						// Threshold 기준 80%이상을 사용할 경우 이벤트 시그널

						margin = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getMargin();

						if (global.EXPERIMENTAUTODR) {
							if (power > 10 && power >= margin * global.EXPERIMENTPERCENT) {
								setEvent(jsonObj.getString("SrcEMA"), margin, power);
							}
						}

						Emap_Cema_Profile profile = new Emap_Cema_Profile("MQTT", jsonObj.getString("SrcEMA"),
								registrationID, qos, state, power, dimming, margin, generate, storage, maxValue,
								minValue, avgValue, maxTime, minTime, priority, pullModel, timeTable, realTimetable,
								"CONNECT");

						global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), profile);

						cema_database cd = new cema_database();
						cd.buildup(jsonObj.getString("SrcEMA"), qos, state, power, dimming, margin, generate, storage,
								maxValue, minValue, avgValue, maxTime, minTime, priority);

					}

					else if (jsonObj.getString("type").equals("Explicit") || 1 < decrArr.length()) {

//						System.out.println("익스플리시트");
						
						for (int j = 0; j < decrArr.length(); j++) {

							JSONObject decr = new JSONObject(decrArr.get(j).toString());

							String qos, registrationID, state, minTime, maxTime, rID;
							double margin, minValue, maxValue, avgValue, power, generate, storage;
							int priority, dimming;
							boolean pullModel;

							if (decr.getString("deviceType").contains("EMA")) {
								qos = decr.getString("qos");
								registrationID = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getRegistrationID();
//								margin = decr.getDouble("margin");
								minValue = decr.getDouble("minValue");
								maxValue = decr.getDouble("maxValue");
								avgValue = decr.getDouble("avgValue");
								minTime = decr.getString("minTime");
								maxTime = decr.getString("maxTime");
								power = decr.getDouble("power");
								generate = decr.getDouble("generate");
								storage = decr.getDouble("storage");
								state = decr.getString("state");
								priority = decr.getInt("priority");
								dimming = decr.getInt("dimming");
								pullModel = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isPullModel();

								boolean realTimetable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA"))
										.isRealTimetableChanged();
								boolean timeTable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).isTableChanged();


								margin = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getMargin();

								if (global.EXPERIMENTAUTODR) {
									if (power >= margin * global.EXPERIMENTPERCENT) {

										setEvent(jsonObj.getString("SrcEMA"), margin, power);

									}
								}
								

								Emap_Cema_Profile profile = new Emap_Cema_Profile("MQTT", jsonObj.getString("SrcEMA"),
										registrationID, qos, state, power, dimming, margin, generate, storage, maxValue,
										minValue, avgValue, maxTime, minTime, priority, pullModel, timeTable,
										realTimetable, "CONNECT");

								global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), profile);

								cema_database cd = new cema_database();
								cd.buildup(jsonObj.getString("SrcEMA"), qos, state, power, dimming, margin, generate,
										storage, maxValue, minValue, avgValue, maxTime, minTime, priority);

							}

							else {

								rID = decr.getString("rID");
								qos = decr.getString("qos");
								margin = decr.getDouble("margin");
								minValue = decr.getDouble("minValue");
								maxValue = decr.getDouble("maxValue");
								avgValue = decr.getDouble("avgValue");
								minTime = decr.getString("minTime");
								maxTime = decr.getString("maxTime");
								power = decr.getDouble("power");
								generate = decr.getDouble("generate");
								storage = decr.getDouble("storage");
								state = decr.getString("state");
								priority = decr.getInt("priority");
								dimming = decr.getInt("dimming");

								
//								System.out.println("STATE"+state);
								
								deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"), rID,
										decr.getString("deviceType"), qos, state, power, dimming, margin, generate,
										storage, maxValue, minValue, avgValue, maxTime, minTime, priority);

								// global.emaProtocolCoAP_Device.replace(rID,
								// deviceProfile);
//								new devUpdate(rID, deviceProfile).start();
								if (!global.emaProtocolCoAP_Device.containsKey(rID)) {
									// System.out.println("있음?");
									global.emaProtocolCoAP_Device.put(rID, deviceProfile);
								} else {
									// System.out.println("없음");
									global.emaProtocolCoAP_Device.replace(rID, deviceProfile);
								}

//								dcevice_total_database dtd = new device_total_database();
//								dtd.buildUp(jsonObj.getString("SrcEMA"), rID, decr.getString("deviceType"), state,
//										dimming, priority, power, new Date(System.currentTimeMillis()).toString());

							}

//							JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));

//							for (int k = 0; k < powerAtts.length(); k++) {
//								JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());
//							}

						}

					}

				}

				com.mir.ems.profile.emap.v2.UpdatedReport udt = new com.mir.ems.profile.emap.v2.UpdatedReport();
				udt.setDestEMA(emaProfile.getEmaID());
				udt.setRequestID(emaProfile.getRequestID());
				udt.setResponseCode(200);
				udt.setResponseDescription("OK");
				udt.setService("UpdatedReport");
				udt.setSrcEMA(global.SYSTEM_ID);
				udt.setTime(new Date(System.currentTimeMillis()).toString());
				udt.setType(global.reportType);

				// String topic = "/EMAP/CEMA/1.0b/Report";

				String topic = "/EMAP/" + emaProfile.getEmaID() + "/1.0b/Report";

				String payload = udt.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				return "NORESPONSE";

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}

		else if (version.equals("OpenADR2.0b")) {

			try {
				jsonObj = new JSONObject(requestText);

				emaProfile = new Emap_Cema_Profile();

				
				String venID = jsonObj.getString("venID");
				
				emaProfile.setEmaID(venID).setRequestID(jsonObj.getString("requestID"));
				String registrationID = global.emaProtocolCoAP.get(venID).getRegistrationID();
				boolean pullModel = global.emaProtocolCoAP.get(venID).isPullModel();
				boolean realTimetable = global.emaProtocolCoAP.get(venID).isRealTimetableChanged();
				boolean timeTable = global.emaProtocolCoAP.get(venID).isTableChanged();

				JSONArray reportArr = new JSONArray(jsonObj.getString("oadrReport"));

				for (int i = 0; i < reportArr.length(); i++) {

					JSONArray decrArr = new JSONArray(
							new JSONObject(reportArr.get(i).toString()).getString("oadrReportDescription"));


					if (decrArr.length() == 1) {
						JSONObject decr = new JSONObject(decrArr.get(0).toString());

						String minTime = decr.getString("oadrMinPeriod");
						String maxTime = decr.getString("oadrMaxPeriod");

						JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));

						double power = 0;

						for (int k = 0; k < powerAtts.length(); k++) {
							JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());
							power = powerAtt.getDouble("voltage");

//							cema_database cd = new cema_database();
//							cd.buildup(jsonObj.getString("venID"), "qos", "state", power, -1, -1, -1, -1, -1, -1, -1,
//									new Date(System.currentTimeMillis()).toString(),
//									new Date(System.currentTimeMillis()).toString(), -1);

						}
						
						double margin = global.emaProtocolCoAP.get(venID).getMargin();
						Emap_Cema_Profile profile = new Emap_Cema_Profile("MQTT", venID,
								registrationID, "qos", "state", power, -1, margin, -1, -1, -1, -1, -1, maxTime, minTime,
								-1, pullModel, timeTable, realTimetable, "CONNECT");

						global.emaProtocolCoAP.replace(venID, profile);
						
						if (global.EXPERIMENTAUTODR) {
							if (power > 10 && power >= margin * global.EXPERIMENTPERCENT) {
								setEvent(jsonObj.getString("venID"), margin, power);
							}
						}

					}

					else if (decrArr.length() > 1) {
						for (int j = 0; j < decrArr.length(); j++) {

							JSONObject decr = new JSONObject(decrArr.get(j).toString());
							if (decr.getString("rID").contains("EMA")) {
								
								String minTime = decr.getString("oadrMinPeriod");
								String maxTime = decr.getString("oadrMaxPeriod");

								JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));
								double power=0;
								
								for (int k = 0; k < powerAtts.length(); k++) {
									JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());

									power = powerAtt.getDouble("voltage");

//									cema_database cd = new cema_database();
//									cd.buildup(venID, "qos", "state", power, -1, -1, -1, -1, -1,
//											-1, -1, new Date(System.currentTimeMillis()).toString(),
//											new Date(System.currentTimeMillis()).toString(), -1);

								}
								
								double margin = global.emaProtocolCoAP.get(venID).getMargin();

								Emap_Cema_Profile profile = new Emap_Cema_Profile("MQTT",
										venID, registrationID, "qos", "state", power, -1, margin,
										-1, -1, -1, -1, -1, maxTime, minTime, -1, pullModel, timeTable,
										realTimetable, "CONNECT");

								global.emaProtocolCoAP.replace(venID, profile);
								
							} else {
								

								String minTime = decr.getString("oadrMinPeriod");
								String maxTime = decr.getString("oadrMaxPeriod");

								JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));
								String rID = decr.getString("rID");
								double totalPower = 0;

								double power = 0;
								for (int k = 0; k < powerAtts.length(); k++) {
									JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());

									power = powerAtt.getDouble("voltage");
									totalPower += power;

//									device_total_database dtd = new device_total_database();
//									dtd.buildUp(jsonObj.getString("venID"), rID, decr.getString("deviceType"), "state",
//											-1, -1, power, new Date(System.currentTimeMillis()).toString());

								}

								double margin = global.emaProtocolCoAP.get(venID).getMargin();

								deviceProfile = new Emap_Device_Profile(venID, rID,
										"LED", "qos", "state", power, -1, margin, -1, -1, -1, -1,
										-1, maxTime, minTime, -1);
								global.emaProtocolCoAP_Device.put(rID, deviceProfile);
								// 자동 DR
								if (global.EXPERIMENTAUTODR) {
									if (totalPower > 10 && totalPower >= margin * global.EXPERIMENTPERCENT) {
										setEvent(venID, margin, totalPower);
									}
								}

								
							}
						}
					}

				}

				com.mir.ems.profile.openadr.recent.UpdatedReport rdt = new com.mir.ems.profile.openadr.recent.UpdatedReport();
				rdt.setDestEMA(emaProfile.getEmaID());
				rdt.setRequestID(emaProfile.getRequestID());
				rdt.setResponseCode(200);
				rdt.setResponseDescription("OK");
				rdt.setService("oadrUpdatedReport");
				//
				String topic = "/OpenADR/" + emaProfile.getEmaID() + "/2.0b/EiReport";

				String payload = rdt.toString();
				new Publishing().publishThread(client, topic, global.qos, payload.getBytes());

				return "NORESPONSE";

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			try {
				jsonObj = new JSONObject(requestText);

				srcEMA = jsonObj.getString("SrcEMA");

				emaProfile = new Emap_Cema_Profile();
				jsonObj = new JSONObject(requestText);
				sub2JsonObj = new JSONObject(jsonObj.getString("EMAupdatedMgnInfo"));

				emaProfile.setEmaID(jsonObj.getString("SrcEMA")).setRequestID(jsonObj.getString("requestID"));

				Emap_Cema_Profile originVal = global.emaProtocolCoAP.get(jsonObj.getString("SrcEMA"));
				JSONObject topologyMgn = new JSONObject();

				double threshold = ((global.AVAILABLE_THRESHOLD / 20) * 1000) - (int) (Math.random() * (500 - 250))
						+ 250;

				double returnthreshold = sub2JsonObj.getDouble("margin");
				double returnPower = sub2JsonObj.getDouble("power");

				emaProfile = new Emap_Cema_Profile(jsonObj.getString("SrcEMA"), originVal.getqOs(),
						originVal.getRegistrationID(), sub2JsonObj.getInt("emaCNT"), originVal.getCustomerPriority(),
						sub2JsonObj.getDouble("margin"), sub2JsonObj.getDouble("minValue"),
						sub2JsonObj.getDouble("maxValue"), sub2JsonObj.getDouble("avgValue"), threshold,
						sub2JsonObj.getDouble("generate"), sub2JsonObj.getDouble("storage"),
						jsonObj.getJSONObject("EMAupdatedDRInfo"), jsonObj.getJSONObject("EMAupdatedMgnInfo"),
						"CONNECT");

				if (sub2JsonObj.getInt("emaCNT") > 0) {

					if (!sub2JsonObj.isNull("topology")) {

						JSONArray topologyInfo = new JSONArray(sub2JsonObj.getString("topology"));

						for (int i = 0; i < topologyInfo.length(); i++) {

							sub1JsonObj = new JSONObject(topologyInfo.get(i).toString());

							if (sub1JsonObj.getString("deviceType").contains("LED")) {
								deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"),
										sub1JsonObj.getString("deviceEMAID"), sub1JsonObj.getString("deviceType"),
										sub1JsonObj.getString("state"), sub1JsonObj.getInt("dimming"), 0,
										sub1JsonObj.getInt("priority"), sub1JsonObj.getDouble("power"), 0.0, 0.0, 0.0,
										0.0, 0.0);
							} else if (sub1JsonObj.getString("deviceType").contains("Solar")) {
								deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"),
										sub1JsonObj.getString("deviceEMAID"), sub1JsonObj.getString("deviceType"),
										sub1JsonObj.getString("state"), 0, 0, sub1JsonObj.getInt("priority"),
										sub1JsonObj.getDouble("power"), 0.0, 0.0, 0.0, 0.0, 0.0);
							} else if (sub1JsonObj.getString("deviceType").contains("Battery")) {
								deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"),
										sub1JsonObj.getString("deviceEMAID"), sub1JsonObj.getString("deviceType"),
										sub1JsonObj.getString("state"), 0, sub1JsonObj.getInt("mode"),
										sub1JsonObj.getInt("priority"), sub1JsonObj.getDouble("power"),
										sub1JsonObj.getDouble("capacity"), sub1JsonObj.getDouble("volt"),
										sub1JsonObj.getDouble("hz"), sub1JsonObj.getDouble("chargedEnergy"),
										sub1JsonObj.getDouble("soc"));
							} else if (sub1JsonObj.getString("deviceType").contains("Recloser")) {
								deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"),
										sub1JsonObj.getString("deviceEMAID"), sub1JsonObj.getString("deviceType"),
										sub1JsonObj.getString("state"), 0, 0, sub1JsonObj.getInt("priority"),
										sub1JsonObj.getDouble("power"), 0.0, sub1JsonObj.getDouble("volt"),
										sub1JsonObj.getDouble("hz"), 0.0, 0.0);
							} else if (sub1JsonObj.getString("deviceType").contains("RESOURCE")) {
								deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"),
										sub1JsonObj.getString("deviceEMAID"), sub1JsonObj.getString("deviceType"),
										sub1JsonObj.getString("state"), 0, 0, sub1JsonObj.getInt("priority"),
										sub1JsonObj.getDouble("power"), 0.0, 0.0, 0.0, 0.0, 0.0);
							}

							global.emaProtocolCoAP_Device.put(sub1JsonObj.getString("deviceEMAID"), deviceProfile);

						}
						topologyMgn.put("topology", topologyInfo);

					}
					topologyMgn.put("emaCNT", sub2JsonObj.getInt("emaCNT"));
				} else if (sub2JsonObj.getInt("emaCNT") <= 0) {
					topologyMgn.put("emaCNT", sub2JsonObj.getInt("emaCNT"));

				}

				global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), emaProfile);

				/*
				 * 수정해야되요!@!@!@!@!@!@!@!@!@!@!@!@!@!@!@!@!@!@
				 */
				JSONObject energyInfo = new JSONObject();

				energyInfo.put("threshold", returnthreshold);
				energyInfo.put("currentValue", returnPower);
				energyInfo.put("capacity", returnthreshold - returnPower);
				energyInfo.put("priority", 1);

				drmsg.put("SrcEMA", global.SYSTEM_ID);
				drmsg.put("DestEMA", emaProfile.getEmaID());
				drmsg.put("responseCode", 200);
				drmsg.put("responseDescription", "OK");
				drmsg.put("requestID", emaProfile.getRequestID());
				drmsg.put("EMAEnergyinfo", energyInfo);
				drmsg.put("EMATopologyinfo", topologyMgn);
				drmsg.put("service", "UpdatedReport");
				drmsg.put("time", new Date(System.currentTimeMillis()));

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return "UpdatedReport" + "&&" + drmsg.toString() + "&&" + srcEMA;
	}

	public void setEvent(String srcEMA, double margin, double power) {
		Calendar now = Calendar.getInstance();

		int sYear = now.get(Calendar.YEAR);
		int sMonth = now.get(Calendar.MONTH) + 1;
		int sDate = now.get(Calendar.DATE);
		String strYMD = sYear + "" + sMonth + "" + sDate;
		int sHour = now.get(Calendar.HOUR_OF_DAY);
		int sMin = now.get(Calendar.MINUTE);

		String sTime = sHour + "" + sMin + "" + "11";
		String eTime = (sHour + 1) + "" + sMin + "" + "11";

		double threshold = margin * (global.EXPERIMENTPERCENT);

		// PULL MODEL
		if (global.emaProtocolCoAP.get(srcEMA).isPullModel()) {
			global.emaProtocolCoAP_EventFlag.get(srcEMA).setEventFlag(true).setStartYMD(Integer.parseInt(strYMD))
					.setStartTime(Integer.parseInt(sTime)).setEndYMD(Integer.parseInt(strYMD))
					.setEndTime(Integer.parseInt(eTime)).setThreshold(threshold);
		}

		// PUSH MODEL
		if (!global.emaProtocolCoAP.get(srcEMA).isPullModel()) {

			global.initiater.eventOccur(srcEMA, 1, Integer.parseInt(strYMD), Integer.parseInt(sTime + "11"),
					Integer.parseInt(strYMD), Integer.parseInt(eTime + "11"), threshold);

		}

	}

}
