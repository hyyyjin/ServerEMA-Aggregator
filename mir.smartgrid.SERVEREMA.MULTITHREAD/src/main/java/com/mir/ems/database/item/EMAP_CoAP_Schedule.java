package com.mir.ems.database.item;

public class EMAP_CoAP_Schedule {

	private String optID, optType, emaID;
	private double requestPower;
	private int startYMD, startTime, endYMD, endTime;

	public EMAP_CoAP_Schedule() {

	}

	public EMAP_CoAP_Schedule(String emaID, String optID, String optType, double requestPower, int startYMD,
			int startTime, int endYMD, int endTime) {
		
		this.setEmaID(emaID);
		this.setOptID(optID);
		this.setOptType(optType);
		this.setRequestPower(requestPower);
		this.setStartYMD(startYMD);
		this.setStartTime(startTime);
		this.setEndYMD(endYMD);
		this.setEndTime(endTime);

	}

	public String getOptID() {
		return optID;
	}

	public EMAP_CoAP_Schedule setOptID(String optID) {
		this.optID = optID;
		return this;
	}

	public String getOptType() {
		return optType;
	}

	public EMAP_CoAP_Schedule setOptType(String optType) {
		this.optType = optType;
		return this;
	}

	public String getEmaID() {
		return emaID;
	}

	public EMAP_CoAP_Schedule setEmaID(String emaID) {
		this.emaID = emaID;
		return this;
	}

	public double getRequestPower() {
		return requestPower;
	}

	public EMAP_CoAP_Schedule setRequestPower(double requestPower) {
		this.requestPower = requestPower;
		return this;
	}

	public int getStartYMD() {
		return startYMD;
	}

	public EMAP_CoAP_Schedule setStartYMD(int startYMD) {
		this.startYMD = startYMD;
		return this;
	}

	public int getStartTime() {
		return startTime;
	}

	public EMAP_CoAP_Schedule setStartTime(int startTime) {
		this.startTime = startTime;
		return this;
	}

	public int getEndYMD() {
		return endYMD;
	}

	public EMAP_CoAP_Schedule setEndYMD(int endYMD) {
		this.endYMD = endYMD;
		return this;
	}

	public int getEndTime() {
		return endTime;
	}

	public EMAP_CoAP_Schedule setEndTime(int endTime) {
		this.endTime = endTime;
		return this;
	}

}
