package com.mir.ems.profile.openadr.recent;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Transports {

	private String transportName;
	ArrayList<String> transportNameParam = new ArrayList<String>();
	JSONArray jsonArray = new JSONArray();
	
	public Transports() {
	}

	public String getTransportName() {
		
		
		return transportName;
	}

	public void setTransportName(String transportName) {
		this.transportName = transportName;
	}

	@Override
	public String toString() {
		JSONObject json = new JSONObject();

		try {
			json.put("oadrTransportName", getTransportName());
			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "wrong";
		}

	}
	
	
	public Transports addTransportNameParams(String transportName){
		
		setTransportName(transportName);
		this.transportNameParam.add(toString());
//		this.jsonArray.put(toString())
		return this;
	}
	
	public ArrayList<String> getTransportNameParams() {

//		System.out.println(this.transportNameParam);
//		JSONArray returnVal = new JSONArray(this.transportNameParam);
		return this.transportNameParam;
	}


}
