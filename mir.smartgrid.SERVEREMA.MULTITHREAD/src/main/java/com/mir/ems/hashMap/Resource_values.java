package com.mir.ems.hashMap;

public class Resource_values {
	public int protocol, resource_id, mode, priority;
	String gateway_id;
	public double power;
	public String name;
	
	
	public Resource_values(int protocol, int gateway_id, int resource_id, String name, double power, int mode, int priority) {

		this.protocol = protocol;
		this.gateway_id = "Gateway"+gateway_id;
		this.resource_id = resource_id;
		this.name = name;
		this.mode = mode;
		this.power = power;
		this.priority = priority;
	}

	@Override
	public String toString() {
		return protocol + "/" + gateway_id + "/" + resource_id + "/"
				+ name + "/" + mode + "/" + power +"/"+priority;
	}
}
