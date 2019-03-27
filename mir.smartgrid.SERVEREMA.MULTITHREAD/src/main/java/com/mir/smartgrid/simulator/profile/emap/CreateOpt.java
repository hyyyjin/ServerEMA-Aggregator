package com.mir.smartgrid.simulator.profile.emap;

public class CreateOpt {
	private String srcEMA;
	private String destEMA;
	private String optID;	
	
	private String optType;		// optIn, optOut
	private String optReason;	// economic, emergency, mustRun, participating, noParticipating
	private int requestID;
	private double requestPower;
	private int startYMD;
	private int startTime;
	
	private int endYMD;
	private int endTime;
	private int duration;
	private String service;
	private String createdDateTime;
	
	public CreateOpt() {
		
	}

	public CreateOpt(String srcEMA, String destEMA, String optID, String optType, String optReason, int requestID,
			double requestPower, int startYMD, int startTime, int endYMD, int endTime, int duration,
			String createdDateTime) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.optID = optID;
		this.optType = optType;
		this.optReason = optReason;
		this.requestID = requestID;
		this.requestPower = requestPower;
		this.startYMD = startYMD;
		this.startTime = startTime;
		this.endYMD = endYMD;
		this.endTime = endTime;
		this.duration = duration;
		this.createdDateTime = createdDateTime;
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

	public String getOptType() {
		return optType;
	}

	public void setOptType(String optType) {
		this.optType = optType;
	}

	public String getOptReason() {
		return optReason;
	}

	public void setOptReason(String optReason) {
		this.optReason = optReason;
	}

	public int getRequestID() {
		return requestID;
	}

	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}

	public double getRequestPower() {
		return requestPower;
	}

	public void setRequestPower(double requestPower) {
		this.requestPower = requestPower;
	}

	public int getStartYMD() {
		return startYMD;
	}

	public void setStartYMD(int startYMD) {
		this.startYMD = startYMD;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndYMD() {
		return endYMD;
	}

	public void setEndYMD(int endYMD) {
		this.endYMD = endYMD;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(String createdDateTime) {
		this.createdDateTime = createdDateTime;
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

				+ "\"optID\":\"" + optID + "\","
				+ "\"optType\":\"" + optType + "\","
				+ "\"optReason\":\"" + optReason + "\","
				+ "\"requestID\":" + requestID + ","
				+ "\"requestPower\":" + requestPower + ","
				+ "\"startYMD\":" + startYMD + ","
				+ "\"startTime\":" + startTime + ","
				
				+ "\"endYMD\":" + endYMD + ","
				+ "\"endTime\":" + endTime + ","
				+ "\"duration\":" + duration + ","
				+ "\"service\":\"" + service + "\""
				+ "\"createdDateTime\":\"" + createdDateTime + "\""
				+ "}";
	}
	
	

}
