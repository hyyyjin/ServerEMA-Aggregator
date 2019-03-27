package com.mir.ems.database.item;

import java.net.InetAddress;
import java.util.ArrayList;

public class GatewayClass {
	public String gateway_id;
	public int protocol; //0:UDP, 1:MQTT, 2:CoAP
	public InetAddress ip_addr;
	public int port_num;
	public String building;
	public int floor;
	public int room_num;
	public String room_name;
	public ArrayList<Double> value_list;
	public int threshold;
	public int node_id;
	public String gateway_name;
	
	public GatewayClass(String gateway_id, int node_id, int protocol, String building, int floor,
			int room_num, String room_name, int threshold, String gateway_name) {
		this.gateway_id = gateway_id;
		this.protocol = protocol;
		this.building = building;
		this.floor = floor;
		this.room_num = room_num;
		this.room_name = room_name;
		this.threshold = threshold;
		this.node_id = node_id;
		this.gateway_name = gateway_name;
		
		value_list = new ArrayList<Double>();
		for(int i=0; i<45; i++) value_list.add(0.0);
	}
	
	public GatewayClass(String gateway_id, int protocol, InetAddress ip_addr, int port_num, String building, int floor,
			int room_num, String room_name, int threshold) {
		this.gateway_id = gateway_id;
		this.protocol = protocol;
		this.ip_addr = ip_addr;
		this.port_num = port_num;
		this.building = building;
		this.floor = floor;
		this.room_num = room_num;
		this.room_name = room_name;
		this.threshold = threshold;

		value_list = new ArrayList<Double>();
		for(int i=0; i<45; i++) value_list.add(0.0);
	}
	
	public void addValue(int value){
		value_list.add((double)value);
		value_list.remove(0);
	}
	
	public int getMaxValue(){
		double maxValue = 0;
		
		int size = value_list.size();
		for(int i=0; i<size; i++){
			if(value_list.get(i) > maxValue)
				maxValue = value_list.get(i);
		}
		
		return (int)maxValue;
	}
}
