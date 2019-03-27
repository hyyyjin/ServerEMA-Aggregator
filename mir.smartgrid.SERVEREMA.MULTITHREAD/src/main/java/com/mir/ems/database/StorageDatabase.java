package com.mir.ems.database;

import java.util.ArrayList;

import com.mir.ems.database.item.StorageClass;

public class StorageDatabase {
	public ArrayList<StorageClass> storage_list;

	public StorageDatabase() {
		storage_list = new ArrayList<StorageClass>();
	}
	

	public void addValue(int protocol, int gateway_id, int storage_id, String name, double power, 
			int mode, int state, double changedEnergy, int capacity, double soc, double volt, double hz, int priority){
			storage_list.add(new StorageClass(protocol, gateway_id, storage_id, name, power, 
					 mode, state, changedEnergy, capacity, soc, volt, hz, priority));
			
	}
	
	public ArrayList<StorageClass> getStoragelist(){
		return storage_list;
	}

}
