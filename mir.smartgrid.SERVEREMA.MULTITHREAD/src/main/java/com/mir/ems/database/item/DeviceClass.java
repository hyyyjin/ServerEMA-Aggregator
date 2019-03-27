package com.mir.ems.database.item;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.MqttClient;
//import java.util.Random;

public class DeviceClass {
	public int protocol;
	public int node_id;
	public int room_id;
	public int type;		//LED or other
	public String device_name;
	public int mode;		//on off mode
	public int dimming;		//LED: 1~20, other: -1
	public int priority;
	public ArrayList<Double> value_list;
	public String device_id;
	public String ema_id;
	public String ipAddr;
	MqttClient client;
	private double power;
	
	public DeviceClass(int protocol, String device_id, String ema_id, int type, String device_name,
			int mode, int dimming, int priority, String ipAddr, MqttClient client, double power){
//			String product, String date, String time) {
		// TODO Auto-generated constructor stub
		this.protocol = protocol;
		this.device_id = device_id;
		this.ema_id = ema_id;
		this.type = type;
		this.device_name = device_name;
		this.mode = mode;
		this.dimming = dimming;
		this.priority = priority;
		this.ipAddr = ipAddr;
		this.client = client;
		this.power = power;
//		this.date = date;
//		this.time = time;
		
//		value_list = new ArrayList<Double>();
////		virtual data	
///*		Random random = new Random();
//		for(int i=0; i<=30; i++) values_list.add(random.nextDouble()*90+10);*/
//		for(int i=0; i<=150; i++) {
//			value_list.add(0.0);			
//		}
	}
	
	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public void addValuesList(Double new_value){
		value_list.add(new_value);
		value_list.remove(0);
	}
	
	public void deleteValuesList(Double new_value){
		value_list.remove(new_value);
	}
	
	public ArrayList<Double> getValueList(){
		return value_list;
	}

	public int getProtocol() {
		return protocol;
	}

	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}

	public int getNode_id() {
		return node_id;
	}

	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}

	public int getRoom_id() {
		return room_id;
	}

	public void setRoom_id(int room_id) {
		this.room_id = room_id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getDimming() {
		return dimming;
	}

	public void setDimming(int dimming) {
		this.dimming = dimming;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public ArrayList<Double> getValue_list() {
		return value_list;
	}

	public void setValue_list(ArrayList<Double> value_list) {
		this.value_list = value_list;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}

	public String getEma_id() {
		return ema_id;
	}

	public void setEma_id(String ema_id) {
		this.ema_id = ema_id;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public MqttClient getClient() {
		return client;
	}

	public void setClient(MqttClient client) {
		this.client = client;
	}
	
	
	
}
