package com.mir.ems.profile.emap.v2;

import org.json.JSONException;
import org.json.JSONObject;

public class DistributeEvent {

	
	private String srcEMA, destEMA, requestID, service, response, event, time, responseRequired;

	@Override
	public String toString() {
		
		
		
		return "{\"SrcEMA" + "\":" + "\"" + getSrcEMA() + "\"" + ", "
				+ "\"DestEMA" + "\":" + "\"" + getDestEMA() + "\"" + ", "
				+"\"requestID" + "\":" + "\"" + getRequestID() + "\"" + ", "
				+"\"response" + "\":" + getResponse() + ", "
				+"\"event" + "\":" + getEvent() + ", "
				+"\"service" + "\":" + "\"" + getService() + "\"" + ", "
				+"\"time" + "\":" + "\"" + getTime() + "\"" + ", "
				+"\"responseRequired" + "\":" + "\"" + getResponseRequired()+ "\"" + "}";
		
		
		
	}
	
	public String getResponseRequired() {
		return responseRequired;
	}

	public void setResponseRequired(String responseRequired) {
		this.responseRequired = responseRequired;
	}

	public String getSrcEMA() {
		return srcEMA;
	}

	public void setSrcEMA(String srcEMA) {
		this.srcEMA = srcEMA;
	}

	public String getDestEMA() {
		return destEMA;
	}

	public void setDestEMA(String destEMA) {
		this.destEMA = destEMA;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	
	
}
