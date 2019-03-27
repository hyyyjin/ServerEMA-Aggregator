package com.mir.smartgrid.simulator.profile.emap;

public class CancelOpt {
	private String optID; 
	private String srcEMA;
	private String destEMA;
	private int requestID;
	private String service;
	private String time;
	
	public CancelOpt() {
		
	}

	public CancelOpt(String optID, String srcEMA, String destEMA, int requestID, String time) {
		this.optID = optID;
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
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
		return "{\"optID\":\"" + optID + "\","
				+ "\"SrcEMA\":\"" + srcEMA + "\","
				+ "\"DestEMA\":\"" + destEMA + "\","
				
				+ "\"requestID\":" + requestID + ","
				
				+ "\"service\":\"" + service + "\""
				+ "\"time\":\"" + time + "\""
				+ "}";
	}

	

}
