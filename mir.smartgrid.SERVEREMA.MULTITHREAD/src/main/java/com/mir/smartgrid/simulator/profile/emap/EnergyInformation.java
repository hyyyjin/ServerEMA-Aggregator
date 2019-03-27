package com.mir.smartgrid.simulator.profile.emap;

public class EnergyInformation {
	private long id;
	
	private double threshold;
	private double currentValue;
	private double capacity;
	private int priority;
	
	public EnergyInformation() {
		
	}

	public EnergyInformation(double threshold, double currentValue, double capacity, int priority) {
		this.threshold = threshold;
		this.currentValue = currentValue;
		this.capacity = capacity;
		this.priority = priority;
	}
	
	public EnergyInformation(EnergyInformation energy) {
		this();
		this.threshold = energy.threshold;
		this.currentValue = energy.currentValue;
		this.capacity = energy.capacity;
		this.priority = energy.priority;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	@Override
	public String toString() {
		return "{\"threshold\":" + threshold + ","
				+ "\"currentValue\":" + currentValue + ","
				+ "\"capacity\":" + capacity + ","
				+ "\"priority\":" + priority + ""
				+ "}";
	}


}
