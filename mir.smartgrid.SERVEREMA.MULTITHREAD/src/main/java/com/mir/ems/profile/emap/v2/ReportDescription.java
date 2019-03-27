package com.mir.ems.profile.emap.v2;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class ReportDescription {

	private String rID, resouceID, deviceType, reportType, itemUnits, siScaleCode, marketContext, minPeriod, maxPeriod,
			itemDescription, powerAttributes, state, time, maxTime, minTime, qos;
	private int dimming, priority;
	private double power, margin, generate, storage, maxValue, minValue, avgValue;
	private boolean onChange;

	ArrayList<String> reportDescriptionParam = new ArrayList<String>();

	public String getrID() {
		return rID;
	}

	public void setrID(String rID) {
		this.rID = rID;
	}

	public String getResouceID() {
		return resouceID;
	}

	public void setResouceID(String resouceID) {
		this.resouceID = resouceID;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getItemUnits() {
		return itemUnits;
	}

	public void setItemUnits(String itemUnits) {
		this.itemUnits = itemUnits;
	}

	public String getSiScaleCode() {
		return siScaleCode;
	}

	public void setSiScaleCode(String siScaleCode) {
		this.siScaleCode = siScaleCode;
	}

	public String getMarketContext() {
		return marketContext;
	}

	public void setMarketContext(String marketContext) {
		this.marketContext = marketContext;
	}

	public String getMinPeriod() {
		return minPeriod;
	}

	public void setMinPeriod(String minPeriod) {
		this.minPeriod = minPeriod;
	}

	public String getMaxPeriod() {
		return maxPeriod;
	}

	public void setMaxPeriod(String maxPeriod) {
		this.maxPeriod = maxPeriod;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public String getPowerAttributes() {
		return powerAttributes;
	}

	public void setPowerAttributes(String powerAttributes) {
		this.powerAttributes = powerAttributes;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	public String getMinTime() {
		return minTime;
	}

	public void setMinTime(String minTime) {
		this.minTime = minTime;
	}

	public int getDimming() {
		return dimming;
	}

	public void setDimming(int dimming) {
		this.dimming = dimming;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public double getMargin() {
		return margin;
	}

	public void setMargin(double margin) {
		this.margin = margin;
	}

	public double getGenerate() {
		return generate;
	}

	public void setGenerate(double generate) {
		this.generate = generate;
	}

	public double getStorage() {
		return storage;
	}

	public void setStorage(double storage) {
		this.storage = storage;
	}

	public double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(double maxValue) {
		this.maxValue = maxValue;
	}

	public double getMinValue() {
		return minValue;
	}

	public void setMinValue(double minValue) {
		this.minValue = minValue;
	}

	public double getAvgValue() {
		return avgValue;
	}

	public void setAvgValue(double avgValue) {
		this.avgValue = avgValue;
	}

	public boolean isOnChange() {
		return onChange;
	}

	public void setOnChange(boolean onChange) {
		this.onChange = onChange;
	}

	public String getQos() {
		return qos;
	}

	public void setQos(String qos) {
		this.qos = qos;
	}

	@Override
	public String toString() {

		JSONObject json = new JSONObject();

		try {
			if (qos != null) {
				json.put("rID", getrID());
				json.put("resourceID", getResouceID());
				json.put("deviceType", getDeviceType());
				json.put("reportType", getReportType());
				json.put("itemUnits", getItemUnits());
				json.put("siScaleCode", getSiScaleCode());
				json.put("marketContext", getMarketContext());
				json.put("minPeriod", getMinPeriod());
				json.put("maxPeriod", getMaxPeriod());
				json.put("onChange", isOnChange());
				json.put("itemDescription", getItemDescription());
				json.put("powerAttributes", getPowerAttributes());
				json.put("state", getState());
				json.put("qos", getQos());
				json.put("power", getPower());
				json.put("dimming", getDimming());
				json.put("margin", getMargin());
				json.put("generate", getGenerate());
				json.put("storage", getStorage());
				json.put("maxValue", getMaxValue());
				json.put("minValue", getMinValue());
				json.put("avgValue", getAvgValue());
				json.put("time", getTime());
				json.put("maxTime", getMaxTime());
				json.put("minTime", getMinTime());
				json.put("priority", getPriority());
			} else {
				
				System.out.println("여기맞지?");
				
				json.put("rID", getrID());
				json.put("resourceID", getResouceID());
				json.put("deviceType", getDeviceType());
				json.put("reportType", getReportType());
				json.put("itemUnits", getItemUnits());
				json.put("siScaleCode", getSiScaleCode());
				json.put("marketContext", getMarketContext());
				json.put("minPeriod", getMinPeriod());
				json.put("maxPeriod", getMaxPeriod());
				json.put("onChange", isOnChange());
				json.put("itemDescription", getItemDescription());
				json.put("powerAttributes", getPowerAttributes());
				json.put("state", getState());
				json.put("power", getPower());
				json.put("dimming", getDimming());
				json.put("margin", getMargin());
				json.put("generate", getGenerate());
				json.put("storage", getStorage());
				json.put("maxValue", getMaxValue());
				json.put("minValue", getMinValue());
				json.put("avgValue", getAvgValue());
				json.put("time", getTime());
				json.put("maxTime", getMaxTime());
				json.put("minTime", getMinTime());
				json.put("priority", getPriority());
			}
			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "wrong";
		}

	}

	public ReportDescription addReportDescriptionParams(String rID, String resourceID, String deviceType,
			String reportType, String itemUnits, String siScaleCode, String marketContext, String minPeriod,
			String maxPeriod, boolean onChange, String itemDescription, String powerAttributes, String state,
			String qos, double power, int dimming, double margin, double generate, double storage, double maxValue,
			double minValue, double avgValue, String time, String maxTime, String minTime, int priority) {

		setAvgValue(avgValue);
		setDeviceType(deviceType);
		setDimming(dimming);
		setGenerate(generate);
		setItemDescription(itemDescription);
		setItemUnits(itemUnits);
		setMargin(margin);
		setMarketContext(marketContext);
		setMaxPeriod(maxPeriod);
		setMaxTime(maxTime);
		setMaxValue(maxValue);
		setMinPeriod(minPeriod);
		setMinTime(minTime);
		setMinValue(minValue);
		setOnChange(onChange);
		setPower(power);
		setPowerAttributes(powerAttributes);
		setPriority(priority);
		setReportType(reportType);
		setResouceID(resourceID);
		setrID(rID);
		setSiScaleCode(siScaleCode);
		setState(state);
		setStorage(storage);
		setTime(time);
		setQos(qos);

		this.reportDescriptionParam.add(toString());

		return this;
	}

	public ReportDescription addReportDescriptionParams(String rID, String resourceID, String deviceType,
			String reportType, String itemUnits, String siScaleCode, String marketContext, String minPeriod,
			String maxPeriod, boolean onChange, String itemDescription, String powerAttributes, String state,
			double power, int dimming, double margin, double generate, double storage, double maxValue, double minValue,
			double avgValue, String time, String maxTime, String minTime, int priority) {

		System.out.println("이거콜햇지?");
		
		setAvgValue(avgValue);
		setDeviceType(deviceType);
		setDimming(dimming);
		setGenerate(generate);
		setItemDescription(itemDescription);
		setItemUnits(itemUnits);
		setMargin(margin);
		setMarketContext(marketContext);
		setMaxPeriod(maxPeriod);
		setMaxTime(maxTime);
		setMaxValue(maxValue);
		setMinPeriod(minPeriod);
		setMinTime(minTime);
		setMinValue(minValue);
		setOnChange(onChange);
		setPower(power);
		setPowerAttributes(powerAttributes);
		setPriority(priority);
		setReportType(reportType);
		setResouceID(resourceID);
		setrID(rID);
		setSiScaleCode(siScaleCode);
		setState(state);
		setStorage(storage);
		setTime(time);

		this.reportDescriptionParam.add(toString());

		return this;
	}

	public String getReportDescriptionParams() {

		return this.reportDescriptionParam.toString();
	}

}
