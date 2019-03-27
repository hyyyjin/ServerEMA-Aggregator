package com.mir.ems.profile.emap.v2;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class Available {

	private String dtStart, duration;
	private int startYMD, startTime, endYMD, endTime;
	private double requestPower;

	ArrayList<String> availableParam = new ArrayList<String>();

	public Available addEventParams(String dtStart, String duration, double requestPower, int startYMD,
			int startTime, int endYMD, int endTime) {

		setDtStart(dtStart);
		setDuration(duration);
		setEndTime(endTime);
		setEndYMD(endYMD);
		setRequestPower(requestPower);
		setStartTime(startTime);
		setStartYMD(startYMD);

		this.availableParam.add(toString());

		return this;
	}

	public String getEventParams() {

		return this.availableParam.toString();
	}

	@Override
	public String toString() {
		JSONObject json = new JSONObject();

		try {

			json.put("dtStart", getDtStart());
			json.put("duration", getDuration());
			json.put("requestPower", getRequestPower());
			json.put("startYMD", getStartYMD());
			json.put("startTime", getStartTime());
			json.put("endYMD", getEndYMD());
			json.put("endTime", getEndTime());

			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "wrong";
		}
	}

	public String getDtStart() {
		return dtStart;
	}

	public void setDtStart(String dtStart) {
		this.dtStart = dtStart;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getStartYMD() {
		return startYMD;
	}

	public void setStartYMD(int startYMD) {
		this.startYMD = startYMD;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndYMD() {
		return endYMD;
	}

	public void setEndYMD(int endYMD) {
		this.endYMD = endYMD;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public double getRequestPower() {
		return requestPower;
	}

	public void setRequestPower(double requestPower) {
		this.requestPower = requestPower;
	}

}
