package com.mir.ems.mqtt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttClient;

import com.mir.ems.globalVar.global;
import com.mir.ems.mqtt.emap.DemandResponseEvent;
import com.mir.ems.profile.emap.v2.Event;
import com.mir.ems.profile.emap.v2.EventResponse;
import com.mir.ems.profile.emap.v2.EventSignals;
import com.mir.ems.profile.emap.v2.Intervals;

public class EventResponder implements PushEventListener {

	public MqttClient mqttClient;
	public String version;
	public String emaID;
	
	public EventResponder(MqttClient mqttClient, String version, String emaID) {
		this.mqttClient = mqttClient;
		this.version = version;
		this.emaID = emaID;
	}

	public String getEmaID() {
		return emaID;
	}



	public void setEmaID(String emaID) {
		this.emaID = emaID;
	}



	@Override
	public void eventNotification(String emaID, int type, int strYMD, int strTime, int endYMD, int endTime,
			double threshold) {
		// TODO Auto-generated method stub

		if (version.equals("EMAP1.0b") && this.emaID.equals(emaID)) {

			System.out.println("저기");

			
			String strTime_str = strTime + "";
			String endTime_str = endTime + "";

			strTime_str = strTime_str.length() < 6 ? "0" + strTime_str : strTime_str;
			endTime_str = endTime_str.length() < 6 ? "0" + endTime_str : endTime_str;

			String eventDuration = DemandResponseEvent.ISO8601(strYMD, strTime, endYMD, endTime);
			String timeForm = (strYMD + "") + strTime_str;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

			try {

				Date createdDateTime = sdf.parse(timeForm);
				Intervals interval = new Intervals();
				interval.addIntervalsParams(global.duration, "uid", 0.0);

				EventSignals es = new EventSignals();
				es.addEventSignalsParams(interval.getIntervalsParams(), "Distribute", "Event", "signalID", 0, threshold,
						0, 0, "KW");

				Event event = new Event();
				event.addEventParams("eventID", es.getEventSignalsParams(), 1, "None", 2, "marketContext",
						createdDateTime.toString(), "Event", false, "Event", "None", "None", "None",
						new Date(System.currentTimeMillis()).toString(), eventDuration, "None", "None", "None", "None");

				EventResponse er = new EventResponse();
				er.setRequestID("REQUESTID");
				er.setResponseCode(200);
				er.setResponseDescription("OK");

				com.mir.ems.profile.emap.v2.DistributeEvent drE = new com.mir.ems.profile.emap.v2.DistributeEvent();

				drE.setDestEMA(emaID);
				drE.setEvent(event.getEventParams());
				drE.setRequestID("REQUESTID");
				drE.setResponse(er.toString());
				drE.setService("DistributeEvent");
				drE.setSrcEMA(global.SYSTEM_ID);
				drE.setTime(new Date(System.currentTimeMillis()).toString());
				drE.setResponseRequired("Always");

				// String topic = "/EMAP/CEMA/1.0b/Event";

				String topic = "/EMAP/" + emaID.toUpperCase() + "/1.0b/Event";

				String payload = drE.toString();
				new Publishing().publishThread(this.mqttClient, topic, global.qos, payload.getBytes());

			} catch (Exception e) {

				

				
			}
		}else if(version.equals("OpenADR2.0b") && this.emaID.equals(emaID)){
			
			
			System.out.println("여기");
			
			String strTime_str = strTime + "";
			String endTime_str = endTime + "";

			strTime_str = strTime_str.length() < 6 ? "0" + strTime_str : strTime_str;
			endTime_str = endTime_str.length() < 6 ? "0" + endTime_str : endTime_str;

			String eventDuration = DemandResponseEvent.ISO8601(strYMD, strTime, endYMD, endTime);
			String timeForm = (strYMD + "") + strTime_str;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			
			try {

				Date createdDateTime = sdf.parse(timeForm);
				
				Intervals interval = new Intervals();
				interval.addIntervalsParams(global.duration, "uid", threshold);
				
				com.mir.ems.profile.openadr.recent.Event event = new com.mir.ems.profile.openadr.recent.Event();
				// Threshold 정보 전달

				event.addEventParams("eventID",
						new com.mir.ems.profile.openadr.recent.EventSignals().addEventSignalsParams(interval.getIntervalsParams(), "signalName",
								"Control Event", "signalID", 0).getEventSignalsParams(),
						1, "modificationReason", -1, "mirLab", createdDateTime.toString(),
						"eventStatus", false, "Event", "properties", "components", emaID,
						new Date(System.currentTimeMillis()).toString(), eventDuration, "tolerance", "notification", "rampUp",
						"recovery");

				com.mir.ems.profile.openadr.recent.EventResponse er = new com.mir.ems.profile.openadr.recent.EventResponse();
				er.setRequestID("REQUESTID");
				er.setResponseCode(200);
				er.setResponseDescription("OK");

				com.mir.ems.profile.openadr.recent.DistributeEvent drE = new com.mir.ems.profile.openadr.recent.DistributeEvent();

				drE.setSrcEMA(global.SYSTEM_ID);
				drE.setEvent(event.getEventParams());
				drE.setRequestID("REQUESTID");
				drE.setResponse(er.toString());
				drE.setService("oadrDistributeEvent");
				drE.setResponseRequired("Always");

				String topic = "/OpenADR/" + emaID.toUpperCase() + "/2.0b/EiEvent";
				
				String payload = drE.toString();
				new Publishing().publishThread(this.mqttClient, topic, global.qos, payload.getBytes());

				global.emaProtocolCoAP_EventFlag.get(emaID).setEventFlag(false);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}

}
