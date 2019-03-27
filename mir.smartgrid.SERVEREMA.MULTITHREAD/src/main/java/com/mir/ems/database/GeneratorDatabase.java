package com.mir.ems.database;

import java.util.ArrayList;

import com.mir.ems.database.item.GeneratorClass;

public class GeneratorDatabase {
	public ArrayList<GeneratorClass> generator_list;

	public GeneratorDatabase() {
		// TODO Auto-generated constructor stub
		generator_list = new ArrayList<GeneratorClass>();
	}
	
	public void addValue(int protocol, int gateway_id, int device_id, String name, int mode, double power, int priority){
			generator_list.add(new GeneratorClass(protocol, gateway_id, device_id, name, mode, power, priority));

	}
	
	public ArrayList<GeneratorClass> getStoragelist(){
		return generator_list;
	}
}
