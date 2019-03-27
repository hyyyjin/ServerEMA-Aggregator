package com.mir.smartgrid.simulator.profile.emap;

public class Response {
	private String srcEMA;
	private String destEMA;
	
	private int requestID;
	private int version;
	private int responseCode;
	private String responseDescription;
	
	private String type;
	private double threshold;
	private String service;	/* Registration, Periodic,
							   report, Event, Price */
	private String time;
	
	public Response() {

	}

	public Response(String srcEMA, String destEMA, int requestID, int version, int responseCode,
			String responseDescription,String type, String service, String time) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
		this.version = version;
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
		this.type = type;
		this.service = service;
		this.time = time;
	}

	public Response(String srcEMA, String destEMA, int requestID, int version, int responseCode,
			String responseDescription, String type, double threshold, String service, String time) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
		this.version = version;
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
		this.type = type;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	@Override
	public String toString() {
		return "{\"SrcEMA\":\"" + srcEMA + "\","
				+ "\"DestEMA\":\"" + destEMA + "\","

				+ "\"requestID\":" + requestID + ","
				+ "\"responseCode\":" + responseCode + ","
				+ "\"version\":" + version + ","
				+ "\"responseDescription\":\"" + responseDescription + "\","
				
				+ "\"type\":\"" + type + "\","
				+ "\"service\":\"" + service + "\","
				+ "\"time\":\"" + time + "\""
				+ "}";
	}
	
	public String toStringWithThreshold() {
		return "{\"SrcEMA\":\"" + srcEMA + "\","
				+ "\"DestEMA\":\"" + destEMA + "\","

				+ "\"requestID\":" + requestID + ","
				+ "\"responseCode\":" + responseCode + ","
				+ "\"version\":" + version + ","
				+ "\"responseDescription\":\"" + responseDescription + "\","
				
				+ "\"type\":\"" + type + "\","
				+ "\"threshold\":" + threshold + ","
				+ "\"service\":\"" + service + "\","
				+ "\"time\":\"" + time + "\""
				+ "}";
	}
}
