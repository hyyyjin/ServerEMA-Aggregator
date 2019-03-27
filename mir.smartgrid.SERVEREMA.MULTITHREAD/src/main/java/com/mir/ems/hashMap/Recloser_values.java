package com.mir.ems.hashMap;

public class Recloser_values {
	
	public int protocol, recloser_id, mode, priority;
	String gateway_id;
	public double power;
	public String name;
	
	
	public Recloser_values(int protocol, int gateway_id, int recloser_id, String name, double power, int mode, int priority) {

		this.protocol = protocol;
		this.gateway_id = "Gateway"+gateway_id;
		this.recloser_id = recloser_id;
		this.name = name;
		this.mode = mode;
		this.power = power;
		this.priority = priority;
	}

	@Override
	public String toString() {
		return protocol + "/" + gateway_id + "/" + recloser_id + "/"
				+ name + "/" + mode + "/" + power +"/"+priority;
	}
}
