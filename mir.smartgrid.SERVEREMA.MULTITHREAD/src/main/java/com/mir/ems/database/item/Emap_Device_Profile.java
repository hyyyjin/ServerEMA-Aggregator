package com.mir.ems.database.item;

public class Emap_Device_Profile {

	private String emaID, deviceEMAID, deviceType, state, qos, maxTime, minTime;
	private int dimming, mode, priority;
	private double power, capacity, volt, hz, chargedEnergy, soc, margin, generate, storage, maxValue, minValue, avgValue;

	public Emap_Device_Profile() {

	}

	public Emap_Device_Profile(String emaID, String deviceEMAID, String deviceType, String qos, String state, double power, int dimming, double margin,
			double generate, double storage, double maxValue, double minValue, double avgValue, String maxTime,
			String minTime, int priority) {

		setDeviceEMAID(deviceEMAID);
		setDeviceType(deviceType);
		setEmaID(emaID);
		setQos(qos);
		setState(state);
		setPower(power);
		setDimming(dimming);
		setMargin(margin);
		setGenerate(generate);
		setStorage(storage);
		setMaxValue(maxValue);
		setMinValue(minValue);
		setAvgValue(avgValue);
		setMaxTime(maxTime);
		setMinTime(minTime);
		setPriority(priority);
		

	}
	
	
	
	public Emap_Device_Profile(String emaID, String deviceEMAID, String deviceType, String state, int dimming, int mode, int priority,
			double power, double capacity, double volt, double hz, double chargedEnergy, double soc) {
		
		this.setDeviceInfo(emaID, deviceEMAID, deviceType, state, dimming, mode, priority, power, capacity, volt, hz, chargedEnergy, soc);
	}


	
	
	public String getQos() {
		return qos;
	}

	public void setQos(String qos) {
		this.qos = qos;
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

	public String getEmaID() {
		return emaID;
	}

	public Emap_Device_Profile setEmaID(String emaID) {
		this.emaID = emaID;
		return this;
	}

	public double getSoc() {
		return soc;
	}

	public Emap_Device_Profile setSoc(double soc) {
		this.soc = soc;
		return this;
	}

	public String getDeviceEMAID() {
		return deviceEMAID;
	}

	public Emap_Device_Profile setDeviceEMAID(String deviceEMAID) {
		this.deviceEMAID = deviceEMAID;
		return this;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public Emap_Device_Profile setDeviceType(String deviceType) {
		this.deviceType = deviceType;
		return this;
	}

	public String getState() {
		return state;
	}

	public Emap_Device_Profile setState(String state) {
		this.state = state;
		return this;
	}

	public int getDimming() {
		return dimming;
	}

	public Emap_Device_Profile setDimming(int dimming) {
		this.dimming = dimming;
		return this;
	}

	public int getMode() {
		return mode;
	}

	public Emap_Device_Profile setMode(int mode) {
		this.mode = mode;
		return this;
	}

	public int getPriority() {
		return priority;
	}

	public Emap_Device_Profile setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public double getPower() {
		return power;
	}

	public Emap_Device_Profile setPower(double power) {
		this.power = power;
		return this;
	}

	public double getCapacity() {
		return capacity;
	}

	public Emap_Device_Profile setCapacity(double capacity) {
		this.capacity = capacity;
		return this;
	}

	public double getVolt() {
		return volt;
	}

	public Emap_Device_Profile setVolt(double volt) {
		this.volt = volt;
		return this;
	}

	public double getHz() {
		return hz;
	}

	public Emap_Device_Profile setHz(double hz) {
		this.hz = hz;
		return this;
	}
	

	public double getChargedEnergy() {
		return chargedEnergy;
	}

	public Emap_Device_Profile setChargedEnergy(double chargedEnergy) {
		this.chargedEnergy = chargedEnergy;
		return this;
	}
	
	public void setDeviceInfo(String emaID, String deviceEMAID, String deviceType, String state, int dimming, int mode, int priority,
			double power, double capacity, double volt, double hz, double chargedEnergy, double soc){
		
		this.setEmaID(emaID);
		this.setDeviceEMAID(deviceEMAID);
		this.setDeviceType(deviceType);
		this.setState(state);
		this.setDimming(dimming);
		this.setMode(mode);
		this.setPriority(priority);
		this.setPower(power);
		this.setCapacity(capacity);
		this.setVolt(volt);
		this.setHz(hz);
		this.setChargedEnergy(chargedEnergy);
		this.setSoc(soc);
		
		
	}
	

}
