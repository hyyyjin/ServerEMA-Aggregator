package com.mir.ems.database;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;

import com.mir.ems.database.item.GatewayClass;

public class GatewayDatabase {
	public HashMap<String, GatewayClass> gateway_list;
	
	public GatewayDatabase() {
		gateway_list = new HashMap<String, GatewayClass>();
	}
	
	public void addValue(String gateway_id, int protocol, InetAddress ip_addr, int port_num, 
			String building, int floor, int room_num, String room_name, int threshold){
		gateway_list.put(gateway_id, new GatewayClass(gateway_id, protocol, ip_addr, port_num, 
				building, floor, room_num, room_name, threshold));
	}
	
	
	public ArrayList<GatewayClass> getGatewayList(){
		ArrayList<GatewayClass> hash_value = new ArrayList<>(gateway_list.values());
		return hash_value;
	}
	
	public ArrayList<String> getBuilding(){	//get building list
		ArrayList<GatewayClass> hash_value = new ArrayList<>(gateway_list.values());
		ArrayList<String> building_list = new ArrayList<String>();
		
		for(GatewayClass gateway: hash_value){
			building_list.add(gateway.gateway_id+ ". "+ gateway.building);
		}
		
		return building_list;
	}
}