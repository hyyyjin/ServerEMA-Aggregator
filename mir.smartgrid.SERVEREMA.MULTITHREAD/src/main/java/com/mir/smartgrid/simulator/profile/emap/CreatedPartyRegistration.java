package com.mir.smartgrid.simulator.profile.emap;

public class CreatedPartyRegistration {
	private String srcEMA;
	private String destEMA;
	
	private int requestID;
	private int version;
	private String transportName;
	private String registrationID;
	private int duration;
	private int responseCode;
	private String profileName;
	private String responseDescription;
	
	private String service;
	private String time;
	
	public CreatedPartyRegistration() {
		this.srcEMA = null;
		this.destEMA = null;
		this.requestID = 0;
		this.version = 0;
		this.transportName = null;
		this.registrationID = null;
		this.duration = 0;
		this.responseCode = 0;
		this.profileName = null;
		this.responseDescription = null;
		this.service = null;
		this.time = null;
	}

	public CreatedPartyRegistration(String srcEMA, String destEMA, int requestID, int version, String transportName,
			String registrationID, int duration, int responseCode, String profileName, String responseDescription,
			String service, String time) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
		this.version = version;
		this.transportName = transportName;
		this.registrationID = registrationID;
		this.duration = duration;
		this.responseCode = responseCode;
		this.profileName = profileName;
		this.responseDescription = responseDescription;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getTransportName() {
		return transportName;
	}

	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}

	public String getRegistrationID() {
		return registrationID;
	}

	public void setRegistrationID(String registrationID) {
		this.registrationID = registrationID;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
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
				+ "\"version\":" + version + ","
				+ "\"transportName\":\"" + transportName + "\","
				+ "\"registrationID\":\"" + registrationID + "\","
				+ "\"duration\":" + duration + ","
				+ "\"responseCode\":" + responseCode + ","
				+ "\"profileName\":\"" + profileName + "\","
				+ "\"responseDescription\":\"" + responseDescription + "\","
								
				+ "\"service\":\"" + service + "\","
				+ "\"time\":\"" + time + "\""
				+ "}";
	}
	
	
}
