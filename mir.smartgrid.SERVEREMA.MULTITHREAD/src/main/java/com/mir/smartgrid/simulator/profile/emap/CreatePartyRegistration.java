package com.mir.smartgrid.simulator.profile.emap;

public class CreatePartyRegistration {
	private String srcEMA;
	private String destEMA;
	
	private int version;
	private int requestID;
	private String transportName;
	private String transportAddress;
	private int reportOnly;
	private int httpPullModel;
	private String profileName;
	private int xmlSignature;
	private String registrationID;
	
	private String service;
	private String time;
	
	public CreatePartyRegistration() {
		super();
		this.srcEMA = null;
		this.destEMA = null;
		this.version = 0;
		this.requestID = 0;
		this.transportName = null;
		this.transportAddress = null;
		this.reportOnly = 0;
		this.httpPullModel = 0;
		this.profileName = null;
		this.xmlSignature = 0;
		this.registrationID = null;
		this.service = null;
		this.time = null;
	}

	public CreatePartyRegistration(String srcEMA, String destEMA, int version, int requestID, String transportName,
			String transportAddress, int reportOnly, int httpPullModel, String profileName, int xmlSignature,
			String registrationID, String service, String time) {
		super();
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.version = version;
		this.requestID = requestID;
		this.transportName = transportName;
		this.transportAddress = transportAddress;
		this.reportOnly = reportOnly;
		this.httpPullModel = httpPullModel;
		this.profileName = profileName;
		this.xmlSignature = xmlSignature;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public String getTransportName() {
		return transportName;
	}

	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}

	public String getTransportAddress() {
		return transportAddress;
	}

	public void setTransportAddress(String transportAddress) {
		this.transportAddress = transportAddress;
	}

	public int getReportOnly() {
		return reportOnly;
	}

	public void setReportOnly(int reportOnly) {
		this.reportOnly = reportOnly;
	}

	public int getHttpPullModel() {
		return httpPullModel;
	}

	public void setHttpPullModel(int httpPullModel) {
		this.httpPullModel = httpPullModel;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public int getXmlSignature() {
		return xmlSignature;
	}

	public void setXmlSignature(int xmlSignature) {
		this.xmlSignature = xmlSignature;
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
				
				+ "\"version\":" + version + ","
				+ "\"requestID\":" + requestID + ","
				+ "\"transportName\":\"" + transportName + "\","
				+ "\"transportAddress\":\"" + transportAddress + "\","
				+ "\"reportOnly\":" + reportOnly + ","
				+ "\"httpPullModel\":" + httpPullModel + ","
				+ "\"profileName\":\"" + profileName + "\","
				+ "\"xmlSignature\":" + xmlSignature + ","
				+ "\"registrationID\":\"" + registrationID + "\","
				
				+ "\"service\":\"" + service + "\","
				+ "\"time\":\"" + time + "\""
				+ "}";
	}
	
	
	

}
