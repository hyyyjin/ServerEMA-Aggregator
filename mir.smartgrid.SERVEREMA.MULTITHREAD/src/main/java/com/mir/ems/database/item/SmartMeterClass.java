package com.mir.ems.database.item;

import java.util.ArrayList;

public class SmartMeterClass {

	public int emaNum;
	public String devID;
	public int energyValue;
	public ArrayList<Double> value_list = new ArrayList<Double>();

	public SmartMeterClass(int emaNum, String devID, int energyValue) {

		this.emaNum = emaNum;
		this.devID = devID;
		this.energyValue = energyValue;
		for (int i = 0; i <= 30; i++)
			value_list.add(0.0);
	}
}
