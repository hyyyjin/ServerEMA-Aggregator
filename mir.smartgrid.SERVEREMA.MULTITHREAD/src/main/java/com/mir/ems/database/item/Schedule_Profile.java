package com.mir.ems.database.item;


public class Schedule_Profile {

	private String emaID, optID;
	private Double power;
	private String timeStamp;

	public Schedule_Profile(String emaID, String optID, Double power, String timeStamp) {
		// TODO Auto-generated constructor stub
		this.setEmaID(emaID);
		this.setPower(power);
		this.setTimeStamp(timeStamp);
	}

	public String getEmaID() {
		return emaID;
	}

	public void setEmaID(String emaID) {
		this.emaID = emaID;
	}

	public Double getPower() {
		return power;
	}

	public void setPower(Double power) {
		this.power = power;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getOptID() {
		return optID;
	}

	public void setOptID(String optID) {
		this.optID = optID;
	}

	@Override
	public String toString() {
		return emaID + "/" + optID + "/" + power + "/" + timeStamp;
	}

}
