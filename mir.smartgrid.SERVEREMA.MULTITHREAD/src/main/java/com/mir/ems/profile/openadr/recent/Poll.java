package com.mir.ems.profile.openadr.recent;

import org.json.JSONException;
import org.json.JSONObject;

public class Poll {

	private String venID, service, time;

	@Override
	public String toString() {

		JSONObject json = new JSONObject();

		try {
			json.put("venID", getVenID());
//			json.put("DestEMA", getDestEMA());
			json.put("service", getService());
//			json.put("time", getTime());

			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "wrong";
		}

	}


	public String getVenID() {
		return venID;
	}

	public void setVenID(String venID) {
		this.venID = venID;
	}


	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
