package com.mir.smartgrid.simulator.profile.emap;

public class CancelPartyRegistration {
	private String srcEMA;
	private String destEMA;
	
	private int requestID;
	private String registrationID;
	
	private String service;
	private String time;

	public CancelPartyRegistration() {
		
	}

	public CancelPartyRegistration(String srcEMA, String destEMA, int requestID, String registrationID, String service,
			String time) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
		this.registrationID = registrationID;
		this.service = service;
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

	public String getRegistrationID() {
		return registrationID;
	}

	public void setRegistrationID(String registrationID) {
		this.registrationID = registrationID;
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

	@Override
	public String toString() {
		return "{\"SrcEMA\":\"" + srcEMA + "\","
				+ "\"DestEMA\":\"" + destEMA + "\","
				
				+ "\"requestID\":" + requestID + ","
				+ "\"registrationID\":\"" + registrationID + "\","
				
				+ "\"service\":\"" + service + "\","
				+ "\"time\":\"" + time + "\""
				+ "}";
	}
	
	
}
