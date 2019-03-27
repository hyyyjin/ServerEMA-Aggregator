package com.mir.ems.price;

public class Industrial_RealTime {

	private String strTime, endTime;
	private double price;

	public Industrial_RealTime(String strTime, String endTime, double price) {
		
		setStrTime(strTime);
		setEndTime(endTime);
		setPrice(price);
		
	}

	public String getStrTime() {
		return strTime;
	}

	public void setStrTime(String strTime) {
		this.strTime = strTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

}
