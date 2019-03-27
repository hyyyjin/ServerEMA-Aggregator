package com.mir.ems.database.item;

import java.util.Date;

public class ServerEmaProfile {

	String venID, protocol, optType, type, qos, state;
	int emaCnt, dimming, priority;
	double threshold, exceedPower, capacity, power, maxValue, minValue, avgValue;
	Date timeStamp, maxTime, minTime;

	public ServerEmaProfile(){
		
	}
	
	public ServerEmaProfile(String venID, String protocol, String optType, String type, String qos, int emaCnt,
			String state, int dimming, int priority, double threshold, double exceedPower, double capacity,
			double power, double maxValue, double minValue, double avgValue, Date timeStamp, Date maxTime,
			Date minTime) {

		this.venID = venID;
		this.protocol = protocol;
		this.type = type;
		this.qos = qos;
		this.state = state;
		this.emaCnt = emaCnt;
		this.dimming = dimming;
		this.priority = priority;
		this.threshold = threshold;
		this.exceedPower = exceedPower;
		this.capacity = capacity;
		this.power = power;
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.avgValue = avgValue;
		this.timeStamp = timeStamp;
		this.maxTime = maxTime;
		this.minTime = minTime;

	}
	
	@Override
	public String toString() {
		return venID + "/" + protocol + "/" + optType + "/" + type + "/" + qos + "/" + emaCnt + "/" + state + "/"
				+ dimming + "/" + priority + "/" + threshold + "/" + exceedPower + "/" + capacity + "/" + power + "/"
				+ maxValue + "/" + minValue + "/" + avgValue + "/" + timeStamp + "/" + maxTime + "/" + minTime;
	}

	public String getVenID() {
		return venID;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getType() {
		return type;
	}

	public String getQos() {
		return qos;
	}

	public String getState() {
		return state;
	}

	public int getEmaCnt() {
		return emaCnt;
	}

	public int getDimming() {
		return dimming;
	}

	public int getPriority() {
		return priority;
	}

	public double getThreshold() {
		return threshold;
	}

	public double getExceedPower() {
		return exceedPower;
	}

	public double getCapacity() {
		return capacity;
	}

	public double getPower() {
		return power;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public double getAvgValue() {
		return avgValue;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public Date getMaxTime() {
		return maxTime;
	}

	public Date getMinTime() {
		return minTime;
	}

}
