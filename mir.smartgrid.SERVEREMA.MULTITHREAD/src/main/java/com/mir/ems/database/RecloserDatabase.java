package com.mir.ems.database;

import java.util.ArrayList;

import com.mir.ems.database.item.RecloserClass;

public class RecloserDatabase {
	
	public ArrayList<RecloserClass> recloser_list;

	public RecloserDatabase() {
		recloser_list = new ArrayList<RecloserClass>();
	}

	public void addValue(int protocol, int gateway_id, int recloser_id, String name, double power, int mode,
			int priority) {
		recloser_list.add(new RecloserClass(protocol, gateway_id, recloser_id, name, power, mode, priority));
	}

	public ArrayList<RecloserClass> getRecloserlist(){
		return recloser_list;
	}
}

