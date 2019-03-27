package com.mir.ems.database;

import java.util.ArrayList;

import com.mir.ems.database.item.SmartMeterClass;

public class SmartMeterDatabase {
	public ArrayList<SmartMeterClass>smartMeter_list;

	public SmartMeterDatabase() {
		smartMeter_list = new ArrayList<SmartMeterClass>();
	}

	public void addValue(int emaNum, String devID, int energyValue) {
		smartMeter_list.add(new SmartMeterClass(emaNum, devID, energyValue));
	}

	public ArrayList<SmartMeterClass> getResourcelist(){
		return smartMeter_list;
	}
}
