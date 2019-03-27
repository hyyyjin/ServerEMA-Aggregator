package com.mir.smartgrid.simulator.profile.emap;

public class CreatedOpt {
	private String srcEMA;
	private String destEMA;
	private String optID; 
	
	private int requestID;
	private int responseCode;
	private String responseDescription;
	
	private String service;
	private String optStatus;	// Accept, Reject

	public CreatedOpt() {
		
	}

	public CreatedOpt(String srcEMA, String destEMA, String optID, int requestID, int responseCode,
			String responseDescription, String service, String optStatus) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.optID = optID;
		this.requestID = requestID;
		this.responseCode = responseCode;
		this.responseDescription = responseDescription;
		this.service = service;
		this.optStatus = optStatus;
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

	public String getOptID() {
		return optID;
	}

	public void setOptID(String optID) {
		this.optID = optID;
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

	public String getOptStatus() {
		return optStatus;
	}

	public void setOptStatus(String optStatus) {
		this.optStatus = optStatus;
	}

	@Override
	public String toString() {
		return "{\"SrcEMA\":\"" + srcEMA + "\","
				+ "\"DestEMA\":\"" + destEMA + "\","
				+ "\"optID\":\"" + optID + "\","
				
				+ "\"requestID\":" + requestID + ","
				+ "\"responseCode\":" + responseCode + ","
				+ "\"responseDescription\":\"" + responseDescription + "\","
				
				+ "\"service\":\"" + service + "\","
				+ "\"optStatus\":\"" + optStatus + "\""
				+ "}";
	}
	
	
}
