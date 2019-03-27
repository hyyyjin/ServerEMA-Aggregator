package com.mir.ems.hashMap;

public class PV_values {
	public int protocol;
	public String gateway_id;
	public int device_id;
	public String name;
	public int mode;
	public double power;
	public int ChargedEnergy;
	public int priority;

	public PV_values(int protocol, int gateway_id, int device_id, String name, int mode, double power, int priority) {

		this.protocol = protocol;
		this.gateway_id = "Gateway"+gateway_id;
		this.device_id = device_id;
		this.name = name;
		this.mode = mode;
		this.power = power;
		this.priority = priority;
	}

	@Override
	public String toString() {
		return protocol + "/" + gateway_id + "/" + device_id + "/"
				+ name + "/" + mode + "/" + power + "/" + ChargedEnergy+"/"+priority;
	}
}
