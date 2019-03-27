package com.mir.ems.database.item;

import java.util.ArrayList;

public class ResourceClass {
	public int protocol;
	public int gateway_id;
	public int resource_id;
	public int priority;
	public String name;
	public int mode;
	public double power;
	public ArrayList<Double> value_list;

	public ResourceClass(int protocol, int gateway_id, int resource_id, String name, double power, int mode,
			int priority) {
		value_list = new ArrayList<Double>();

		this.protocol = protocol;
		this.gateway_id = gateway_id;
		this.resource_id = resource_id;
		this.name = name;
		this.power = power;
		this.mode = mode;
		this.priority = priority;

		value_list = new ArrayList<Double>();
		for (int i = 0; i <= 30; i++)
			value_list.add(0.0);

	}

	public void addValuesList(Double new_value) {
		value_list.add(new_value);
		value_list.remove(0);
	}

	public ArrayList<Double> getValueList() {
		return value_list;
	}
}
