package com.mir.ems.profile.emap.v2;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class Intervals {

	private String duration, uid;
	private double value;
	ArrayList<String> intervalsParam = new ArrayList<String>();

	public Intervals addIntervalsParams(String duration, String uid, double value) {

		setDuration(duration);
		setValue(value);
		setUid(uid);
		
		this.intervalsParam.add(toString());

		return this;
	}

	public String getIntervalsParams() {

		return this.intervalsParam.toString();
	}

	@Override
	public String toString() {
		JSONObject json = new JSONObject();

		try {
			
			json.put("duration", getDuration());
			json.put("uid", getUid());
			json.put("value", getValue());

			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "wrong";
		}
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}


}
