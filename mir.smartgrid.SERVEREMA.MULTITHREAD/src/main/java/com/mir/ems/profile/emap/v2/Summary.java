package com.mir.ems.profile.emap.v2;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class Summary {

	private double threshold, avgPower, maxPower, generate, storage, currentPower;
	private String rID;
	ArrayList<String> summaryParam = new ArrayList<String>();

	public Summary addsummaryParam(String rID, double threshold, double avgPower, double maxPower, double generate, double storage, double currentPower) {

		
		setAvgPower(avgPower);
		setCurrentPower(currentPower);
		setGenerate(generate);
		setMaxPower(maxPower);
		setrID(rID);
		setStorage(storage);
		setThreshold(threshold);

		this.summaryParam.add(toString());

		return this;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		
		try {
			json.put("rID", getrID());
			json.put("threshold", getThreshold());
			json.put("avgPower", getAvgPower());
			json.put("maxPower", getMaxPower());
			json.put("generate", getGenerate());
			json.put("storage", getStorage());
			json.put("currentPower", getCurrentPower());

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json.toString();
	}
	
	public String getEventParams() {

		return this.summaryParam.toString();
	}
	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getAvgPower() {
		return avgPower;
	}

	public void setAvgPower(double avgPower) {
		this.avgPower = avgPower;
	}

	public double getMaxPower() {
		return maxPower;
	}

	public void setMaxPower(double maxPower) {
		this.maxPower = maxPower;
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

	public double getCurrentPower() {
		return currentPower;
	}

	public void setCurrentPower(double currentPower) {
		this.currentPower = currentPower;
	}

	public String getrID() {
		return rID;
	}

	public void setrID(String rID) {
		this.rID = rID;
	}
	
}
