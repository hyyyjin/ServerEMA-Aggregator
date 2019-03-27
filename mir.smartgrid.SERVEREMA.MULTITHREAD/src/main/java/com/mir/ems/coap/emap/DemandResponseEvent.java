package com.mir.ems.coap.emap;

import java.awt.Toolkit;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mir.ems.database.item.Emap_Device_Profile;
import com.mir.ems.GUI.MainFrame;
import com.mir.ems.algorithm.Greedy;
import com.mir.ems.beep.BeepSound;
import com.mir.ems.coap.emap.Report.ReportType;
import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.globalVar.global;
import com.mir.ems.hashMap.ESS_values;
import com.mir.ems.hashMap.PV_values;
import com.mir.ems.mqtt.Publishing;
import com.mir.ems.profile.emap.v2.Event;
import com.mir.ems.profile.emap.v2.EventResponse;
import com.mir.ems.profile.emap.v2.EventSignals;
import com.mir.ems.profile.emap.v2.Intervals;

public class DemandResponseEvent extends CoapResource {

	enum Type {
		POLL, CREATEDEVENT, REQUESTEVENT
	}

	public DemandResponseEvent(String name) {
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

		// System.out.println(exchange.getRequestText().toString());
		// System.out.println(getName());

		// exchange.respond(ResponseCode.CONTENT, "Event");

		// new EventType(getName(), exchange).start();

		// System.out.println(exchange.getRequestText().toString());
		// System.out.println(getPath());

		if (getPath().contains(global.version)) {

			try {

				String version = "EMAP" + getPath().split("/")[3];

				JSONObject json = new JSONObject(exchange.getRequestText().toString());
				String service = json.getString("service");

				new EventType(getName(), service, exchange, version).start();

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
				service = service.replaceAll("Oadr", "");

				new EventType(getName(), service, exchange, version).start();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		else {
			new EventType(getName(), exchange).start();

		}

	}

	class EventType extends Thread {

		Emap_Cema_Profile emaProfile;
		Emap_Device_Profile deviceProfile;
		private JSONObject jsonObj;
		CoapExchange exchange;
		String incomingType, requestText, setPayload;
		private String version, service;

		EventType(String incomingType, String service, CoapExchange exchange, String version) {
			this.exchange = exchange;
			this.incomingType = incomingType;
			this.requestText = exchange.getRequestText();
			this.version = version;
			this.service = service;
		}

		EventType(String incomingType, CoapExchange exchange) {
			this.exchange = exchange;
			this.incomingType = incomingType;
			this.requestText = exchange.getRequestText();
		}

		@Override
		public void run() {

			// Type type = Type.valueOf(incomingType.toUpperCase());

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

			case REQUESTEVENT:
				this.setPayload = acknowledgeREQUESTEVENT(requestText);
				break;

			case POLL:
				this.setPayload = acknowledgePOLL(this.requestText);
				break;
			case CREATEDEVENT:
				this.setPayload = acknowledgeCREATEDEVENT(this.requestText);
				break;
			default:
				this.setPayload = "TYPE WRONG";
			}

			if (this.setPayload.equals("TYPE WRONG"))
				this.exchange.respond(ResponseCode.FORBIDDEN, "Wrong Access");
			else if (this.setPayload.equals("NORESPONSE")) {

			} else
				this.exchange.respond(ResponseCode.CONTENT, this.setPayload, MediaTypeRegistry.APPLICATION_JSON);

		}

		public String acknowledgePOLL(String requestText) {
			JSONObject drmsg = new JSONObject();

			String srcEMA = null;

			if (version.equals("EMAP1.0b")) {

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile = new Emap_Cema_Profile();

					String emaID = jsonObj.getString("SRCEMA");

					emaProfile.setEmaID(emaID);

					boolean flag = false;
					if (global.emaProtocolCoAP_EventFlag.containsKey(emaID)) {
						flag = global.emaProtocolCoAP_EventFlag.get(emaID).isEventFlag();

					}

					if (global.tableHasChanged) {

						Intervals interval = new Intervals();

						interval.addIntervalsParams(global.duration, "uid", 0.0);

						Event event = new Event();

						for (int i = 0; i < global.priceTable.size(); i++) {

							if (global.priceTable.get(i).getType() != null) {
								if (global.priceTable.get(i).getType().equals("Industrial1")) {

									for (int j = 0; j < 3; j++) {

										String vtnComment = "";
										double price = 0;

										if (j == 0) {
											vtnComment = "Summer";
											price = global.priceTable.get(i).getSummer();
										} else if (j == 1) {
											vtnComment = "Spring/Fall";
											price = global.priceTable.get(i).getSpringFall();
										} else if (j == 2) {
											vtnComment = "Winter";
											price = global.priceTable.get(i).getWinter();
										}

										event.addEventParams("eventID", new EventSignals()
												.addEventSignalsParams(interval.getIntervalsParams(), "signalName",
														"Price Event", "signalID", 0, 0, 0, price, "KW/WON")
												.getEventSignalsParams(), 1, "modificationReason", -1,
												"http://cyber.kepco.co.kr/ckepco/front/jsp/CY/E/E/CYEEHP00103.jsp",
												global.createTableDate, "eventStatus", global.tableHasChanged,
												vtnComment, "properties", "components", emaProfile.getEmaID(),
												"dtStart", "duration", "tolerance", "notification", "rampUp",
												"recovery");

									}
								}

								else if (global.priceTable.get(i).getType().equals("Industrial2")) {

									// 추후 가격정보에 대한 더 상세한 자료가 필요할 경우 추가, 가격표는
									// ElectricityPrice.txt 정보 참조

								} else if (global.priceTable.get(i).getType().equals("Industrial3")) {

									// 추후 가격정보에 대한 더 상세한 자료가 필요할 경우 추가, 가격표는
									// ElectricityPrice.txt 정보 참조

								}
							}

						}

						global.tableHasChanged = false;

						EventResponse er = new EventResponse();
						er.setRequestID(emaProfile.getRequestID());
						er.setResponseCode(200);
						er.setResponseDescription("OK");

						com.mir.ems.profile.emap.v2.DistributeEvent drE = new com.mir.ems.profile.emap.v2.DistributeEvent();

						drE.setDestEMA(emaProfile.getEmaID());
						drE.setEvent(event.getEventParams());
						drE.setRequestID(emaProfile.getRequestID());
						drE.setResponse(er.toString());
						drE.setService("DistributeEvent");
						drE.setSrcEMA(global.SYSTEM_ID);
						drE.setTime(new Date(System.currentTimeMillis()).toString());
						drE.setResponseRequired("Always");

						String payload = drE.toString();

						this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

						return "NORESPONSE";

					}

					if (!flag && !global.tableHasChanged) {

						com.mir.ems.profile.emap.v2.Response response = new com.mir.ems.profile.emap.v2.Response();
						response.setDestEMA(emaProfile.getEmaID());
						response.setRequestID("");
						response.setResponseCode(200);
						response.setResponseDescription("OK");
						response.setService("Response");
						response.setSrcEMA(global.SYSTEM_ID);
						response.setTime(new Date(System.currentTimeMillis()).toString());

						String payload = response.toString();

						this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

						return "NORESPONSE";

					} else if (!global.tableHasChanged) {

						
						System.out.println("여기뭐여");
						
						double threshold = global.emaProtocolCoAP_EventFlag.get(emaID).getThreshold();
						int strTime = global.emaProtocolCoAP_EventFlag.get(emaID).getStartTime();
						int strYMD = global.emaProtocolCoAP_EventFlag.get(emaID).getStartYMD();
						int endTime = global.emaProtocolCoAP_EventFlag.get(emaID).getEndTime();
						int endYMD = global.emaProtocolCoAP_EventFlag.get(emaID).getEndYMD();

						String strTime_str = strTime + "";
						String endTime_str = endTime + "";

						strTime_str = strTime_str.length() < 6 ? "0" + strTime_str : strTime_str;
						endTime_str = endTime_str.length() < 6 ? "0" + endTime_str : endTime_str;

						String eventDuration = ISO8601(strYMD, strTime, endYMD, endTime);
						String timeForm = (strYMD + "") + strTime_str;
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

						try {

							// Date createdDateTime = sdf.parse(timeForm);
							Intervals interval = new Intervals();
							interval.addIntervalsParams(global.duration, "uid", 0.0);

							EventSignals es = new EventSignals();
							es.addEventSignalsParams(interval.getIntervalsParams(), "Distribute", "Event", "signalID",
									0, threshold, 0, 0, "KW");

							String eventID = global.emaProtocolCoAP_EventFlag.get(emaID).getEventID();

							Event event = new Event();
							event.addEventParams(eventID, es.getEventSignalsParams(), 1, "None", 2, "marketContext",
									new Date(System.currentTimeMillis()).toString(), "Event", false, "Event", "None",
									"None", "None", new Date(System.currentTimeMillis()).toString(), eventDuration,
									"None", "None", "None", "None");

							EventResponse er = new EventResponse();
							er.setRequestID(emaProfile.getRequestID());
							er.setResponseCode(200);
							er.setResponseDescription("OK");

							com.mir.ems.profile.emap.v2.DistributeEvent drE = new com.mir.ems.profile.emap.v2.DistributeEvent();

							drE.setDestEMA(emaProfile.getEmaID());
							drE.setEvent(event.getEventParams());
							drE.setRequestID(emaProfile.getRequestID());
							drE.setResponse(er.toString());
							drE.setService("DistributeEvent");
							drE.setSrcEMA(global.SYSTEM_ID);
							drE.setTime(new Date(System.currentTimeMillis()).toString());
							drE.setResponseRequired("Always");

							String payload = drE.toString();

							this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

							if (!global.eventFromServer) {
								if (global.autoDR) {
									if (global.autoDRCNT < global.autoDRTOTAL) {
										global.autoDRCNT += 1;
										eventID = global.eventID.get(global.autoDRCNT);
										global.emaProtocolCoAP_EventFlag.get(emaProfile.getEmaID()).setEventID(eventID);
										System.out.println(global.autoDRCNT + "===>");
									} else {
										global.emaProtocolCoAP_EventFlag.get(emaID).setEventFlag(false);
									}
								}

								else {
									global.emaProtocolCoAP_EventFlag.get(emaID).setEventFlag(false);
								}
							} else {
								global.emaProtocolCoAP_EventFlag.get(emaID).setEventFlag(false);
							}
							// global.emaProtocolCoAP_EventFlag.get(emaID).setEventFlag(false);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return "NORESPONSE";
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "NORESPONSE";

			}

			else if (version.equals("OpenADR2.0b")) {

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile = new Emap_Cema_Profile();

					String emaID = jsonObj.getString("VENID");

					emaProfile.setEmaID(emaID);

					boolean flag = false;
					if (global.emaProtocolCoAP_EventFlag.containsKey(emaID)) {
						flag = global.emaProtocolCoAP_EventFlag.get(emaID).isEventFlag();

					}

					if (global.venRegisterFlag.get(emaID).intValue() == 0) {

						global.venRegisterFlag.replace(emaID, 1);

						com.mir.ems.profile.openadr.recent.RegisterReport rt2 = new com.mir.ems.profile.openadr.recent.RegisterReport();
						rt2.setDestEMA(emaProfile.getEmaID());
						rt2.setRequestID("REQUESTID");
						rt2.setService("oadrRegisterReport");

						String payload = rt2.toString();
						this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

						return "NORESPONSE";

					}

					if (global.venRegisterFlag.get(emaID).intValue() > 0 && !flag && !global.tableHasChanged) {

						com.mir.ems.profile.openadr.recent.Response response = new com.mir.ems.profile.openadr.recent.Response();
						response.setDestEMA(emaProfile.getEmaID());
						response.setRequestID("");
						response.setResponseCode(200);
						response.setResponseDescription("OK");
						response.setService("oadrResponse");

						String payload = response.toString();
						this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

						return "NORESPONSE";

					} else if (!global.tableHasChanged) {

						double threshold = global.emaProtocolCoAP_EventFlag.get(emaID).getThreshold();
						int strTime = global.emaProtocolCoAP_EventFlag.get(emaID).getStartTime();
						int strYMD = global.emaProtocolCoAP_EventFlag.get(emaID).getStartYMD();
						int endTime = global.emaProtocolCoAP_EventFlag.get(emaID).getEndTime();
						int endYMD = global.emaProtocolCoAP_EventFlag.get(emaID).getEndYMD();

						String strTime_str = strTime + "";
						String endTime_str = endTime + "";

						strTime_str = strTime_str.length() < 6 ? "0" + strTime_str : strTime_str;
						endTime_str = endTime_str.length() < 6 ? "0" + endTime_str : endTime_str;

						String eventDuration = ISO8601(strYMD, strTime, endYMD, endTime);
						String timeForm = (strYMD + "") + strTime_str;
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

						try {

							// Date createdDateTime = sdf.parse(timeForm);

							Intervals interval = new Intervals();
							interval.addIntervalsParams(global.duration, "uid", threshold);

							com.mir.ems.profile.openadr.recent.Event event = new com.mir.ems.profile.openadr.recent.Event();
							// Threshold 정보 전달

							event.addEventParams("eventID",
									new com.mir.ems.profile.openadr.recent.EventSignals()
											.addEventSignalsParams(interval.getIntervalsParams(), "signalName",
													"Control Event", "signalID", 0)
											.getEventSignalsParams(),
									1, "modificationReason", -1, "mirLab",
									new Date(System.currentTimeMillis()).toString(), "eventStatus", false, "Event",
									"properties", "components", emaProfile.getEmaID(),
									new Date(System.currentTimeMillis()).toString(), eventDuration, "tolerance",
									"notification", "rampUp", "recovery");

							com.mir.ems.profile.openadr.recent.EventResponse er = new com.mir.ems.profile.openadr.recent.EventResponse();
							er.setRequestID(emaProfile.getRequestID());
							er.setResponseCode(200);
							er.setResponseDescription("OK");

							com.mir.ems.profile.openadr.recent.DistributeEvent drE = new com.mir.ems.profile.openadr.recent.DistributeEvent();

							drE.setSrcEMA(global.SYSTEM_ID);
							drE.setEvent(event.getEventParams());
							drE.setRequestID(emaProfile.getRequestID());
							drE.setResponse(er.toString());
							drE.setService("oadrDistributeEvent");
							drE.setResponseRequired("Always");

							String payload = drE.toString();
							this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

							global.emaProtocolCoAP_EventFlag.get(emaID).setEventFlag(false);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return "NORESPONSE";
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "NORESPONSE";

			}

			else {

				try {

					emaProfile = new Emap_Cema_Profile();

					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setType(jsonObj.getString("TYPE"))
							.setRequestID(jsonObj.getString("REQUESTID"));

					String emaID = jsonObj.getString("SRCEMA");

					// System.out.println("이벤이벤");

					// ADD Flag
					if (emaProfile.getType().toUpperCase().equals("REGISTRATION")) {
						// System.out.println("여기다");
						drmsg.put("SrcEMA", global.SYSTEM_ID);
						drmsg.put("DestEMA", emaProfile.getEmaID());
						drmsg.put("version", emaProfile.getVersion());
						drmsg.put("requestID", emaProfile.getRequestID());
						drmsg.put("reportName", "InitReport");
						drmsg.put("reportType", "Registration");
						drmsg.put("EMAregisteredDRInformation",
								global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getEMARegisteredInfo());
						drmsg.put("EMAregisteredMgnInformation",
								global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getEMARegisteredMgnInfo());

						drmsg.put("time", new Date(System.currentTimeMillis()));
						drmsg.put("service", "RegisterReport");

						return drmsg.toString();

					} else if (emaProfile.getType().toUpperCase().equals("PERIODICAL")) {
						boolean flag = false;
						if (global.emaProtocolCoAP_EventFlag.containsKey(emaID)) {
							flag = global.emaProtocolCoAP_EventFlag.get(emaID).isEventFlag();

						}
						if (!flag) {

							double threshold = (global.AVAILABLE_THRESHOLD / 20) * 1000;
							drmsg.put("SrcEMA", global.SYSTEM_ID);
							drmsg.put("DestEMA", emaProfile.getEmaID());
							drmsg.put("responseCode", 200);
							drmsg.put("responseDescription", "OK");
							drmsg.put("requestID", emaProfile.getRequestID());
							drmsg.put("type", emaProfile.getType());
							drmsg.put("version", emaProfile.getVersion());
							drmsg.put("threshold", threshold);
							drmsg.put("service", "Response");
							drmsg.put("time", new Date(System.currentTimeMillis()));
							global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).setMargin(threshold);
							return drmsg.toString();

						} else {
							emaProfile = new Emap_Cema_Profile();
							jsonObj = new JSONObject(requestText.toUpperCase());

							emaProfile.setEmaID(jsonObj.getString("SRCEMA"))
									.setRequestID(jsonObj.getString("REQUESTID"));

							drmsg.put("SrcEMA", global.SYSTEM_ID);
							drmsg.put("DestEMA", emaProfile.getEmaID());
							drmsg.put("requestID", emaProfile.getRequestID());
							drmsg.put("responseCode", 200);
							drmsg.put("responseDescription", "OK");
							drmsg.put("time", new Date(System.currentTimeMillis()));

							JSONArray eventInfo = new JSONArray();
							JSONObject childFormat = new JSONObject();
							childFormat.put("profileName", global.profileName);
							childFormat.put("modificationNumber", 0);

							childFormat.put("testEvent", false);
							childFormat.put("marketContext", 1);
							childFormat.put("eventStatus", "DREVENT");
							childFormat.put("priority", 0);
							childFormat.put("eventID", 0);
							childFormat.put("startYMD",
									global.emaProtocolCoAP_EventFlag.get(emaProfile.getEmaID()).getStartYMD());
							childFormat.put("startTime",
									global.emaProtocolCoAP_EventFlag.get(emaProfile.getEmaID()).getStartTime());
							childFormat.put("endYMD",
									global.emaProtocolCoAP_EventFlag.get(emaProfile.getEmaID()).getEndYMD());
							childFormat.put("endTime",
									global.emaProtocolCoAP_EventFlag.get(emaProfile.getEmaID()).getEndTime());
							childFormat.put("duration", 1000);
							childFormat.put("uid", 0);
							double currentValue = global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getPower();
							double threshold = global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getMargin();
							childFormat.put("currentValue", currentValue);
							childFormat.put("signalName", "DREVENT");
							childFormat.put("signalType", "DistributeEvent");
							childFormat.put("signalID", 1);
							childFormat.put("threshold", threshold);
							childFormat.put("capacity", threshold - currentValue);
							eventInfo.put(childFormat);
							drmsg.put("EMADREventInformation", eventInfo);

							eventInfo = new JSONArray();
							childFormat = new JSONObject();
							childFormat.put("price", 1000);
							childFormat.put("unit", "KW");
							eventInfo.put(childFormat);
							drmsg.put("EMADRPriceInformation", eventInfo);

							drmsg.put("service", "DistributeEvent");
							drmsg.put("type", "Level");
							global.emaProtocolCoAP_EventFlag.get(jsonObj.getString("SRCEMA")).setEventFlag(false);
							return drmsg.toString();

						}
					}
					return drmsg.toString();
					// return "aa";
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					return "NORESPONSE";
				}
			}
		}

		public String acknowledgeCREATEDEVENT(String requestText) {
			JSONObject drmsg = new JSONObject();
			String srcEMA = null;

			if (version.equals("EMAP1.0b")) {

				// System.out.println("받앗니?");

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());
					String emaID = jsonObj.getString("SRCEMA");

					com.mir.ems.profile.emap.v2.Response response = new com.mir.ems.profile.emap.v2.Response();
					response.setDestEMA(emaID);
					response.setRequestID("");
					response.setResponseCode(200);
					response.setResponseDescription("OK");
					response.setService("Response");
					response.setSrcEMA(global.SYSTEM_ID);
					response.setTime(new Date(System.currentTimeMillis()).toString());

					String payload = response.toString();

					// System.out.println(payload.toString());

					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			else if (version.equals("OpenADR2.0b")) {

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());
					String emaID = jsonObj.getString("VENID");

					com.mir.ems.profile.openadr.recent.Response response = new com.mir.ems.profile.openadr.recent.Response();
					response.setDestEMA(emaID);
					response.setRequestID("");
					response.setResponseCode(200);
					response.setResponseDescription("OK");
					response.setService("oadrResponse");

					String payload = response.toString();
					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				try {

					emaProfile = new Emap_Cema_Profile();

					jsonObj = new JSONObject(requestText.toUpperCase());

					// emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setType(jsonObj.getString("OPTTYPE"))
					// .setRequestID(jsonObj.getInt("REQUESTID"));
					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"));

					// opt in & out check
					drmsg.put("SrcEMA", global.SYSTEM_ID);
					drmsg.put("DestEMA", emaProfile.getEmaID());
					drmsg.put("responseCode", 200);
					drmsg.put("version", emaProfile.getVersion());
					drmsg.put("responseDescription", "OK");
					drmsg.put("requestID", emaProfile.getRequestID());
					drmsg.put("service", "Response");
					drmsg.put("time", new Date(System.currentTimeMillis()));

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return drmsg.toString();
		}

		public String acknowledgeREQUESTEVENT(String requestText) {
			JSONObject drmsg = new JSONObject();
			String srcEMA = null;

			if (version.equals("EMAP1.0b")) {

				// Threshold 분배 알고리즘 시작, Thread 아님, 반드시 이게 끝나고 분배해줘야 하기 때문
				// new Greedy().start();

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"));

					Intervals interval = new Intervals();

					interval.addIntervalsParams(global.duration, "uid", 0.0);

					EventSignals es = new EventSignals();
					Event event = new Event();

					// Threshold 정보 전달

					event.addEventParams("eventID",
							new EventSignals().addEventSignalsParams(interval.getIntervalsParams(), "signalName",
									"Control Event", "signalID", 0,
									global.emaThresholdManage.get(jsonObj.getString("SRCEMA")).doubleValue(), 0, 0,
									"KW/WON").getEventSignalsParams(),
							1, "modificationReason", -1, "mirLab", new Date(System.currentTimeMillis()).toString(),
							"eventStatus", false, "SessionSetup", "properties", "components", emaProfile.getEmaID(),
							new Date(System.currentTimeMillis()).toString(), "PT1H", "tolerance", "notification",
							"rampUp", "recovery");

					// Passive 기본 가격정보 전송

					if (global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).isTableChanged()) {

						for (int i = 0; i < global.priceTable.size(); i++) {

							if (global.priceTable.get(i).getType() != null) {
								if (global.priceTable.get(i).getType().equals("Industrial1")) {

									for (int j = 0; j < 3; j++) {

										String vtnComment = "";
										double price = 0;

										if (j == 0) {
											vtnComment = "Summer";
											price = global.priceTable.get(i).getSummer();
										} else if (j == 1) {
											vtnComment = "Spring/Fall";
											price = global.priceTable.get(i).getSpringFall();
										} else if (j == 2) {
											vtnComment = "Winter";
											price = global.priceTable.get(i).getWinter();
										}

										event.addEventParams("eventID", new EventSignals()
												.addEventSignalsParams(interval.getIntervalsParams(), "signalName",
														"Price Event", "signalID", 0, 0, 0, price, "KW/WON")
												.getEventSignalsParams(), 1, "modificationReason", -1,
												"http://cyber.kepco.co.kr/ckepco/front/jsp/CY/E/E/CYEEHP00103.jsp",
												global.createTableDate, "eventStatus", true, vtnComment, "properties",
												"components", emaProfile.getEmaID(), "dtStart", "duration", "tolerance",
												"notification", "rampUp", "recovery");

									}
								}

								else if (global.priceTable.get(i).getType().equals("Industrial2")) {
									// 추후 가격정보에 대한 더 상세한 자료가 필요할 경우 추가, 가격표는
									// ElectricityPrice.txt 정보 참조
								} else if (global.priceTable.get(i).getType().equals("Industrial3")) {
									// 추후 가격정보에 대한 더 상세한 자료가 필요할 경우 추가, 가격표는
									// ElectricityPrice.txt 정보 참조
								}
							}
						}

						global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).setTableChanged(false);
					}

					// Active Mode 일때 실시간 가격정보 전달
					if (MainFrame.rdbtnmntmNewRadioItem_1.isSelected()) {

						if (global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).isRealTimetableChanged()) {

							Calendar now = Calendar.getInstance();
							int cHour = now.get(Calendar.HOUR_OF_DAY);
							int year = now.get(Calendar.YEAR);
							int month = now.get(Calendar.MONTH) + 1;
							int date = now.get(Calendar.DATE);

							for (int i = 0; i < global.realTimePriceTable.size(); i++) {

								int time = Integer
										.parseInt(global.realTimePriceTable.get(i).getStrTime().split(":")[0]);

								if (time >= cHour) {

									double price = global.realTimePriceTable.get(i).getPrice();

									event.addEventParams("eventID",
											new EventSignals()
													.addEventSignalsParams(interval.getIntervalsParams(), "signalName",
															"Price Event", "signalID", 0, 0, 0, price, "KW/WON")
													.getEventSignalsParams(),
											1, "modificationReason", -1,
											"https://hourlypricing.comed.com/live-prices/?date=20180826",
											new Date(System.currentTimeMillis()).toString(), "eventStatus", true,
											"RealTimePricing", "properties", "components", emaProfile.getEmaID(),
											new Date(year, month, date, time, 0).toString(), "PT1H", "tolerance",
											"notification", "rampUp", "recovery");
								}
							}

							global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).setRealTimetableChanged(false);

						}
					}

					/*-----------------여기까지가 Active Mode --------------*/

					EventResponse er = new EventResponse();
					er.setRequestID(emaProfile.getRequestID());
					er.setResponseCode(200);
					er.setResponseDescription("OK");

					com.mir.ems.profile.emap.v2.DistributeEvent drE = new com.mir.ems.profile.emap.v2.DistributeEvent();

					drE.setDestEMA(emaProfile.getEmaID());
					drE.setEvent(event.getEventParams());
					drE.setRequestID(emaProfile.getRequestID());
					drE.setResponse(er.toString());
					drE.setService("DistributeEvent");
					drE.setSrcEMA(global.SYSTEM_ID);
					drE.setTime(new Date(System.currentTimeMillis()).toString());
					drE.setResponseRequired("Always");

					String payload = drE.toString();

					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "NORESPONSE";

			}

			else if (version.equals("OpenADR2.0b")) {

				// Threshold 분배 알고리즘 시작, Thread 아님, 반드시 이게 끝나고 분배해줘야 하기 때문
				new Greedy().start();

				try {
					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile = new Emap_Cema_Profile();

					emaProfile.setEmaID(jsonObj.getString("VENID")).setRequestID(jsonObj.getString("REQUESTID"));

					Intervals interval = new Intervals();
					interval.addIntervalsParams(global.duration, "uid", 1234.0);

					com.mir.ems.profile.openadr.recent.Event event = new com.mir.ems.profile.openadr.recent.Event();
					// Threshold 정보 전달

					event.addEventParams("eventID",
							new com.mir.ems.profile.openadr.recent.EventSignals()
									.addEventSignalsParams(interval.getIntervalsParams(), "signalName", "Control Event",
											"signalID", 0)
									.getEventSignalsParams(),
							1, "modificationReason", -1, "mirLab", new Date(System.currentTimeMillis()).toString(),
							"eventStatus", false, "SessionSetup", "properties", "components", emaProfile.getEmaID(),
							new Date(System.currentTimeMillis()).toString(), "PT1H", "tolerance", "notification",
							"rampUp", "recovery");

					com.mir.ems.profile.openadr.recent.EventResponse er = new com.mir.ems.profile.openadr.recent.EventResponse();
					er.setRequestID(emaProfile.getRequestID());
					er.setResponseCode(200);
					er.setResponseDescription("OK");

					com.mir.ems.profile.openadr.recent.DistributeEvent drE = new com.mir.ems.profile.openadr.recent.DistributeEvent();

					drE.setSrcEMA(global.SYSTEM_ID);
					drE.setEvent(event.getEventParams());
					drE.setRequestID(emaProfile.getRequestID());
					drE.setResponse(er.toString());
					drE.setService("oadrDistributeEvent");
					drE.setResponseRequired("Always");

					String payload = drE.toString();
					this.exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

					return "NORESPONSE";

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "NORESPONSE";

			}

			else {
				try {

					emaProfile = new Emap_Cema_Profile();
					jsonObj = new JSONObject(requestText.toUpperCase());

					emaProfile.setEmaID(jsonObj.getString("SRCEMA")).setRequestID(jsonObj.getString("REQUESTID"));

					drmsg.put("SrcEMA", global.SYSTEM_ID);
					drmsg.put("DestEMA", emaProfile.getEmaID());
					drmsg.put("requestID", emaProfile.getRequestID());
					drmsg.put("responseCode", 200);
					drmsg.put("responseDescription", "OK");
					drmsg.put("time", new Date(System.currentTimeMillis()));

					Calendar calendar = Calendar.getInstance();
					String startYMD = "" + calendar.get(Calendar.YEAR) + "" + (calendar.get(Calendar.MONTH) + 1) + ""
							+ calendar.get(Calendar.DATE);
					String startTime = "" + calendar.get(Calendar.HOUR_OF_DAY) + "" + calendar.get(Calendar.MINUTE) + ""
							+ calendar.get(Calendar.SECOND);

					JSONArray eventInfo = new JSONArray();
					JSONObject childFormat = new JSONObject();
					childFormat.put("testEvent", false);
					childFormat.put("marketContext", 1);
					childFormat.put("eventStatus", "Registration");
					childFormat.put("priority", 0);
					childFormat.put("eventID", 0);
					childFormat.put("startYMD", Integer.parseInt(startYMD));
					childFormat.put("startTime", Integer.parseInt(startTime));
					childFormat.put("endYMD", Integer.parseInt(startYMD));
					childFormat.put("endTime", Integer.parseInt(startTime));
					childFormat.put("duration", 1000);
					childFormat.put("uid", 0);
					double currentValue = global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getPower();
					double threshold = global.emaProtocolCoAP.get(jsonObj.getString("SRCEMA")).getMargin();
					childFormat.put("currentValue", currentValue);
					childFormat.put("signalName", "Initial");
					childFormat.put("signalType", "DistributeEvent");
					childFormat.put("signalID", 1);
					childFormat.put("threshold", threshold);
					childFormat.put("capacity", threshold - currentValue);
					eventInfo.put(childFormat);
					drmsg.put("EMADREventInformation", eventInfo);

					eventInfo = new JSONArray();
					childFormat = new JSONObject();
					childFormat.put("price", 1000);
					childFormat.put("unit", "KW");
					eventInfo.put(childFormat);
					drmsg.put("EMADRPriceInformation", eventInfo);

					drmsg.put("service", "DistributeEvent");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return drmsg.toString();
		}

	}

	public static String ISO8601(int startYMD, int startTime, int endYMD, int endTime) {

		int calTime = endTime - startTime;

		if (calTime > 0) {

			if (calTime < 60) {

				return "PT" + calTime + "S";

			} else if (calTime > 60 && calTime < 6000) {

				int stemp = calTime % 100;
				int mTemp = ((calTime - stemp) % 10000) / 100;

				if (stemp >= 60) {
					stemp -= 60;
					mTemp += 1;
				}

				return stemp == 0 ? "PT" + mTemp + "M" : "PT" + mTemp + "M" + stemp + "S";

			} else {

				int stemp = calTime % 100;
				int mTemp = ((calTime - stemp) % 10000) / 100;
				int hTemp = (calTime) / 10000;

				if (stemp >= 60) {
					stemp -= 60;
					mTemp += 1;
				}

				if (mTemp >= 60) {
					mTemp -= 60;
					hTemp += 1;
				}

				if (stemp == 0 && mTemp != 0) {

					return "PT" + hTemp + "H" + mTemp + "M";

				}
				if (mTemp == 0 && stemp != 0) {

					return "PT" + hTemp + "H" + stemp + "S";
				}
				if (mTemp == 0 && stemp == 0) {

					return "PT" + hTemp + "H";
				}

				return "PT" + hTemp + "H" + mTemp + "M" + stemp + "S";

			}
		}

		return "WRONG";

	}

}
