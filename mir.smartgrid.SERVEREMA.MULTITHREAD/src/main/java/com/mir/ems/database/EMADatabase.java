package com.mir.ems.database;

import java.util.ArrayList;

import com.mir.ems.database.item.EMAClass;

public class EMADatabase {
	public ArrayList<EMAClass> ema_list;
	
	public EMADatabase(){
		ema_list = new ArrayList<EMAClass>();
	}
	
	public void addValue(String emaName, String protocol, double resource, double threshold){
		ema_list.add(new EMAClass(emaName, protocol, resource, threshold));
	}
	
	public ArrayList<EMAClass> getEMAlist(){
		return ema_list;
	}
}

