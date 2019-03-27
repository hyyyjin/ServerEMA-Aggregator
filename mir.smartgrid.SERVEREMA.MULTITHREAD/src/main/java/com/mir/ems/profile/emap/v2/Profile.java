package com.mir.ems.profile.emap.v2;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Profile {

	private String profileName;
	private ArrayList<String> transports;
	ArrayList<String> profileParam = new ArrayList<String>();

	public Profile() {

	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public ArrayList<String> getTransports() {
		return transports;
	}

	public void setTransports(ArrayList<String> transports) {
		this.transports = transports;
	}

	@Override
	public String toString() {

		return "{\"profileName" + "\":" + "\"" + getProfileName() + "\"" + ", " + "\"transports" + "\":"
				+ getTransports() + "}";

		// try {
		// json.put("profileName", getProfileName());
		// json.put("transports", getTransports());
		// return json.toString();
		//
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		//
		// return "wrong";
		// }

	}

	public Profile addProfileParams(String profileName, ArrayList<String> transports) {

		setProfileName(profileName);
		setTransports(transports);

		this.profileParam.add(toString());
		return this;
	}

	 public String getProfileParams() {
	
	
	 return this.profileParam.toString();

	
	 }

}
