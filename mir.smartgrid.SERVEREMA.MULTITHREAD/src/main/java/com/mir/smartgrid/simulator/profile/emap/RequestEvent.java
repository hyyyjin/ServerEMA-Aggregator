package com.mir.smartgrid.simulator.profile.emap;

public class RequestEvent {
	private String srcEMA;
	private String destEMA;
	
	private int requestID;
	private int replyLimit;

	private String time;

	public RequestEvent() {

	}

	public RequestEvent(String srcEMA, String destEMA, int requestID, int replyLimit, String time) {
		this.srcEMA = srcEMA;
		this.destEMA = destEMA;
		this.requestID = requestID;
		this.replyLimit = replyLimit;
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

	public int getReplyLimit() {
		return replyLimit;
	}

	public void setReplyLimit(int replyLimit) {
		this.replyLimit = replyLimit;
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
				+ "\"replyLimit\":" + replyLimit + ","
				
				+ "\"time\":\"" + time + "\""
				+ "}";
	}
	
	
}
