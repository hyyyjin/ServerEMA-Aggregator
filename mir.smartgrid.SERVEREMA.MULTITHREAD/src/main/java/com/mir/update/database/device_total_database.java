package com.mir.update.database;

import com.mir.ems.globalVar.global;

public class device_total_database {

	private String emaID, deviceEMAID, deviceType, state;
	private int dimming, mode, priority;
	private double power, capacity, volt, hz, chargedEnergy, soc;
	private String timeStamp;

	public device_total_database() {

	}

	public device_total_database(String emaID, String deviceEMAID, String deviceType, String state, int dimming,
			int mode, int priority, double power, double capacity, double volt, double hz, double chargedEnergy,
			double soc, String timeStamp) {

		this.setDeviceInfo(deviceEMAID, deviceType, state, dimming, mode, priority, power, capacity, volt, hz,
				chargedEnergy, soc, timeStamp, state);
	}

	/*
	 * =========================================
	 * 
	 *
	 * FUCNTIONS
	 * 
	 * =========================================
	 * 
	 * 
	 */

	// Appliance LED
	// public void buildUp(String deviceEMAID, String deviceType, double power,
	// String state, int dimming, int priority, Date timeStamp) {
	public void buildUp(String emaID, String deviceEMAID, String deviceType, String state, int dimming, int priority,
			double power, String timeStamp) {

		setEmaID(deviceEMAID);
		setDeviceEMAID(deviceEMAID);
		setDeviceType(deviceType);
		setPower(power);
		setState(state);
		setDimming(dimming);
		setPriority(priority);
		setDate(timeStamp);

		global.saveDevice.put(++global.REPORTSEQ, this);
		++global.REPORTCNT;

	}

	// // PV
	public void buildUp(String deviceEMAID, String deviceType, String state, Double power, int priority,
			String timeStamp) {
		setDeviceEMAID(deviceEMAID);
		setDeviceType(deviceType);
		setState(state);
		setPower(power);
		setPriority(priority);
		setDate(timeStamp);

		global.saveDevice.put(++global.REPORTSEQ, this);
		++global.REPORTCNT;
	}

	// ESS
	// public void buildUp(String deviceEMAID, String deviceType, double power,
	// String state, int priority) {
	//
	// global.saveDevice.put(++global.REPORTSEQ, this);
	// ++global.REPORTCNT;
	// }
	//
	//
	//
	// // RECLOSER
	// public void buildUp(String deviceEMAID, String deviceType, double power,
	// String state, int priority) {
	//
	// global.saveDevice.put(++global.REPORTSEQ, this);
	// ++global.REPORTCNT;
	// }

	/*
	 * =========================================
	 * 
	 *
	 * GETTER && SETTER
	 * 
	 * =========================================
	 * 
	 * 
	 */

	public double getSoc() {
		return soc;
	}

	public String getEmaID() {
		return emaID;
	}

	public void setEmaID(String emaID) {
		this.emaID = emaID;
	}

	public device_total_database setSoc(double soc) {
		this.soc = soc;
		return this;
	}

	public String getDeviceEMAID() {
		return deviceEMAID;
	}

	public device_total_database setDeviceEMAID(String deviceEMAID) {
		this.deviceEMAID = deviceEMAID;
		return this;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public device_total_database setDeviceType(String deviceType) {
		this.deviceType = deviceType;
		return this;
	}

	public String getState() {
		return state;
	}

	public device_total_database setState(String state) {
		this.state = state;
		return this;
	}

	public int getDimming() {
		return dimming;
	}

	public device_total_database setDimming(int dimming) {
		this.dimming = dimming;
		return this;
	}

	public int getMode() {
		return mode;
	}

	public device_total_database setMode(int mode) {
		this.mode = mode;
		return this;
	}

	public int getPriority() {
		return priority;
	}

	public device_total_database setPriority(int priority) {
		this.priority = priority;
		return this;
	}

	public double getPower() {
		return power;
	}

	public device_total_database setPower(double power) {
		this.power = power;
		return this;
	}

	public double getCapacity() {
		return capacity;
	}

	public device_total_database setCapacity(double capacity) {
		this.capacity = capacity;
		return this;
	}

	public double getVolt() {
		return volt;
	}

	public device_total_database setVolt(double volt) {
		this.volt = volt;
		return this;
	}

	public double getHz() {
		return hz;
	}

	public device_total_database setHz(double hz) {
		this.hz = hz;
		return this;
	}

	public double getChargedEnergy() {
		return chargedEnergy;
	}

	public device_total_database setChargedEnergy(double chargedEnergy) {
		this.chargedEnergy = chargedEnergy;
		return this;
	}

	public String getDate() {
		return timeStamp;
	}

	public device_total_database setDate(String date) {
		this.timeStamp = date;
		return this;
	}

	public void setDeviceInfo(String emaID, String deviceEMAID, String deviceType, int dimming, int mode, int priority,
			double power, double capacity, double volt, double hz, double chargedEnergy, double soc, String timeStamp,
			String srcEMA) {

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
		this.setDate(timeStamp);

	}

	public void put(String string, com.mir.update.database.device_total_database device_total_database) {
		// TODO Auto-generated method stub

	}

}
