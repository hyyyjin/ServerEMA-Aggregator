package com.mir.ems.coap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.mir.ems.database.item.EMAP_CoAP_EMA_DR;
import com.mir.ems.database.item.Emap_Cema_Profile;
import com.mir.ems.globalVar.global;
import com.mir.ems.profile.emap.v2.Event;
import com.mir.ems.profile.emap.v2.EventResponse;
import com.mir.ems.profile.emap.v2.EventSignals;
import com.mir.ems.profile.emap.v2.Intervals;
import com.mir.ems.topTab.DRScheduling;

public class CoAPObserverSubPath extends CoapResource {

	// private boolean initialFlag = false;
	// int gwNum;
	private String name;
	private String parentPath;

	public CoAPObserverSubPath(String name, String parentPath) {
		super(name);
		this.name = name;
		setObservable(true);
		setObserveType(Type.NON);
		getAttributes().setObservable();

		setParentPath(parentPath);

		Timer timer = new Timer();
		// timer.schedule(new UpdateTask(), 0, 100);
	}

	private class UpdateTask extends TimerTask {
		public void run() {

			if (global.getObs_emaProtocolCoAP_EventFlag().containsKey(name)) {
				if (global.getObs_emaProtocolCoAP_EventFlag().get(name).isEventFlag()) {
					changed();
					try {
						// Thread.sleep(80);
						DRScheduling.obsEventFlag = false;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		}
	}

	public void handleGET(CoapExchange exchange) {

		Response response = new Response(ResponseCode.CONTENT);

		if (global.getObs_emaProtocolCoAP_EventFlag().containsKey(name)
				&& !global.obs_emaProtocolCoAP_EventFlag.get(name).isEventFlag()) {
			
			clearObserveRelations();
			global.observeManager.replace(name, this);
			response.setPayload("Heartbeat_Response");
			exchange.respond(response);
		}

		else if (!global.getObs_emaProtocolCoAP_EventFlag().containsKey(name)) {

			global.obs_emaProtocolCoAP_EventFlag.put(name, new EMAP_CoAP_EMA_DR());
			global.observeManager.put(name, this);
			response.setPayload("Initial_Success");
			exchange.respond(response);

		} else if (global.obs_emaProtocolCoAP_EventFlag.get(name).isEventFlag()) {

			if (getParentPath().contains("OpenADR")) {

				new Thread(new Runnable() {
					public void run() {
						global.obs_emaProtocolCoAP_EventFlag.replace(name, new EMAP_CoAP_EMA_DR().setEventFlag(false));
					}
				}).start();

				Emap_Cema_Profile emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(name);

				double threshold = global.obs_emaProtocolCoAP_EventFlag.get(name).getThreshold();
				int strTime = global.obs_emaProtocolCoAP_EventFlag.get(name).getStartTime();
				int strYMD = global.obs_emaProtocolCoAP_EventFlag.get(name).getStartYMD();
				int endTime = global.obs_emaProtocolCoAP_EventFlag.get(name).getEndTime();
				int endYMD = global.obs_emaProtocolCoAP_EventFlag.get(name).getEndYMD();

				String strTime_str = strTime + "";
				String endTime_str = endTime + "";

				strTime_str = strTime_str.length() < 6 ? "0" + strTime_str : strTime_str;
				endTime_str = endTime_str.length() < 6 ? "0" + endTime_str : endTime_str;

				String eventDuration = ISO8601(strYMD, strTime, endYMD, endTime);
				String timeForm = (strYMD + "") + strTime_str;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

				try {

//					Date createdDateTime = sdf.parse(timeForm);

					Intervals interval = new Intervals();
					interval.addIntervalsParams(global.duration, "uid", threshold);

					com.mir.ems.profile.openadr.recent.Event event = new com.mir.ems.profile.openadr.recent.Event();
					// Threshold 정보 전달

					event.addEventParams("eventID",
							new com.mir.ems.profile.openadr.recent.EventSignals()
									.addEventSignalsParams(interval.getIntervalsParams(), "signalName", "Control Event",
											"signalID", 0)
									.getEventSignalsParams(),
							1, "modificationReason", -1, "mirLab", new Date(System.currentTimeMillis()).toString(), "eventStatus", false,
							"Event", "properties", "components", emaProfile.getEmaID(),
							new Date(System.currentTimeMillis()).toString(), eventDuration, "tolerance", "notification",
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
					exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else if (getParentPath().contains("EMAP")) {

				new Thread(new Runnable() {
					public void run() {
						global.obs_emaProtocolCoAP_EventFlag.replace(name, new EMAP_CoAP_EMA_DR().setEventFlag(false));
					}
				}).start();

				double threshold = global.obs_emaProtocolCoAP_EventFlag.get(name).getThreshold();
				int strTime = global.obs_emaProtocolCoAP_EventFlag.get(name).getStartTime();
				int strYMD = global.obs_emaProtocolCoAP_EventFlag.get(name).getStartYMD();
				int endTime = global.obs_emaProtocolCoAP_EventFlag.get(name).getEndTime();
				int endYMD = global.obs_emaProtocolCoAP_EventFlag.get(name).getEndYMD();

				String strTime_str = strTime + "";
				String endTime_str = endTime + "";

				strTime_str = strTime_str.length() < 6 ? "0" + strTime_str : strTime_str;
				endTime_str = endTime_str.length() < 6 ? "0" + endTime_str : endTime_str;

				String eventDuration = ISO8601(strYMD, strTime, endYMD, endTime);
				String timeForm = (strYMD + "") + strTime_str;

				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

				Emap_Cema_Profile emaProfile = new Emap_Cema_Profile();

				emaProfile.setEmaID(name);

				try {
					// Date createdDateTime = sdf.parse(timeForm);
					// Date createdDateTime = new
					// Date(System.currentTimeMillis());
					Intervals interval = new Intervals();
					interval.addIntervalsParams(global.duration, "uid", 0.0);

					EventSignals es = new EventSignals();
					es.addEventSignalsParams(interval.getIntervalsParams(), "Distribute", "Event", "signalID", 0,
							threshold, 0, 0, "KW");

					Event event = new Event();

					String eventID = global.obs_emaProtocolCoAP_EventFlag.get(name).getEventID();

					event.addEventParams(eventID, es.getEventSignalsParams(), 1, "None", 2, "marketContext",
							new Date(System.currentTimeMillis()).toString(), "Event", false, "Event", "None", "None",
							"None", new Date(System.currentTimeMillis()).toString(), eventDuration, "None", "None",
							"None", "None");

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

					// response.setPayload(payload.toString());
					// exchange.respond(response);

					exchange.respond(ResponseCode.CONTENT, payload, MediaTypeRegistry.APPLICATION_JSON);

//					if (global.autoDR) {
//						if (global.autoDRCNT < global.autoDRTOTAL) {
//							global.autoDRCNT += 1;
//							eventID = global.eventID.get(global.autoDRCNT);
//							global.obs_emaProtocolCoAP_EventFlag.get(name).setEventID(eventID);
//							System.out.println(global.autoDRCNT + "==>");
//						} else {
//							global.obs_emaProtocolCoAP_EventFlag.get(name).setEventFlag(false);
//						}
//					} else {
						global.obs_emaProtocolCoAP_EventFlag.get(name).setEventFlag(false);
//					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// JSONObject drmsg = new JSONObject();
				//
				// try {
				// // System.out.println("AA");
				// // drmsg.put("SrcEMA", global.SYSTEM_ID);
				// // drmsg.put("DestEMA", emaID);
				// // drmsg.put("requestID", 1);
				// // drmsg.put("responseCode", 200);
				// // drmsg.put("responseDescription", "OK");
				// // drmsg.put("time", new Date(System.currentTimeMillis()));
				// //
				// // JSONArray eventInfo = new JSONArray();
				// // JSONObject childFormat = new JSONObject();
				// // childFormat.put("profileName", global.profileName);
				// // childFormat.put("modificationNumber", 0);
				// //
				// // childFormat.put("testEvent", false);
				// // childFormat.put("marketContext", 1);
				// // childFormat.put("eventStatus", "DREVENT");
				// // childFormat.put("priority", 0);
				// // childFormat.put("eventID", 0);
				// // childFormat.put("startYMD",
				// //
				// global.getObs_emaProtocolCoAP_EventFlag().get(emaID).getStartYMD());
				// // childFormat.put("startTime",
				// //
				// global.getObs_emaProtocolCoAP_EventFlag().get(emaID).getStartTime());
				// // childFormat.put("endYMD",
				// //
				// global.getObs_emaProtocolCoAP_EventFlag().get(emaID).getEndYMD());
				// // childFormat.put("endTime",
				// //
				// global.getObs_emaProtocolCoAP_EventFlag().get(emaID).getEndTime());
				// // childFormat.put("duration", 1000);
				// // childFormat.put("uid", 0);
				// // double currentValue =
				// // global.emaProtocolCoAP.get(emaID).getPower();
				// // double threshold =
				// // global.emaProtocolCoAP.get(emaID).getMargin();
				// // childFormat.put("currentValue", currentValue);
				// // childFormat.put("signalName", "DREVENT");
				// // childFormat.put("signalType", "DistributeEvent");
				// // childFormat.put("signalID", 1);
				// // childFormat.put("threshold", threshold);
				// // childFormat.put("capacity", threshold - currentValue);
				// // eventInfo.put(childFormat);
				// // drmsg.put("EMADREventInformation", eventInfo);
				// //
				// // eventInfo = new JSONArray();
				// // childFormat = new JSONObject();
				// // childFormat.put("price", 1000);
				// // childFormat.put("unit", "KW");
				// // eventInfo.put(childFormat);
				// // drmsg.put("EMADRPriceInformation", eventInfo);
				// //
				// // drmsg.put("service", "DistributeEvent");
				// // drmsg.put("type", "Level");
				// // exchange.respond(ResponseCode.CHANGED, drmsg.toString(),
				// // MediaTypeRegistry.APPLICATION_JSON);
				//
				// // response.setPayload(drmsg.toString());
				// // // response.setConfirmable(false);
				// // response.setType(CoAP.Type.ACK);
				// // response.setPayload(drmsg.toString());
				// //// response.setOptions(OpionSet)
				// //
				//
				// drmsg.put("Service", "oadrDistributeEvent");
				// drmsg.put("StartYMD",
				// global.getObs_emaProtocolCoAP_EventFlag().get(emaID).getStartYMD());
				// drmsg.put("StartTime",
				// global.getObs_emaProtocolCoAP_EventFlag().get(emaID).getStartTime());
				// drmsg.put("EndYMD",
				// global.getObs_emaProtocolCoAP_EventFlag().get(emaID).getEndYMD());
				// drmsg.put("EndTime",
				// global.getObs_emaProtocolCoAP_EventFlag().get(emaID).getEndTime());
				// drmsg.put("Value", (int)
				// global.getObs_emaProtocolCoAP_EventFlag().get(emaID).getThreshold());
				// drmsg.put("Response", 100);
				// drmsg.put("RequestID", 1);
				// drmsg.put("EventID", 1);
				// drmsg.put("ModificationNumber", 0);
				// drmsg.put("TargetVEN", "MIR_VEN");
				// drmsg.put("Responsedescription", "MIR");
				// drmsg.put("OptType", "optIn");
				//
				// System.out.println(drmsg.toString());
				// response.setPayload(drmsg.toString());
				// exchange.respond(response);
				// // exchange.respond(response);
				// System.out.println("보냇니");
				// // exchange.
				// } catch (JSONException e) {
				// e.printStackTrace();
				// }
				// // System.out.println("여기니?");

				// //여기 수정했음
				// new Thread(new Runnable() {
				// public void run() {
				// global.obs_emaProtocolCoAP_EventFlag.replace(name, new
				// EMAP_CoAP_EMA_DR().setEventFlag(false));
				// }
				// }).start();

			}
		}

		// emaProtocolCoAP_EventFlag
		//
		//
		// String uri = getURI();
		// String[] uriParseArr = uri.split("/");
		// String uriParse = uriParseArr[2];
		// String[] uri_processParse = uriParse.split("s");
		// int gwNum = Integer.parseInt(uri_processParse[1]);
		//
		// System.out.println("����Ʈ���� �ѹ� �̴Ͻþ�" + gwNum);
		// DRScheduling drf = new DRScheduling();
		// Response response = new Response(ResponseCode.CONTENT);
		//
		// if (check && global.eventStatus != true) {
		// response.setPayload("Initial_Success");
		// exchange.respond(response);
		// }
		//
		// if (global.eventStatus == true && drf.obsFlag.equals("obs")) {
		// check = false;
		//
		// if (gwNum==drf.indexOfEma) {
		// global.eventStatus = false;
		//
		// System.out.println("����Ʈ���� �ѹ�" + gwNum);
		// System.out.println("�Ľ̼���" + drf.indexOfEma);
		// System.out.println("���ſ���");
		// String paylaod = DRScheduling.DR_MSG;
		// response.setPayload(paylaod);
		// exchange.respond(response);
		//
		// gwNum=0;
		// }
		// // MessageTracer abc = new MessageTracer();
		// // abc.receiveEmptyMessage();
		//
		// }

	}

	class UpdateJob implements Runnable {
		public UpdateJob() {

		}

		@Override
		public void run() {
			while (true) {
				if (DRScheduling.obsEventFlag) {
					changed();
				} else {

				}
			}
			// try {
			// wait();
			// } catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
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

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

}
