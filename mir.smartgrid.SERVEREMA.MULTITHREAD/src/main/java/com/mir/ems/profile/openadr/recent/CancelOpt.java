package com.mir.ems.profile.openadr.recent;

import org.json.JSONException;
import org.json.JSONObject;

public class CancelOpt {

	private String srcEMA, destEMA, requestID, optID, time, service;

	@Override
	public String toString() {
		JSONObject json = new JSONObject();

		try {

			json.put("SrcEMA", getSrcEMA());
			json.put("DestEMA", getDestEMA());
			json.put("requestID", getRequestID());
			json.put("optID", getOptID());
			json.put("time", getTime());
			json.put("service", getService());

			return json.toString();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return "wrong";
		}
	}

	public String getSrcEMA() {
		return srcEMA;
	}

	public void setSrcEMA(String srcEMA) {
		this.srcEMA = srcEMA;
	}

	public String getDestEMA() {
		return destEMA;
	}

	public void setDestEMA(String destEMA) {
		this.destEMA = destEMA;
	}

	public String getRequestID() {
		return requestID;
	}

	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}

	public String getOptID() {
		return optID;
	}

	public void setOptID(String optID) {
		this.optID = optID;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

}
