package com.mir.ems.database.item;

public class EMAP_CoAP_EMA_DR {

	private boolean eventFlag=false;
	private int startYMD, startTime, endYMD, endTime;
	private double threshold;
	private String eventID;
	public EMAP_CoAP_EMA_DR() {

	}

	public EMAP_CoAP_EMA_DR(boolean eventFlag, int startYMD, int startTime, int endYMD, int endTime, double threshold) {
		
		this.setEventFlag(eventFlag);
		this.setStartYMD(startYMD);
		this.setStartTime(startTime);
		this.setEndYMD(endYMD);
		this.setEndTime(endTime);
		this.setThreshold(threshold);
		
	}

	public boolean isEventFlag() {
		return eventFlag;
	}

	public EMAP_CoAP_EMA_DR setEventFlag(boolean eventFlag) {
		this.eventFlag = eventFlag;
		return this;
	}

	public String getEventID() {
		return eventID;
	}

	public void setEventID(String eventID) {
		this.eventID = eventID;
	}

	public int getStartYMD() {
		return startYMD;
	}

	public EMAP_CoAP_EMA_DR setStartYMD(int startYMD) {
		this.startYMD = startYMD;
		return this;
	}

	public int getStartTime() {
		return startTime;
	}

	public EMAP_CoAP_EMA_DR setStartTime(int startTime) {
		this.startTime = startTime;
		return this;
	}

	public int getEndYMD() {
		return endYMD;
	}

	public EMAP_CoAP_EMA_DR setEndYMD(int endYMD) {
		this.endYMD = endYMD;
		return this;
	}

	public int getEndTime() {
		return endTime;
	}

	public EMAP_CoAP_EMA_DR setEndTime(int endTime) {
		this.endTime = endTime;
		return this;
	}

	public double getThreshold() {
		return threshold;
	}

	public EMAP_CoAP_EMA_DR setThreshold(double threshold) {
		this.threshold = threshold;
		return this;
	}
	
}

