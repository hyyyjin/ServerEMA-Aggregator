package com.mir.smartgrid.simulator.profile.emap;

public class MgnInformation {
	private long id;
	
	private int emaCNT;
	private String topology;
	private String state;
	private int dimming;
	private double power;
	private double margin;
	private double generate;
	private double storage;
	private double maxValue;
	private double minValue;
	private double avgValue;
	private String maxTime;
	private String minTime;
	
	private boolean explicit = false;
	
	
	public MgnInformation(int emaCNT, String state, int dimming, double power, double margin,
			double generate, double storage, double maxValue, double minValue, double avgValue, String maxTime,
			String minTime) {
		
		this.explicit = false;
		
		this.emaCNT = emaCNT;
		this.state = state;
		this.dimming = dimming;
		this.power = power;
		this.margin = margin;
		this.generate = generate;
		this.storage = storage;
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.avgValue = avgValue;
		this.maxTime = maxTime;
		this.minTime = minTime;
	}
	
	
	public MgnInformation(int emaCNT, String topology, String state, int dimming, double power, double margin,
			double generate, double storage, double maxValue, double minValue, double avgValue, String maxTime,
			String minTime) {
		
		this.explicit = true;
		
		this.emaCNT = emaCNT;
		this.topology = topology;
		this.state = state;
		this.dimming = dimming;
		this.power = power;
		this.margin = margin;
		this.generate = generate;
		this.storage = storage;
		this.maxValue = maxValue;
		this.minValue = minValue;
		this.avgValue = avgValue;
		this.maxTime = maxTime;
		this.minTime = minTime;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getEmaCNT() {
		return emaCNT;
	}

	public void setEmaCNT(int emaCNT) {
		this.emaCNT = emaCNT;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getDimming() {
		return dimming;
	}

	public void setDimming(int dimming) {
		this.dimming = dimming;
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

	@Override
	public String toString() {
		
		if (this.explicit){
			return "{\"emaCNT\":" + emaCNT + ","
					+ "\"topology\":" + topology.toString() + ","
					+ "\"state\":\"" + state + "\","
					+ "\"dimming\":" + dimming + ","
					+ "\"power\":" + power + ","
					+ "\"margin\":" + margin + ","
					+ "\"generate\":" + generate + ","
					+ "\"storage\":" + storage + ","
					+ "\"maxValue\":" + maxValue + ","
					+ "\"minValue\":" + minValue + ","
					+ "\"avgValue\":" + avgValue + ","
					+ "\"maxTime\":\"" + maxTime + "\","
					+ "\"minTime\":\"" + minTime + "\""
					+ "}";
		}else {
			return "{\"emaCNT\":" + emaCNT + ","
					+ "\"state\":\"" + state + "\","
					+ "\"dimming\":" + dimming + ","
					+ "\"power\":" + power + ","
					+ "\"margin\":" + margin + ","
					+ "\"generate\":" + generate + ","
					+ "\"storage\":" + storage + ","
					+ "\"maxValue\":" + maxValue + ","
					+ "\"minValue\":" + minValue + ","
					+ "\"avgValue\":" + avgValue + ","
					+ "\"maxTime\":\"" + maxTime + "\","
					+ "\"minTime\":\"" + minTime + "\""
					+ "}";
		}
		

	}

}
