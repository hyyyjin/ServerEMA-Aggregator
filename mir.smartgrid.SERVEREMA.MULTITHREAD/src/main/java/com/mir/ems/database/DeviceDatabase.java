package com.mir.ems.database;

import java.util.ArrayList;

import com.mir.ems.database.item.DeviceClass;

public class DeviceDatabase {
	public ArrayList<DeviceClass> led_list;
	public ArrayList<DeviceClass> other_list;

	public DeviceDatabase() {
		led_list = new ArrayList<DeviceClass>();
		other_list = new ArrayList<DeviceClass>();
	}
	
//	public void addValue(int protocol, int node_id, int room_id, int type, 
//			String device_name, int mode, int dimming, int priority){
//		if(type == 1){
//			led_list.add(new DeviceClass(protocol, node_id, room_id, type, device_name, mode, dimming, priority));
//		}
//		else 
//			other_list.add(new DeviceClass(protocol, node_id, room_id, type, device_name, mode, -1, priority));
//	}
//	
	public ArrayList<DeviceClass> getLEDlist(){
		return led_list;
	}
	
	public ArrayList<DeviceClass> getOtherDevicelist(){
		return other_list;
	}
}
