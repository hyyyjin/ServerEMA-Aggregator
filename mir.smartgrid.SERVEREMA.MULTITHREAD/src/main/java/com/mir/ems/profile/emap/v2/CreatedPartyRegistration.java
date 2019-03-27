package com.mir.ems.profile.emap.v2;

import org.json.JSONObject;

public class CreatedPartyRegistration {

	private String srcEMA, destEMA, responseDescription, requestID, duration, service, version, time, registrationID;
	private int responseCode;
	private String profile;

	public CreatedPartyRegistration() {

	}

	public CreatedPartyRegistration(String srcEMA, String destEMA, int responseCode, String responseDescription,
			String requestID, String registrationID, String profile, String duration, String service, String version,
			String time) {

	}

	public String getRegistrationID() {
		return registrationID;
	}

	public void setRegistrationID(String registrationID) {
		this.registrationID = registrationID;
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

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	@Override
	public String toString() {


		return "{\"SrcEMA" + "\":" + "\"" + getSrcEMA() + "\"" + ", "
		+ "\"DestEMA" + "\":" + "\"" + getDestEMA() + "\"" + ", "
		+"\"responseCode" + "\":" + "\"" + getResponseCode() + "\"" + ", "
		+"\"responseDescription" + "\":" + "\"" + getResponseDescription() + "\"" + ", "
		+"\"requestID" + "\":" + "\"" + getRequestID() + "\"" + ", "
		+"\"duration" + "\":" + "\"" + getDuration() + "\"" + ", "
		+"\"service" + "\":" + "\"" + getService() + "\"" + ", "
		+"\"version" + "\":" + "\"" + getVersion() + "\"" + ", "
		+"\"time" + "\":" + "\"" + getTime() + "\"" + ", "
		+"\"registrationID" + "\":" + "\"" + getRegistrationID() + "\"" + ", "
		+ "\"profile" + "\": ["+ getProfile() + "]}";
		


	}

}
