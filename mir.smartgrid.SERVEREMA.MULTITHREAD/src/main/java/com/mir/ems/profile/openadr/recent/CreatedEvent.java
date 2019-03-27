package com.mir.ems.profile.openadr.recent;

import org.json.JSONException;
import org.json.JSONObject;

public class CreatedEvent {

	private String venID, vtnID, responseDescription, optType, eventID, requestID, service, time;
	private int responseCode, modificationNumber;
	

	@Override
	public String toString() {
		JSONObject json = new JSONObject();

		try {

			json.put("venID", getVenID());
			json.put("vtnID", getVtnID());
			json.put("responseCode", getResponseCode());
			json.put("responseDescription", getResponseDescription());
			json.put("optType", getOptType());
			json.put("eventID", getEventID());
			json.put("modificationNumber", getModificationNumber());
			json.put("requestID", getRequestID());
			json.put("service", getService());
//			json.put("time", getTime());
			
			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "wrong";
		}
	}
	
	
	public String getVenID() {
		return venID;
	}


	public void setVenID(String venID) {
		this.venID = venID;
	}


	public String getVtnID() {
		return vtnID;
	}


	public void setVtnID(String vtnID) {
		this.vtnID = vtnID;
	}

	public String getResponseDescription() {
		return responseDescription;
	}
	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}
	public String getOptType() {
		return optType;
	}
	public void setOptType(String optType) {
		this.optType = optType;
	}
	public String getEventID() {
		return eventID;
	}
	public void setEventID(String eventID) {
		this.eventID = eventID;
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
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	public int getModificationNumber() {
		return modificationNumber;
	}
	public void setModificationNumber(int modificationNumber) {
		this.modificationNumber = modificationNumber;
	}
	
	
}
