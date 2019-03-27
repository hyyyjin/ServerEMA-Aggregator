package com.mir.ems.profile.emap.v2;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class EventSignals {

	private String intervals, signalName, signalType, signalID, unit;
	private double currentValue, threshold, capacity, price;
	
	ArrayList<String> eventSignalsParam = new ArrayList<String>();

	
	public EventSignals addEventSignalsParams(String intervals, String signalName, String signalType, String signalID, double currentValue, double threshold, double capacity, double price, String unit) {

		
		setCapacity(capacity);
		setCurrentValue(currentValue);
		setIntervals(intervals);
		setPrice(price);
		setSignalID(signalID);
		setSignalName(signalName);
		setSignalType(signalType);
		setThreshold(threshold);
		setUnit(unit);

		this.eventSignalsParam.add(toString());

		return this;
	}

	public String getEventSignalsParams() {

		return this.eventSignalsParam.toString();
	}
	
	@Override
	public String toString() {
		
		
		return "{\"intervals" + "\":" +  getIntervals() + ", "
				+ "\"signalName" + "\":" + "\"" + getSignalName() + "\"" + ", "
				+"\"signalType" + "\":" + "\"" + getSignalType() + "\"" + ", "
				+"\"signalID" + "\":" + "\"" + getSignalID() + "\"" + ", "
				+"\"currentValue" + "\":" + "\"" + getCurrentValue() + "\"" + ", "
				+"\"threshold" + "\":" + "\"" + getThreshold() + "\"" + ", "
				+"\"capacity" + "\":" + "\"" + getCapacity() + "\"" + ", "
				+"\"price" + "\":" + "\"" + getPrice() + "\"" + ", "
				+"\"unit" + "\":" + "\"" + getUnit() + "\"" + "}";
		
		
	}
	
	public String getIntervals() {
		return intervals;
	}
	public void setIntervals(String intervals) {
		this.intervals = intervals;
	}
	public String getSignalName() {
		return signalName;
	}
	public void setSignalName(String signalName) {
		this.signalName = signalName;
	}
	public String getSignalType() {
		return signalType;
	}
	public void setSignalType(String signalType) {
		this.signalType = signalType;
	}
	public String getSignalID() {
		return signalID;
	}
	public void setSignalID(String signalID) {
		this.signalID = signalID;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public double getCurrentValue() {
		return currentValue;
	}
	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}
	public double getThreshold() {
		return threshold;
	}
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	public double getCapacity() {
		return capacity;
	}
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	
	
}
