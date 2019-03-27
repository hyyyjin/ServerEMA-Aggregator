package com.mir.smartgrid.simulator.profile.emap;


public class CanceledOpt {
	private String optID; 
	private String srcEMA;
	private String destEMA;
	
	private int requestID;
	private int responseCode;
	private String responseDescription;
	
	private String service;
	private String time;

	public CanceledOpt() {
		
	}

	public CanceledOpt(String optID, String srcEMA, String destEMA, int requestID, int responseCode,
			String responseDescription, String service, String time) {
		this.optID = optID;
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
		this.service = service;
		this.time = time;
	}

	public String getOptID() {
		return optID;
	}

	public void setOptID(String optID) {
		this.optID = optID;
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
		return "{\"optID\":\"" + optID + "\","
				+ "\"SrcEMA\":\"" + srcEMA + "\","
				+ "\"DestEMA\":\"" + destEMA + "\","
				
				+ "\"requestID\":" + requestID + ","
				+ "\"responseCode\":" + responseCode + ","
				+ "\"responseDescription\":\"" + responseDescription + "\","
				
				+ "\"service\":\"" + service + "\","
				+ "\"time\":\"" + time + "\""
				+ "}";
	}
	
	
}
