package com.mir.smartgrid.simulator.profile.emap;

public class PriceInformation {
	private long id;
	
	private int price;
	private String unit;
	
	public PriceInformation() {

	}

	public PriceInformation(int price, String unit) {
		this.price = price;
		this.unit = unit;
	}
	
	public PriceInformation(PriceInformation price) {
		this.price = price.price;
		this.unit = price.unit;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		return "{\"price\":" + price + ","
				+ "\"unit\":\"" + unit + "\""
				+ "}";
	}
	
	
}
