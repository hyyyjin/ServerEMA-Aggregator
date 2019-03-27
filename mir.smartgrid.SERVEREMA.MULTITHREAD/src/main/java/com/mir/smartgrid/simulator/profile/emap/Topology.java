package com.mir.smartgrid.simulator.profile.emap;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class Topology {
	private long id;

	private String state, deviceEMAID, deviceType;
	private int mode, dimming, priority;
	private double power, capacity, soc, volt, hz, chargedEnergy;
	ArrayList<String> param = new ArrayList<String>();

	public Topology() {

	}

	public Topology(String deviceEMAID, String deviceType, double power, String state, int mode, int dimming,
			double capacity, double soc, double volt, double hz, double chargedEnergy, int priority) {
		this.deviceEMAID = deviceEMAID;
		this.deviceType = deviceType;
		this.power = power;
		this.state = state;
		this.mode = mode;
		this.dimming = dimming;
		this.capacity = capacity;
		this.soc = soc;
		this.volt = volt;
		this.hz = hz;
		this.chargedEnergy = chargedEnergy;
		this.priority = priority;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDeviceEMAID() {
		return deviceEMAID;
	}

	public void setDeviceEMAID(String deviceEMAID) {
		this.deviceEMAID = deviceEMAID;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getDimming() {
		return dimming;
	}

	public void setDimming(int dimming) {
		this.dimming = dimming;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public double getSoc() {
		return soc;
	}

	public void setSoc(double soc) {
		this.soc = soc;
	}

	public double getVolt() {
		return volt;
	}

	public void setVolt(double volt) {
		this.volt = volt;
	}

	public double getHz() {
		return hz;
	}

	public void setHz(double hz) {
		this.hz = hz;
	}

	public double getChargedEnergy() {
		return chargedEnergy;
	}

	public void setChargedEnergy(double chargedEnergy) {
		this.chargedEnergy = chargedEnergy;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public String toString() {

		JSONObject jsonObject = new JSONObject();

		try {
			jsonObject.put("deviceEMAID", getDeviceEMAID());
			jsonObject.put("deviceType", getDeviceType());
			jsonObject.put("power", getPower());
			jsonObject.put("priority", getPriority());

			if (getDeviceType().equals("LED")) {
				jsonObject.put("state", getState());
				jsonObject.put("dimming", getDimming());

			} else if (getDeviceType().equals("Solar")) {

				jsonObject.put("state", getState());

			} else if (getDeviceType().equals("Battery")) {

				jsonObject.put("mode", getMode());
				jsonObject.put("state", getState());
				jsonObject.put("capacity", getCapacity());
				jsonObject.put("soc", getSoc());

				jsonObject.put("volt", getVolt());
				jsonObject.put("hz", getHz());
				jsonObject.put("chargedEnergy", getChargedEnergy());

			} else if (getDeviceType().equals("Recloser")) {

				jsonObject.put("state", getState());
				jsonObject.put("volt", getVolt());
				jsonObject.put("hz", getHz());

			}	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonObject.toString();
	}

	public void addParams(String deviceEMAID, String deviceType, double power, String state, int mode, int dimming,
			double capacity, double soc, double volt, double hz, double chargedEnergy, int priority) {

		setCapacity(capacity);
		setChargedEnergy(chargedEnergy);
		setDeviceEMAID(deviceEMAID);
		setDeviceType(deviceType);
		setDimming(dimming);
		setHz(hz);
		setMode(mode);
		setPower(power);
		setPriority(priority);
		setSoc(soc);
		setState(state);
		setVolt(volt);

		this.param.add(toString());
	}

	public String getParams() {

		return this.param.toString();
	}

	public static void main(String... args) {
		Topology aa = new Topology();

		for (int i = 0; i < 10; i++) {
			
			if( i % 2  == 0){
				aa.addParams("HYUNJINEMA" + i, "Recloser", 1, "ON", 1, 1, 1.1, 1.2, 1.3, 1.4, 1.5, 1);	
			}
			else {
				aa.addParams("HYUNJINEMA" + i, "ESS", 1, "ON", 1, 1, 1.1, 1.2, 1.3, 1.4, 1.5, 1);
			}
		}
	
		System.out.println(aa.getParams());
	}

}
