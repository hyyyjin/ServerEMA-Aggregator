package com.mir.ems.profile.openadr.recent;

import org.json.JSONException;
import org.json.JSONObject;

public class DistributeEvent {

	
	private String srcEMA, requestID, service, response, event, responseRequired;

	@Override
	public String toString() {
		
		
		
		return "{\"vtnID" + "\":" + "\"" + getSrcEMA() + "\"" + ", "
//				+ "\"DestEMA" + "\":" + "\"" + getDestEMA() + "\"" + ", "
				+"\"requestID" + "\":" + "\"" + getRequestID() + "\"" + ", "
				+"\"response" + "\":" + getResponse() + ", "
				+"\"event" + "\":" + getEvent() + ", "
				+"\"service" + "\":" + "\"" + getService() + "\"" + ", "
//				+"\"time" + "\":" + "\"" + getTime() + "\"" + ", "
				+"\"oadrResponseRequired" + "\":" + "\"" + getResponseRequired()+ "\"" + "}";
		
		
		
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

	
}
