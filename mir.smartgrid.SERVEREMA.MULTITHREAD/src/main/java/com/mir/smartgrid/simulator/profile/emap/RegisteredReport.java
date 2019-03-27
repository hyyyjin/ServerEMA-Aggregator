package com.mir.smartgrid.simulator.profile.emap;

public class RegisteredReport {
	private String srcEMA;
	private String destEMA;
	
	private String transportName;
	private int requestID;
	private int responseCode;
	private int version;
	private String responseDescription;
	private double threshold;
	
	private String service;
	private String time;
	
	public RegisteredReport() {
		this.srcEMA = null;
		this.destEMA = null;
		this.transportName = null;
		this.requestID = 0;
		this.responseCode = 0;
		this.version = 0;
		this.responseDescription = null;
		this.threshold = 0;
		this.time = null;
	}

	public RegisteredReport(String srcEMA, String destEMA, String transportName, int requestID, int responseCode,
			int version, String responseDescription, double threshold, String service, String time) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.transportName = transportName;
		this.requestID = requestID;
		this.responseCode = responseCode;
		this.version = version;
		this.responseDescription = responseDescription;
		this.threshold = threshold;
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

	public String getTransportName() {
		return transportName;
	}

	public void setTransportName(String transportName) {
		this.transportName = transportName;
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

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
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
				
				+ "\"transportName\":\"" + transportName + "\","
				+ "\"requestID\":" + requestID + ","
				+ "\"responseCode\":" + responseCode + ","
				+ "\"version\":" + version + ","
				+ "\"responseDescription\":\"" + responseDescription + "\","
				+ "\"threshold\":" + threshold + ","
				
				+ "\"service\":\"" + service + "\","
				+ "\"time\":\"" + time + "\""
				+ "}";
	}

	
}
