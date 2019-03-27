package com.mir.ems.database.item;

import java.util.ArrayList;

public class GeneratorClass {
	public int protocol;
	public int gateway_id;
	public int device_id;
	public int priority;
	//LED or other
	public String name;
	public int mode;		//on off mode
	public double power;
	public int ChargedEnergy;
	public ArrayList<Double> value_list;
	
	public GeneratorClass(int protocol, int gateway_id, int device_id, String name, int mode, double power, int priority){
//			String product, String date, String time) {
		// TODO Auto-generated constructor stub
		this.protocol = protocol;
		this.gateway_id = gateway_id;
		this.device_id = device_id;

		this.name = name;
		this.mode = mode;
		this.power = power;
		this.priority = priority;
//		this.date = date;
//		this.time = time;
		
		value_list = new ArrayList<Double>();
//		virtual data
/*		Random random = new Random();
		for(int i=0; i<=30; i++) values_list.add(random.nextDouble()*90+10);*/
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
