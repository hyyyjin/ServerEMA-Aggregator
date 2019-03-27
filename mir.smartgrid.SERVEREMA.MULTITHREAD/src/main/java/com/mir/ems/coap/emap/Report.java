package com.mir.ems.coap.emap;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.globalVar.global;
import com.mir.update.database.cema_database;
import com.mir.update.database.device_total_database;

public class Report extends CoapResource {

	enum Type {
		REGISTERREPORT, UPDATEREPORT
	}

	public Report(String name) {
		super(name);
	}

	@Override
	public void handleGET(CoapExchange exchange) {
		exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
	}

	@Override
	public void handlePOST(CoapExchange exchange) {
		exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
	}

	@Override
	public void handleDELETE(CoapExchange exchange) {
		exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
	}

	@Override
	public void handlePUT(CoapExchange exchange) {

		if (getPath().contains(global.version)) {
			try {
				String version = "EMAP" + getPath().split("/")[3];

				JSONObject json = new JSONObject(exchange.getRequestText().toString());
				String service = json.getString("service");

				new ReportType(getName(), service, exchange, version).start();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (getPath().contains(global.openADRVersion)) {

			try {

				String version = "OpenADR" + getPath().split("/")[3];

				JSONObject json = new JSONObject(exchange.getRequestText().toString());
				String service = json.getString("service");
				service = service.replaceAll("oadr", "");

				new ReportType(getName(), service, exchange, version).start();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else {
			new ReportType(getName(), exchange).start();

		}

	}

	class ReportType extends Thread {

		CoapExchange exchange;
		String incomingType, requestText, setPayload;

		Emap_Cema_Profile emaProfile;
		Emap_Device_Profile deviceProfile;
		private JSONObject jsonObj;
		private JSONObject sub1JsonObj;
		private JSONObject sub2JsonObj;

		private String version, service;

		ReportType(String incomingType, String service, CoapExchange exchange, String version) {
			this.exchange = exchange;
			this.incomingType = incomingType;
			this.requestText = exchange.getRequestText();
			this.version = version;
			this.service = service;
		}

		ReportType(String incomingType, CoapExchange exchange) {
			this.exchange = exchange;
			this.incomingType = incomingType;
			this.requestText = exchange.getRequestText();
		}

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
				type = Type.valueOf(incomingType.toUpperCase());

			}

			switch (type) {

			case UPDATEREPORT:
				this.setPayload = acknowledgeUPDATEREPORT(requestText);
				break;
			default:
				this.setPayload = "TYPE WRONG";
				break;
			}

			if (this.setPayload.equals("TYPE WRONG"))
				this.exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
			else if (this.setPayload.toUpperCase().equals("NORESPONSE")) {

			} else
				this.exchange.respond(ResponseCode.CONTENT, this.setPayload, MediaTypeRegistry.APPLICATION_JSON);

		}

		public String acknowledgeUPDATEREPORT(String requestText) {
			JSONObject drmsg = new JSONObject();
			String srcEMA = null;

			if (version.equals("EMAP1.0b")) {
				try {

//					System.out.println(requestText);
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
							// margin = decr.getDouble("margin");
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

							Emap_Cema_Profile profile = new Emap_Cema_Profile("COAP", jsonObj.getString("SrcEMA"),
									registrationID, qos, state, power, dimming, margin, generate, storage, maxValue,
									minValue, avgValue, maxTime, minTime, priority, pullModel, timeTable, realTimetable,
									"CONNECT");

							global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), profile);

							cema_database cd = new cema_database();
							cd.buildup(jsonObj.getString("SrcEMA"), qos, state, power, dimming, margin, generate,
									storage, maxValue, minValue, avgValue, maxTime, minTime, priority);

						}

						else if (jsonObj.getString("type").equals("Explicit") || 1 < decrArr.length()) {

							for (int j = 0; j < decrArr.length(); j++) {

								JSONObject decr = new JSONObject(decrArr.get(j).toString());

								String qos, registrationID, state, minTime, maxTime, rID;
								double margin, minValue, maxValue, avgValue, power, generate, storage;
								int priority, dimming;
								boolean pullModel;

								if (decr.getString("deviceType").contains("EMA")) {
									qos = decr.getString("qos");
									registrationID = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA"))
											.getRegistrationID();
									// margin = decr.getDouble("margin");
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
									boolean timeTable = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA"))
											.isTableChanged();

									// Threshold 기준 80%이상을 사용할 경우 이벤트 시그널

									margin = global.emaProtocolCoAP.get(jsonObj.get("SrcEMA")).getMargin();
//
//									if (global.EXPERIMENTAUTODR) {
//										if (power >= margin * global.EXPERIMENTPERCENT) {
//
//											setEvent(jsonObj.getString("SrcEMA"), margin, power);
//
//										}
//									}

									Emap_Cema_Profile profile = new Emap_Cema_Profile("COAP",
											jsonObj.getString("SrcEMA"), registrationID, qos, state, power, dimming,
											margin, generate, storage, maxValue, minValue, avgValue, maxTime, minTime,
											priority, pullModel, timeTable, realTimetable, "CONNECT");

									global.emaProtocolCoAP.replace(jsonObj.getString("SrcEMA"), profile);

									cema_database cd = new cema_database();
									cd.buildup(jsonObj.getString("SrcEMA"), qos, state, power, dimming, margin,
											generate, storage, maxValue, minValue, avgValue, maxTime, minTime,
											priority);

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

									deviceProfile = new Emap_Device_Profile(jsonObj.getString("SrcEMA"), rID,
											decr.getString("deviceType"), qos, state, power, dimming, margin, generate,
											storage, maxValue, minValue, avgValue, maxTime, minTime, priority);

									
									if (!global.emaProtocolCoAP_Device.containsKey(rID)) {
										global.emaProtocolCoAP_Device.put(rID, deviceProfile);
									} else {
										global.emaProtocolCoAP_Device.replace(rID, deviceProfile);
									}

									device_total_database dtd = new device_total_database();
									dtd.buildUp(jsonObj.getString("SrcEMA"), rID, decr.getString("deviceType"), state,
											dimming, priority, power, new Date(System.currentTimeMillis()).toString());

								}

								JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));

								for (int k = 0; k < powerAtts.length(); k++) {
									JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());
								}

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

					String payload = udt.toString();

					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			else if (version.equals("OpenADR2.0b")) {

				try {
					jsonObj = new JSONObject(requestText);

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("venID")).setRequestID(jsonObj.getString("requestID"));
					String registrationID = global.emaProtocolCoAP.get(jsonObj.get("venID")).getRegistrationID();
					boolean pullModel = global.emaProtocolCoAP.get(jsonObj.get("venID")).isPullModel();
					boolean realTimetable = global.emaProtocolCoAP.get(jsonObj.get("venID")).isRealTimetableChanged();
					boolean timeTable = global.emaProtocolCoAP.get(jsonObj.get("venID")).isTableChanged();

					JSONArray reportArr = new JSONArray(jsonObj.getString("oadrReport"));

					for (int i = 0; i < reportArr.length(); i++) {

						JSONArray decrArr = new JSONArray(
								new JSONObject(reportArr.get(i).toString()).getString("oadrReportDescription"));

						if (reportArr.length() == 1) {
							JSONObject decr = new JSONObject(decrArr.get(0).toString());

							String minTime = decr.getString("oadrMinPeriod");
							String maxTime = decr.getString("oadrMaxPeriod");

							JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));

							double power = 0;
							for (int k = 0; k < powerAtts.length(); k++) {
								JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());
								// System.out.println(powerAtt.getString("voltage"));

								power = powerAtt.getDouble("voltage");

								// 오늘 여기
								Emap_Cema_Profile profile = new Emap_Cema_Profile("COAP", jsonObj.getString("venID"),
										registrationID, "qos", "state", power, -1, -1, -1, -1, -1, -1, -1, maxTime,
										minTime, -1, pullModel, timeTable, realTimetable, "CONNECT");

								global.emaProtocolCoAP.replace(jsonObj.getString("venID"), profile);

								global.emaProtocolCoAP.replace(jsonObj.getString("venID"), profile);
								// global.emaThresholdManage.put(jsonObj.getString("venID"),
								// power);

								cema_database cd = new cema_database();
								cd.buildup(jsonObj.getString("venID"), "qos", "state", power, -1, -1, -1, -1, -1, -1,
										-1, new Date(System.currentTimeMillis()).toString(),
										new Date(System.currentTimeMillis()).toString(), -1);

							}

							// 자동 DR
							double margin = global.emaProtocolCoAP.get(jsonObj.getString("venID")).getMargin();

							if (global.EXPERIMENTAUTODR) {
								if (power > 10 && power >= margin * global.EXPERIMENTPERCENT) {
									setEvent(jsonObj.getString("venID"), margin, power);
								}
							}

						}

						else if (reportArr.length() > 1) {
							JSONObject decr = new JSONObject(decrArr.get(i).toString());

							if (decr.getString("rID").contains("EMA")) {
								String minTime = decr.getString("oadrMinPeriod");
								String maxTime = decr.getString("oadrMaxPeriod");

								JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));

								for (int k = 0; k < powerAtts.length(); k++) {
									JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());
									// System.out.println(powerAtt.getString("voltage"));

									double power = powerAtt.getDouble("voltage");

									// 오늘 여기
									Emap_Cema_Profile profile = new Emap_Cema_Profile("COAP",
											jsonObj.getString("venID"), registrationID, "qos", "state", power, -1, -1,
											-1, -1, -1, -1, -1, maxTime, minTime, -1, pullModel, timeTable,
											realTimetable, "CONNECT");

									global.emaProtocolCoAP.replace(jsonObj.getString("venID"), profile);

									global.emaProtocolCoAP.replace(jsonObj.getString("venID"), profile);
									// global.emaThresholdManage.put(jsonObj.getString("venID"),
									// power);

									cema_database cd = new cema_database();
									cd.buildup(jsonObj.getString("venID"), "qos", "state", power, -1, -1, -1, -1, -1,
											-1, -1, new Date(System.currentTimeMillis()).toString(),
											new Date(System.currentTimeMillis()).toString(), -1);

								}
							} else {
								String minTime = decr.getString("oadrMinPeriod");
								String maxTime = decr.getString("oadrMaxPeriod");

								JSONArray powerAtts = new JSONArray(decr.getString("powerAttributes"));

								double totalPower = 0;

								for (int k = 0; k < powerAtts.length(); k++) {
									JSONObject powerAtt = new JSONObject(powerAtts.get(k).toString());
									// System.out.println(powerAtt.getString("voltage"));

									double power = powerAtt.getDouble("voltage");
									totalPower += power;
									String rID = decr.getString("rID");
									deviceProfile = new Emap_Device_Profile(jsonObj.getString("venID"), rID,
											decr.getString("deviceType"), "qos", "state", power, -1, -1, -1, -1, -1, -1,
											-1, maxTime, minTime, -1);

									global.emaProtocolCoAP_Device.replace(rID, deviceProfile);

									device_total_database dtd = new device_total_database();
									dtd.buildUp(jsonObj.getString("venID"), rID, decr.getString("deviceType"), "state",
											-1, -1, power, new Date(System.currentTimeMillis()).toString());

								}

								// 자동 DR
								double margin = global.emaThresholdManage.get(jsonObj.getString("venID")).doubleValue();
								if (global.EXPERIMENTAUTODR) {
									if (totalPower > 10 && totalPower >= margin * global.EXPERIMENTPERCENT) {
										setEvent(jsonObj.getString("venID"), margin, totalPower);
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
					String payload = rdt.toString();
					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			return drmsg.toString();
		}

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
