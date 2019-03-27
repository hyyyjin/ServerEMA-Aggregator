package com.mir.ems.database.item;

import java.util.ArrayList;

public class StorageClass {

	public int gwNum, storage_id, mode, state, capacity, priority;
	public double power, soc, changedEnergy, volt, hz;
	public String name;
	public int protocol;
	
	public ArrayList<Double> value_list;
		
	
	
	public StorageClass(int protocol, int gateway_id, int storage_id, String name, double power, int mode, int state, double changedEnergy,
		int capacity, double soc, double volt, double hz, int priority ){
		
		this.protocol = protocol;
		this.gwNum = gateway_id;
		this.storage_id = storage_id;
		this.name = name;
		this.power = power;
		this.mode = mode;
		this.state = state;
		this.changedEnergy = changedEnergy;
		this.capacity = capacity;
		this.soc = soc;
		this.volt = volt;
		this.hz = hz;
		this.priority = priority;
		value_list = new ArrayList<Double>();

		for(int i=0; i<=30; i++) value_list.add(0.0);
	}
	
	public void addValuesList(Double new_value){
		value_list.add(new_value);
		value_list.remove(0);
	}
	
	public ArrayList<Double> getValueList(){
		return value_list;
	}
}
