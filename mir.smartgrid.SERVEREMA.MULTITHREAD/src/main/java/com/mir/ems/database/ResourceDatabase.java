package com.mir.ems.database;

import java.util.ArrayList;

import com.mir.ems.database.item.ResourceClass;

public class ResourceDatabase {
	
	public ArrayList<ResourceClass> resource_list;

	public ResourceDatabase() {
		resource_list = new ArrayList<ResourceClass>();
	}

	public void addValue(int protocol, int gateway_id, int resource_id, String name, double power, int mode,
			int priority) {
		resource_list.add(new ResourceClass(protocol, gateway_id, resource_id, name, power, mode, priority));
	}

	public ArrayList<ResourceClass> getResourcelist(){
		return resource_list;
	}
}

