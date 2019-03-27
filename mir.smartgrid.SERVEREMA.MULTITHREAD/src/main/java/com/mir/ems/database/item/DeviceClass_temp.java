package com.mir.ems.database.item;

import java.util.Date;

import org.json.JSONObject;

public class DeviceClass_temp {
	
	String emaID, deviceEmaID, state, sort;
	int priority, dimming, mode;
	double devPower, devGenerate, capacity, soc, chargedEnergy, volt, hz;
	Date timeStamp;
	
	
	public DeviceClass_temp(String emaID, String deviceEmaID, String state, String sort, int priority, int dimming, int mode,
			double devPower, double devGenerate, double capacity, double soc, double chargedEnergy, double volt, double hz, Date timeStamp) {

		this.emaID = emaID;
		this.deviceEmaID = deviceEmaID;
		this.state = state;
		this.sort = sort;
		
		this.priority = priority;
		this.dimming = dimming;
		this.mode = mode;
		
		this.devPower = devPower;
		this.devGenerate = devGenerate;
		this.capacity = capacity;
		this.soc = soc;
		this.chargedEnergy = chargedEnergy;
		this.volt = volt;
		this.hz = hz;
		
		this.timeStamp = timeStamp;

	}


	public String getEmaID() {
		return emaID;
	}


	public String getDeviceEmaID() {
		return deviceEmaID;
	}


	public String getState() {
		return state;
	}


	public String getSort() {
		return sort;
	}


	public int getPriority() {
		return priority;
	}


	public int getDimming() {
		return dimming;
	}


	public int getMode() {
		return mode;
	}


	public double getDevPower() {
		return devPower;
	}


	public double getDevGenerate() {
		return devGenerate;
	}


	public double getCapacity() {
		return capacity;
	}


	public double getSoc() {
		return soc;
	}


	public double getChargedEnergy() {
		return chargedEnergy;
	}


	public double getVolt() {
		return volt;
	}


	public double getHz() {
		return hz;
	}


	public Date getTimeStamp() {
		return timeStamp;
	}


	@Override
	public String toString() {
		return "DeviceClass_temp [emaID=" + emaID + ", deviceEmaID=" + deviceEmaID + ", state=" + state + ", sort="
				+ sort + ", priority=" + priority + ", dimming=" + dimming + ", mode=" + mode + ", devPower=" + devPower
				+ ", devGenerate=" + devGenerate + ", capacity=" + capacity + ", soc=" + soc + ", chargedEnergy="
				+ chargedEnergy + ", volt=" + volt + ", hz=" + hz + ", timeStamp=" + timeStamp + "]";
	}

	
}
