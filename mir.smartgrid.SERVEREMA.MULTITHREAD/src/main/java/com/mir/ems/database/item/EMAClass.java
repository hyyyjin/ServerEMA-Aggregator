package com.mir.ems.database.item;


import java.util.ArrayList;

public class EMAClass {
	public String emaName;
	public String protocol;
	public double resource, threshold;
	public ArrayList<Double> value_list;

	public EMAClass(String emaName, String protocol, double resource, double threshold) {
		this.emaName = emaName;
		this.protocol = protocol;
		this.resource = resource;
		this.threshold = threshold;

		value_list = new ArrayList<Double>();

		for (int i = 0; i <= 30; i++) {
			value_list.add(0.0);
		}

	}
	public void addValuesList(Double new_value){
		value_list.add(new_value);
		value_list.remove(0);
	}
	
	public void deleteValuesList(Double new_value){
		value_list.remove(new_value);
	}
}
