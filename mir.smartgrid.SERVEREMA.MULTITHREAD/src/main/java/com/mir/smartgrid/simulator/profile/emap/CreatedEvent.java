package com.mir.smartgrid.simulator.profile.emap;

public class CreatedEvent {
	private String srcEMA;
	private String destEMA;
	
	private int requestID;
	private int responseCode;
	private String responseDescription;
	private String optType;
	private String eventID;
	private int modificationNumber;

	private String service;
	private String time;

	public CreatedEvent() {
	}

	public CreatedEvent(String srcEMA, String destEMA, int requestID, int responseCode, String responseDescription,
			String optType, String eventID, int modificationNumber, String time) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
		this.optType = optType;
		this.eventID = eventID;
		this.modificationNumber = modificationNumber;
		this.time = time;
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

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
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

	public int getModificationNumber() {
		return modificationNumber;
	}

	public void setModificationNumber(int modificationNumber) {
		this.modificationNumber = modificationNumber;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	@Override
	public String toString() {
		return "{\"SrcEMA\":\"" + srcEMA + "\","
				+ "\"DestEMA\":\"" + destEMA + "\","
				
				+ "\"requestID\":" + requestID + ","
				+ "\"version\":" + responseCode + ","
				+ "\"customerPriority\":\"" + responseDescription + "\","
				+ "\"QoS\":\"" + optType + "\","
				+ "\"service\":\"" + eventID + "\","
				+ "\"type\":" + modificationNumber + ","
				
				+ "\"service\":\"" + service + "\""
				+ "\"time\":\"" + time + "\""
				+ "}";
	}
	
	

}
